package SELab.service.meeting;

import SELab.domain.*;
import SELab.repository.*;
import SELab.request.meeting.*;
import SELab.utility.contract.ArticleStatus;
import SELab.utility.contract.MeetingStatus;
import SELab.utility.contract.RebuttalStatus;
import SELab.utility.contract.ReviewStatus;
import SELab.utility.response.ResponseGenerator;
import SELab.utility.response.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
public class MeetingReviewService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private PCMemberRelationRepository pcMemberRelationRepository;
    @Autowired
    private ReviewRelationRepository reviewRelationRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private RebuttalRepository rebuttalRepository;

    @Autowired
    public MeetingReviewService(UserRepository userRepository, ArticleRepository articleRepository, MeetingRepository meetingRepository, PCMemberRelationRepository pcMemberRelationRepository, ReviewRelationRepository reviewRelationRepository,PostRepository postRepository,RebuttalRepository rebuttalRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.meetingRepository = meetingRepository;
        this.pcMemberRelationRepository = pcMemberRelationRepository;
        this.reviewRelationRepository = reviewRelationRepository;
        this.postRepository = postRepository;
        this.rebuttalRepository = rebuttalRepository;
    }
    @Transactional
    public ResponseWrapper<?> reviewPost(ReviewPostRequest request) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        PostMessage post = new PostMessage(
                userRepository.findByUsername(request.getPosterName()).getId(),
                Long.parseLong(request.getArticleId()),
                Long.parseLong(request.getTargetId()),
                request.getContent(),
                request.getStatus(),
                timestamp.toString()
        );
        postRepository.save(post);
        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }

    public ResponseWrapper<?> getPostList(String articleId, String postStatus) {
        ArrayList<PostMessage> postList = (ArrayList<PostMessage>) postRepository.findByArticleIdAndStatus(Long.parseLong(articleId),postStatus);
        postList.sort(new Comparator<PostMessage>() {
            @Override
            public int compare(PostMessage o1, PostMessage o2) {
                return Timestamp.valueOf(o1.getTimeStamp()).before(Timestamp.valueOf(o2.getTimeStamp()))?1:-1;
            }
        });
        HashMap<String, ArrayList<HashMap<String, Object>>> body = new HashMap<>();
        ArrayList<HashMap<String, Object>> retList = new ArrayList<>();

        for(PostMessage x: postList){
            HashMap<String,Object> ret = new HashMap<>();

            String targetContent = "";
            PostMessage target = postRepository.findById(x.getTargetId());
            if(target!=null) {
                User targetUser = userRepository.findById(target.getPosterId());
                targetContent = "Response to " + targetUser.getUsername() + ": " + target.getContent();
            }
            ret.put("postId",x.getId());
            ret.put("targetContent",targetContent);
            ret.put("postContent",x.getContent());
            ret.put("posterName",userRepository.findById((long)Long.valueOf(x.getPosterId())).getUsername());
            ret.put("timeStamp",x.getTimeStamp());

            retList.add(ret);
        }
        body.put("postlist",retList);

        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    public ResponseWrapper<?> updateReview(UpdateReviewRequest request) {
        ReviewRelation reviewRelation = reviewRelationRepository.findByReviewerIdAndArticleId(userRepository.findByUsername(request.getPcMemberName()).getId(),Long.valueOf(request.getArticleId()));
        Meeting meeting = meetingRepository.findById((long)reviewRelation.getMeetingId());
        String meetingStatus = meeting.getStatus();
        String updateStatus = request.getStatus();

        if(updateStatus.equals(RebuttalStatus.beforeRebuttal)){
            if (meetingStatus.equals(MeetingStatus.resultPublished) && reviewRelation.getReviewStatus().equals(ReviewStatus.alreadyReviewed)){
                reviewRelation.setScore(Integer.valueOf(request.getScore()));
                reviewRelation.setReviews(request.getReviews());
                reviewRelation.setConfidence(request.getConfidence());
                reviewRelation.setReviewStatus(ReviewStatus.firstUpdated);
                reviewRelationRepository.save(reviewRelation);
                ArticleStatusUpdate(Long.valueOf(request.getArticleId()));

                return new ResponseWrapper<>(200, ResponseGenerator.success, null);
            }
            else{
                return new ResponseWrapper<>(200, "failed : meeting or review status unavailable to do first update(Meeting should be resultPublished and Review should be alreadyReviewed)", null);
            }
        }
        else {
            if (meetingStatus.equals(MeetingStatus.rebuttalFnish) && reviewRelation.getReviewStatus().equals(ReviewStatus.reviewConfirmed)){
                reviewRelation.setScore(Integer.valueOf(request.getScore()));
                reviewRelation.setReviews(request.getReviews());
                reviewRelation.setConfidence(request.getConfidence());
                reviewRelation.setReviewStatus(ReviewStatus.secondUpdated);
                reviewRelationRepository.save(reviewRelation);
                ArticleStatusUpdate(Long.valueOf(request.getArticleId()));

                return new ResponseWrapper<>(200, ResponseGenerator.success, null);
            }
            else{
                return new ResponseWrapper<>(200, "failed : meeting or review status unavailable to do second update(Meeting should be rebuttalFnish and Review should be reviewConfirmed)", null);
            }

        }
    }

    public ResponseWrapper<?> reviewConfirm(ReviewConfirmRequest request) {
        ReviewRelation reviewRelation = reviewRelationRepository.findByReviewerIdAndArticleId(userRepository.findByUsername(request.getPcMemberName()).getId(),Long.valueOf(request.getArticleId()));
        Meeting meeting = meetingRepository.findById((long)reviewRelation.getMeetingId());
        String meetingStatus = meeting.getStatus();
        String confirmStatus = request.getStatus();

        if(confirmStatus.equals(RebuttalStatus.beforeRebuttal)){//第一次确认
            if((reviewRelation.getReviewStatus().equals(ReviewStatus.alreadyReviewed)||reviewRelation.getReviewStatus().equals(ReviewStatus.firstUpdated)) && meetingStatus.equals(MeetingStatus.resultPublished)){
                reviewRelation.setReviewStatus(ReviewStatus.reviewConfirmed);
                reviewRelationRepository.save(reviewRelation);
                meetingStatusModifyBeforeRebuttal(meeting, ReviewStatus.reviewConfirmed, MeetingStatus.reviewConfirmed);
                return new ResponseWrapper<>(200, ResponseGenerator.success, null);
            }
            else {
                return new ResponseWrapper<>(200, "failed : current status unable to do first confirm", null);
            }
        }
        else{//第二次确认
            if((reviewRelation.getReviewStatus().equals(ReviewStatus.reviewConfirmed)||reviewRelation.getReviewStatus().equals(ReviewStatus.secondUpdated)) && meetingStatus.equals(MeetingStatus.rebuttalFnish)){
                reviewRelation.setReviewStatus(ReviewStatus.finalConfirmed);//最终确认
                reviewRelationRepository.save(reviewRelation);
                meetingStatusModifyBeforeRebuttal(meeting, ReviewStatus.finalConfirmed, MeetingStatus.reviewFinish);
                return new ResponseWrapper<>(200, ResponseGenerator.success, null);
            }
            else {
                return new ResponseWrapper<>(200, "failed : current status unable to do second confirm", null);
            }
        }
    }

    private void meetingStatusModifyBeforeRebuttal(Meeting meeting, String reviewConfirmed, String reviewConfirmed2) {
        if (reviewRelationRepository.findByReviewStatusAndMeetingId(reviewConfirmed, meeting.getId()).size() == reviewRelationRepository.findByMeetingId(meeting.getId()).size()) {
            meeting.setStatus(reviewConfirmed2);
            meetingRepository.save(meeting);
        }
    }

    public ResponseWrapper<?> rebuttal(RebuttalRequest request) {
        Article article = articleRepository.findById((long)Long.valueOf(request.getArticleId()));
        Meeting meeting = meetingRepository.findByMeetingName(article.getMeetingname());
        if(!meeting.getStatus().equals(MeetingStatus.reviewConfirmed)){
            return new ResponseWrapper<>(200, "failed : current status unable to rebuttal for meeting status isn't being reviewConfirmed", null);
        }
        if(!article.getStatus().equals(ArticleStatus.rejected) && request.getStatus().equals(RebuttalStatus.rebuttal)){//没有被拒绝
            return new ResponseWrapper<>(200, "failed : current status unable to rebuttal for accepted article", null);
        }
        Rebuttal rebuttal = new Rebuttal(Long.valueOf(request.getArticleId()),request.getContent(),request.getStatus());
        rebuttalRepository.save(rebuttal);
        if(rebuttalRepository.findByIdNot(-1).size()==articleRepository.findByIdNot(-1).size()){
            meeting.setStatus(MeetingStatus.rebuttalFnish);
            meetingRepository.save(meeting);
        }
        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }

    public ResponseWrapper<?> getRebuttalInfo(String articleId) {
        List<Rebuttal> rebuttals = rebuttalRepository.findByArticleId(Long.valueOf(articleId));
        if(rebuttals.isEmpty()){
            return new ResponseWrapper<>(200, "failed : no rebuttal for this article exist", null);
        }
        else{
            Rebuttal rebuttal = rebuttals.get(0);
            return ResponseGenerator.injectObjectFromObjectToResponse("rebuttal",rebuttal,new String[]{"content"},null);
        }
    }

    public ResponseWrapper<?> finalPublish(FinalPublishRequest request) {
        Meeting meeting = meetingRepository.findByMeetingName(request.getMeetingName());
        if (meeting.getStatus().equals(MeetingStatus.reviewFinish)) {
            meeting.setStatus(MeetingStatus.reviewPublish);
            meetingRepository.save(meeting);
            return new ResponseWrapper<>(200, ResponseGenerator.success, null);
        } else {
            return new ResponseWrapper<>(200, "failed: unable to do final publish for incorrect meeting status", null);
        }
    }

    private void ArticleStatusUpdate(long articleId){
        List<ReviewRelation> reviewRelations = reviewRelationRepository.findByArticleId(articleId);
        Article article = articleRepository.findById(articleId);
        int acceptSign = 1;
        for(ReviewRelation x : reviewRelations){
            if(x.getScore()<0){
                acceptSign = 0;
                break;
            }
        }
        article.setStatus(acceptSign>0?ArticleStatus.accepted:ArticleStatus.rejected);
        articleRepository.save(article);
    }
}

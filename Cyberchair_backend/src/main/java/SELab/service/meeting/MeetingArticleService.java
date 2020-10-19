package SELab.service.meeting;

import SELab.domain.*;
import SELab.exception.*;
import SELab.repository.*;
import SELab.request.meeting.BeginReviewRequest;
import SELab.request.meeting.BeginSubmissionRequest;
import SELab.request.meeting.ReviewRequest;
import SELab.utility.contract.*;
import SELab.utility.response.ResponseGenerator;
import SELab.utility.response.ResponseWrapper;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MeetingArticleService {
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

    private Random random = new Random();

    @Autowired
    public MeetingArticleService(UserRepository userRepository, ArticleRepository articleRepository, MeetingRepository meetingRepository, PCMemberRelationRepository pcMemberRelationRepository, ReviewRelationRepository reviewRelationRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.meetingRepository = meetingRepository;
        this.pcMemberRelationRepository = pcMemberRelationRepository;
        this.reviewRelationRepository = reviewRelationRepository;
    }

    @Transactional
    public ResponseWrapper<?> beginSubmission(BeginSubmissionRequest request) {
        String meetingName = request.getMeetingName();
        Meeting meeting = meetingRepository.findByMeetingName(meetingName);
        if (meeting == null) {
            throw new MeetingOfNoExistenceException(meetingName);
        }

        String meetingStatus = meeting.getStatus();
        if(!meetingStatus.equals(MeetingStatus.applyPassed)){
            throw new MeetingUnavaliableToOperateException(meetingName);
        }
        meeting.setStatus(MeetingStatus.submissionAvaliable);
        meetingRepository.save(meeting);
        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }
    @Transactional
    public ResponseWrapper<?> getInfoOfReview(String pcMemberName, String meetingName) {
        User reviewer = userRepository.findByUsername(pcMemberName);
        Meeting meeting = meetingRepository.findByMeetingName(meetingName);

        if (reviewer == null) {
            throw new UserNamedidntExistException(pcMemberName);
        }//用户是否存在

        if (meeting == null) {
            throw new MeetingOfNoExistenceException(meetingName);
        }//会议是否存在

        HashMap<String, Set<HashMap<String, Object>>> body = new HashMap<>();
        Set<HashMap<String, Object>> responseSet = new HashSet<>();

        List<ReviewRelation> reviewRelationList = reviewRelationRepository.findByReviewerIdAndMeetingId(reviewer.getId(),meeting.getId());

        for(ReviewRelation x: reviewRelationList){
            Article article = articleRepository.findById((long)x.getArticleId());

            HashMap<String, Object> response = ResponseGenerator.generate(article,
                    new String[]{"title","topic"}, null);
            response.put("articleId",x.getArticleId());
            response.put("reviewStatus",x.getReviewStatus());

            responseSet.add(response);
        }
        body.put("reviewArticles", responseSet);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }
    @Transactional
    public ResponseWrapper<?> getInfoOfArticleToReview(String pcMemberName, String articleId) {
        ReviewRelation reviewRelation = securityCheckForReview(pcMemberName,articleId);
        long id = Long.valueOf(reviewRelation.getArticleId());
        Article article = articleRepository.findById(id);

        return ResponseGenerator.injectObjectFromObjectToResponse("reviewArticle",article,new String[]{"title","articleAbstract","submitDate","filePath","topic"},null);
    }
    @Transactional
    public ResponseWrapper<?> review(ReviewRequest request) {
        Meeting meeting = meetingRepository.findByMeetingName(articleRepository.findById((long)Long.valueOf(request.getArticleid())).getMeetingname());
        if(!meeting.getStatus().equals(MeetingStatus.reviewing)){
            throw new MeetingStatusUnAvailableToReviewException();
        }
        ReviewRelation reviewRelation = securityCheckForReview(request.getPcMemberName(),request.getArticleid());

        reviewRelation.setScore(Integer.valueOf(request.getScore()));
        reviewRelation.setConfidence(request.getConfidence());
        reviewRelation.setReviews(request.getReviews());
        reviewRelation.setReviewStatus(ReviewStatus.alreadyReviewed);
        reviewRelationRepository.save(reviewRelation);
        ArticleStatusUpdate(Long.valueOf(request.getArticleid()));

        List<ReviewRelation> reviewRelations = reviewRelationRepository.findByReviewStatusAndMeetingId(ReviewStatus.unReviewed,meeting.getId());
        if(reviewRelations.isEmpty()){
            meeting.setStatus(MeetingStatus.reviewCompleted);
            meetingRepository.save(meeting);
        }

        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }
    @Transactional
    public ResponseWrapper<?> getAlreadyReviewedInfo(String pcMemberName, String articleId) {
        return ResponseGenerator.injectObjectFromObjectToResponse("alreadyReviewedInfo",securityCheckForReview(pcMemberName,articleId),new String[]{"score","confidence","reviews"},null);
    }
    @Transactional
    public ResponseWrapper<?> beginReview(BeginReviewRequest request) {
        String meetingName = request.getMeetingName();
        Meeting meeting = meetingRepository.findByMeetingName(meetingName);
        if (meeting == null) {
            throw new MeetingOfNoExistenceException(meetingName);
        }

        List<Article> articles = articleRepository.findByMeetingNameAndStatus(meetingName, ArticleStatus.queuing);
        if(!meeting.getStatus().equals(MeetingStatus.submissionAvaliable)){
            throw new MeetingUnavaliableToOperateException(meetingName);
        }

        if (articles.isEmpty()){
            throw new NoneArticleToReviewException(meetingName);
        }//暂时无人投稿

        List<PCMemberRelation> pcMemberRelations = pcMemberRelationRepository.findByMeetingIdAndStatus(meeting.getId(), PCmemberRelationStatus.accepted);
        if(pcMemberRelations.size()<3){
            throw new AtLeastThreePCMemberException();
        }//pcmember过少

        //选择分配策略
        if(request.getAssignStrategy().equals(ArticleAssignStrategy.loadBalancing)){
            ArticleAssignInLoadBalancing(articles,pcMemberRelations,meeting.getId());
        }
        else if(request.getAssignStrategy().equals(ArticleAssignStrategy.topicRelevant)){
            ArticleAssignInTopicRelevant(articles,pcMemberRelations,meeting.getId());
        }
        //变更会议状态
        meeting.setStatus(MeetingStatus.reviewing);
        meetingRepository.save(meeting);
        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }

    private ReviewRelation securityCheckForReview(String pcMemberName, String articleId){
        User reviewer = userRepository.findByUsername(pcMemberName);

        if (reviewer == null) {
            throw new UserNamedidntExistException(pcMemberName);
        }//用户是否存在

        ReviewRelation reviewRelation = reviewRelationRepository.findByReviewerIdAndArticleId(reviewer.getId(),Long.valueOf(articleId));

        if(reviewRelation==null){
            throw new RejectToReviewException(pcMemberName,articleId);
        }
        return reviewRelation;
    }

    private static boolean flag;

    int countMax(int[] count){
        int mx = -1;
        for(int i : count){
            mx = Math.max(mx, i);
        }
        return mx;
    }

    int countMin(int[] count){
        int mn = 1<<30;
        for(int i : count){
            mn = Math.min(mn, i);
        }
        return mn;
    }

    boolean balanceCheck(int mx, int mn){
        return (mx - mn <= 1);
    }

    boolean checkSamePCMember(int p, int articleNum, int i, int[] ass){
        if((p >= articleNum && ass[p - articleNum] == i)||(p >= articleNum *2 && ass[p - articleNum*2] == i)) return false;
        return true;
    }

    void dfs(List<Article> articles, List<User> pcmembers, int[] ass, int[] count, int p){
        if(flag) return;
        int articleNum = articles.size();
        int mx = countMax(count);
        int mn = countMin(count);
        if(mx - mn > articleNum * 3 - p + 1){ return; }
        if(p == articleNum * 3){
            flag = balanceCheck(mx, mn);
            return;
        }
        Article article = articles.get(p % articleNum);
        Set<Pair<Author,Integer>> authorsAndRank = article.getAuthors();
        Set<User> authors = new HashSet<>();
        for(Pair<Author, Integer> pair : authorsAndRank){
            User author = userRepository.findByEmail(pair.getKey().getEmail());
            if(author == null) continue;
            authors.add(author);
        }
        for(int i=0; i<pcmembers.size(); i++){
            if(authors.contains(pcmembers.get(i))) continue;
            if(!checkSamePCMember(p, articleNum, i, ass)) continue;
            ass[p] = i;
            count[i] += 1;
            dfs(articles, pcmembers, ass, count, p+1);
            if(flag)return;
            count[i] -= 1;
            ass[p] = 0;
        }
    }

    private void ArticleAssignInLoadBalancing(List<Article> articles, List<PCMemberRelation> pcMemberRelations, long meetingId){
        List<User> pcmembers = new ArrayList<>();
        for(PCMemberRelation relation : pcMemberRelations){
            pcmembers.add(userRepository.findById((long)relation.getPcmemberId()));
        }
        int[] ass = new int[articles.size() * 3];
        int[] count = new int[pcmembers.size()];
        flag = false;
        dfs(articles, pcmembers, ass, count, 0);
        if(flag){
            for(int i=0; i < articles.size(); i++){
                reviewRelationRepository.save(new ReviewRelation(pcmembers.get(ass[i]).getId(), meetingId, articles.get(i).getId(),
                        ReviewStatus.unReviewed, 0, null, null));
                reviewRelationRepository.save(new ReviewRelation(pcmembers.get(ass[i + articles.size()]).getId(), meetingId, articles.get(i).getId(),
                        ReviewStatus.unReviewed, 0, null, null));
                reviewRelationRepository.save(new ReviewRelation(pcmembers.get(ass[i + articles.size()*2]).getId(), meetingId, articles.get(i).getId(),
                        ReviewStatus.unReviewed, 0, null, null));
            }
        }
        else throw new cannotAssignArticlesException();
    }

    private void ArticleAssignInTopicRelevant(List<Article> articles,List<PCMemberRelation> pcMemberRelations,long meetingId){

        for(Article article: articles){
            List<PCMemberRelation> pcMemberRelationSetUnConsiderTopic = pcMemberMatchFilter(article,pcMemberRelations,false);
            List<PCMemberRelation> pcMemberRelationSetConsiderTopic = pcMemberMatchFilter(article,pcMemberRelations,true);

            if(pcMemberRelationSetUnConsiderTopic.size()<3){
                throw new AtLeastThreePCMemberException();
            }
            int numOfPCMember = pcMemberRelationSetConsiderTopic.size();
            Set<PCMemberRelation> assignSet = generateAssignSet(numOfPCMember,pcMemberRelationSetUnConsiderTopic,pcMemberRelationSetConsiderTopic);

            for(PCMemberRelation x: assignSet){
                ReviewRelation reviewRelation = new ReviewRelation(x.getPcmemberId(),meetingId,article.getId(),ReviewStatus.unReviewed,0,null,null);
                reviewRelationRepository.save(reviewRelation);
            }
        }
    }

    Set<PCMemberRelation> generateAssignSet(int numOfPCMember,List<PCMemberRelation> pcMemberRelationSetUnConsiderTopic,List<PCMemberRelation> pcMemberRelationSetConsiderTopic){
        Set<PCMemberRelation> assignSet = new HashSet<>();

        if(numOfPCMember<3){
            int size = pcMemberRelationSetUnConsiderTopic.size();
            while (assignSet.size()<3){
                assignSet.add(pcMemberRelationSetUnConsiderTopic.get(random.nextInt(size)));
            }
        }
        else if(numOfPCMember>3){
            int size = pcMemberRelationSetConsiderTopic.size();
            while (assignSet.size()<3){
                assignSet.add(pcMemberRelationSetConsiderTopic.get(random.nextInt(size)));
            }
        }
        else{
            for(PCMemberRelation x: pcMemberRelationSetConsiderTopic){
                assignSet.add(x);
            }
        }
        return assignSet;
    }

    //筛选符合分配要求的PCMemberRelation,必须筛选的是Article的author是否包含该PCMember，通过 topicConsider布尔值决定是否使用topic有交集这一筛选条件
    private List<PCMemberRelation> pcMemberMatchFilter(Article article,List<PCMemberRelation> pcMemberRelations,boolean topicConsider){

            Set<Pair<Author,Integer>> authors = article.getAuthors();
            Set<Long> authorId = new HashSet<>();
            for(Pair<Author,Integer> pair: authors){
                Author author = pair.getKey();
                User authorUser = userRepository.findByFullnameAndEmail(author.getFullname(),author.getEmail());
                if(authorUser==null){
                    authorId.add((long)-1);
                }
                else {
                    authorId.add(authorUser.getId());
                }
            }

        return generatePcMemberRelationSet(topicConsider,authorId,article.getTopic(),pcMemberRelations);
    }

    private List<PCMemberRelation> generatePcMemberRelationSet(boolean topicConsider,Set<Long> authorId, Set<String> topic,List<PCMemberRelation> pcMemberRelations){
        List<PCMemberRelation> pcMemberRelationSet = new ArrayList<>();
        for (PCMemberRelation pcMemberRelation : pcMemberRelations) {
            if(authorId.contains(pcMemberRelation.getPcmemberId())){
                continue;
            }
            if(topicConsider) {
                Set<String> topicForPcm = pcMemberRelation.getTopic();
                for (String x : topicForPcm) {
                    if (topic.contains(x)) {
                        pcMemberRelationSet.add(pcMemberRelation);
                        break;
                    }
                }
            }
            else{
                pcMemberRelationSet.add(pcMemberRelation);
            }
        }
        return pcMemberRelationSet;
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

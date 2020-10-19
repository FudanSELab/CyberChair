package SELab.service.IntegrationTest;

import SELab.domain.*;
import SELab.repository.*;
import SELab.request.meeting.*;
import SELab.service.meeting.MeetingReviewService;
import SELab.utility.contract.MeetingStatus;
import SELab.utility.contract.RebuttalStatus;
import SELab.utility.contract.ReviewConfidence;
import SELab.utility.contract.ReviewStatus;
import TestUtil.TestUtility;
import javafx.geometry.Pos;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ReviewConfirmAndRebuttalTest {
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
    private MeetingReviewService meetingReviewService;

    private Meeting meeting;
    private User chair;
    private User pcm1;
    private User pcm2;
    private User author1;
    private Article article;
    private ReviewRelation reviewRelation1;
    private ReviewRelation reviewRelation2;
    private ReviewRelation reviewRelation3;
    private PostMessage post;
    private ReviewPostRequest reviewPostRequest;


    void setUp(){
        chair = new User(TestUtility.USER_UTIL);
        chair.setUsername("william001");
        userRepository.save(chair);

        meeting = new Meeting(TestUtility.MEETING_UTIL);
        meeting.setStatus(MeetingStatus.resultPublished);
        meetingRepository.save(meeting);

        pcm1 = new User(TestUtility.USER_UTIL);
        pcm1.setUsername("pcmember1");
        pcm1.setEmail("pcmember1@gmail.com");
        userRepository.save(pcm1);

        pcm2 = new User(TestUtility.USER_UTIL);
        pcm2.setUsername("pcmember2");
        pcm2.setEmail("pcmember2@gmail.com");
        userRepository.save(pcm2);

        author1 = new User(TestUtility.USER_UTIL);
        author1.setUsername("author1");
        author1.setEmail("author1@gmail.com");
        userRepository.save(author1);

        article = new Article(TestUtility.article_UTIL);
        article.setContributorName(author1.getUsername());
        articleRepository.save(article);

        reviewRelation1 = new ReviewRelation(TestUtility.reviewRelation_UTIL);
        reviewRelation1.setArticleId(article.getId());
        reviewRelation1.setMeetingId(meeting.getId());
        reviewRelation1.setReviewerId(pcm1.getId());
        reviewRelation1.setReviewStatus(ReviewStatus.alreadyReviewed);
        reviewRelationRepository.save(reviewRelation1);

        reviewRelation2 = new ReviewRelation(TestUtility.reviewRelation_UTIL);
        reviewRelation2.setArticleId(article.getId());
        reviewRelation2.setMeetingId(meeting.getId());
        reviewRelation2.setReviewerId(pcm2.getId());
        reviewRelation2.setReviewStatus(ReviewStatus.alreadyReviewed);
        reviewRelationRepository.save(reviewRelation2);

        reviewRelation3 = new ReviewRelation(TestUtility.reviewRelation_UTIL);
        reviewRelation3.setArticleId(article.getId());
        reviewRelation3.setMeetingId(meeting.getId());
        reviewRelation3.setReviewerId(chair.getId());
        reviewRelation3.setReviewStatus(ReviewStatus.alreadyReviewed);
        reviewRelationRepository.save(reviewRelation3);

        reviewPostRequest = new ReviewPostRequest(TestUtility.reviewPostRequest);
    }

    void tearDown(){
        userRepository.deleteAll();
        articleRepository.deleteAll();
        meetingRepository.deleteAll();
        pcMemberRelationRepository.deleteAll();
        reviewRelationRepository.deleteAll();
        postRepository.deleteAll();
        rebuttalRepository.deleteAll();
    }

    @Test
    public void accessProcessTest(){
        tearDown();
        setUp();

        ReviewPostRequest post1 = new ReviewPostRequest(
                pcm1.getUsername(),
                article.getId().toString(),
                "-1",
                "乌兹行不行？",
                RebuttalStatus.beforeRebuttal
        );
        assertNotNull(meetingReviewService.reviewPost(post1));//发出第一条post

        ArrayList<HashMap<String, Object>> listRepo1 = ((HashMap<String, ArrayList<HashMap<String, Object>>>)meetingReviewService.getPostList(article.getId().toString(),RebuttalStatus.beforeRebuttal).getResponseBody()).get("postlist");
        assertEquals(1,listRepo1.size());
        HashMap<String, Object> repo11 = null;
        for(HashMap<String, Object> obj:listRepo1){
            repo11 = obj;
        }
        assertNotNull(repo11);

        ReviewPostRequest post2 = new ReviewPostRequest(
                pcm2.getUsername(),
                article.getId().toString(),
                repo11.get("postId").toString(),
                "因为你不行，所以你行",
                RebuttalStatus.beforeRebuttal
        );
        assertNotNull(meetingReviewService.reviewPost(post2));//发出第二条post
        ArrayList<HashMap<String, Object>> listRepo2 = ((HashMap<String, ArrayList<HashMap<String, Object>>>)meetingReviewService.getPostList(article.getId().toString(),RebuttalStatus.beforeRebuttal).getResponseBody()).get("postlist");
        assertEquals(listRepo2.get(1).get("postContent"),"乌兹行不行？");
        assertEquals(listRepo2.get(0).get("postContent"),"因为你不行，所以你行");

        ReviewConfirmRequest reviewConfirmRequest1 = new ReviewConfirmRequest(
                pcm1.getUsername(),
                article.getId().toString(),
                RebuttalStatus.beforeRebuttal
        );
        ReviewConfirmRequest reviewConfirmRequest2 = new ReviewConfirmRequest(
                pcm2.getUsername(),
                article.getId().toString(),
                RebuttalStatus.beforeRebuttal
        );
        ReviewConfirmRequest reviewConfirmRequest3 = new ReviewConfirmRequest(
                chair.getUsername(),
                article.getId().toString(),
                RebuttalStatus.beforeRebuttal
        );

        UpdateReviewRequest updateReviewRequest1 = new UpdateReviewRequest(
                pcm1.getUsername(),
                article.getId().toString(),
                "2",
                ReviewConfidence.veryHigh,
                "dsf grefs htrads ASfe erfs",
                RebuttalStatus.beforeRebuttal
        );
        assertNotNull(meetingReviewService.updateReview(updateReviewRequest1));

        assertNotNull(meetingReviewService.reviewConfirm(reviewConfirmRequest1));
        assertNotNull(meetingReviewService.reviewConfirm(reviewConfirmRequest2));
        assertNotNull(meetingReviewService.reviewConfirm(reviewConfirmRequest3));

        assertEquals(meetingRepository.findByMeetingName(meeting.getMeetingName()).getStatus(),MeetingStatus.reviewConfirmed);

        RebuttalRequest rebuttalRequest = new RebuttalRequest(
                article.getId().toString(),
                "",
                RebuttalStatus.giveUp
        );

        assertNotNull(meetingReviewService.rebuttal(rebuttalRequest));

        assertEquals(meetingRepository.findByMeetingName(meeting.getMeetingName()).getStatus(),MeetingStatus.rebuttalFnish);

        reviewConfirmRequest1.setStatus(RebuttalStatus.afterRebuttal);
        reviewConfirmRequest2.setStatus(RebuttalStatus.afterRebuttal);
        reviewConfirmRequest3.setStatus(RebuttalStatus.afterRebuttal);

        assertNotNull(meetingReviewService.reviewConfirm(reviewConfirmRequest1));
        assertNotNull(meetingReviewService.reviewConfirm(reviewConfirmRequest2));
        assertNotNull(meetingReviewService.reviewConfirm(reviewConfirmRequest3));

        assertEquals(meetingRepository.findByMeetingName(meeting.getMeetingName()).getStatus(),MeetingStatus.reviewFinish);

        assertNotNull(meetingReviewService.finalPublish(new FinalPublishRequest(
                meeting.getMeetingName()
        )));
        tearDown();
    }

}

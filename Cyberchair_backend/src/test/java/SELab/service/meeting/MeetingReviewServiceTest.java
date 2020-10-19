package SELab.service.meeting;

import SELab.domain.*;
import SELab.repository.*;
import SELab.request.meeting.*;
import SELab.utility.contract.*;
import TestUtil.TestUtility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class MeetingReviewServiceTest {
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



    @BeforeEach
    void setUp(){
        chair = new User(TestUtility.USER_UTIL);
        chair.setUsername("william001");
        userRepository.save(chair);

        meeting = new Meeting(TestUtility.MEETING_UTIL);
        meeting.setStatus(MeetingStatus.applyPassed);
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
        reviewRelationRepository.save(reviewRelation1);

        reviewRelation2 = new ReviewRelation(TestUtility.reviewRelation_UTIL);
        reviewRelation2.setArticleId(article.getId());
        reviewRelation2.setMeetingId(meeting.getId());
        reviewRelation2.setReviewerId(pcm2.getId());
        reviewRelationRepository.save(reviewRelation2);

        reviewRelation3 = new ReviewRelation(TestUtility.reviewRelation_UTIL);
        reviewRelation3.setArticleId(article.getId());
        reviewRelation3.setMeetingId(meeting.getId());
        reviewRelation3.setReviewerId(chair.getId());
        reviewRelationRepository.save(reviewRelation3);

        reviewPostRequest = new ReviewPostRequest(TestUtility.reviewPostRequest);
    }

    @AfterEach
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
    void reviewPost() {
//        post = new Post(
//                pcm1.getId(),
//                article.getId(),
//                -1,
//                "sdasd terg gdsrfa wde",
//                RebuttalStatus.beforeRebuttal,
//                new Timestamp(System.currentTimeMillis()).toString()
//        );
//        postRepository.save(post);
        ReviewPostRequest reviewPostRequest1 = new ReviewPostRequest(reviewPostRequest);
        reviewPostRequest1.setArticleId(article.getId().toString());
        reviewPostRequest1.setPosterName(pcm1.getUsername());
        reviewPostRequest1.setTargetId("-1");
        assertNotNull(meetingReviewService.reviewPost(reviewPostRequest1));
    }

    @Test
    void getPostList() {
        ReviewPostRequest reviewPostRequest1 = new ReviewPostRequest(reviewPostRequest);
        reviewPostRequest1.setArticleId(article.getId().toString());
        reviewPostRequest1.setPosterName(pcm1.getUsername());
        reviewPostRequest1.setTargetId("-1");
        ReviewPostRequest reviewPostRequest2 = new ReviewPostRequest(reviewPostRequest);
        reviewPostRequest2.setArticleId(article.getId().toString());
        reviewPostRequest2.setPosterName(pcm2.getUsername());
        reviewPostRequest2.setTargetId("-1");
        ReviewPostRequest reviewPostRequest3 = new ReviewPostRequest(reviewPostRequest);
        reviewPostRequest3.setArticleId(article.getId().toString());
        reviewPostRequest3.setPosterName(chair.getUsername());
        reviewPostRequest3.setTargetId("-1");
        assertNotNull(meetingReviewService.reviewPost(reviewPostRequest1));
        assertNotNull(meetingReviewService.reviewPost(reviewPostRequest2));
        assertNotNull(meetingReviewService.reviewPost(reviewPostRequest3));
        assertNotNull(meetingReviewService.getPostList(article.getId().toString(), RebuttalStatus.beforeRebuttal));
    }

    @Test
    void updateReview() {
        UpdateReviewRequest updateReviewRequest = new UpdateReviewRequest(
                pcm1.getUsername(),
                article.getId().toString(),
                "2",
                ReviewConfidence.high,
                "dsf grefs htrads ASfe erfs",
                RebuttalStatus.beforeRebuttal
        );

        reviewRelation1.setReviewStatus(ReviewStatus.alreadyReviewed);
        meeting.setStatus(MeetingStatus.resultPublished);
        reviewRelationRepository.save(reviewRelation1);
        meetingRepository.save(meeting);

        assertNotNull(meetingReviewService.updateReview(updateReviewRequest));

        updateReviewRequest.setStatus(RebuttalStatus.afterRebuttal);
        reviewRelation1.setReviewStatus(ReviewStatus.reviewConfirmed);
        meeting.setStatus(MeetingStatus.rebuttalFnish);
        reviewRelationRepository.save(reviewRelation1);
        meetingRepository.save(meeting);
        assertNotNull(meetingReviewService.updateReview(updateReviewRequest));
    }

    @Test
    void reviewConfirm() {
        ReviewConfirmRequest reviewConfirmRequest = new ReviewConfirmRequest(
                pcm1.getUsername(),
                article.getId().toString(),
                RebuttalStatus.beforeRebuttal
        );

        assertEquals("failed : current status unable to do first confirm",meetingReviewService.reviewConfirm(reviewConfirmRequest).getResponseMessage());
        reviewConfirmRequest.setStatus(RebuttalStatus.afterRebuttal);
        assertEquals("failed : current status unable to do second confirm",meetingReviewService.reviewConfirm(reviewConfirmRequest).getResponseMessage());
        reviewConfirmRequest.setStatus(RebuttalStatus.beforeRebuttal);

        reviewRelation1.setReviewStatus(ReviewStatus.alreadyReviewed);
        meeting.setStatus(MeetingStatus.resultPublished);
        reviewRelationRepository.save(reviewRelation1);
        meetingRepository.save(meeting);

        assertNotNull(meetingReviewService.reviewConfirm(reviewConfirmRequest));

        reviewConfirmRequest.setStatus(RebuttalStatus.afterRebuttal);
        reviewRelation1.setReviewStatus(ReviewStatus.reviewConfirmed);
        meeting.setStatus(MeetingStatus.rebuttalFnish);
        reviewRelationRepository.save(reviewRelation1);
        meetingRepository.save(meeting);

        assertNotNull(meetingReviewService.reviewConfirm(reviewConfirmRequest));
    }

    @Test
    void rebuttal() {
        meeting.setStatus(MeetingStatus.reviewConfirmed);
        article.setStatus(ArticleStatus.rejected);
        article.setMeetingname(meeting.getMeetingName());
        meetingRepository.save(meeting);
        articleRepository.save(article);
        RebuttalRequest rebuttalRequest = new RebuttalRequest(article.getId().toString(),"asd dsuaio frueify adsdyw",RebuttalStatus.rebuttal);
        assertNotNull(meetingReviewService.rebuttal(rebuttalRequest));

        article.setStatus(ArticleStatus.accepted);
        meetingRepository.save(meeting);
        articleRepository.save(article);
        RebuttalRequest rebuttalRequest2 = new RebuttalRequest(article.getId().toString(),"asd dsuaio frueify adsdyw",RebuttalStatus.rebuttal);
        assertEquals("failed : current status unable to rebuttal for accepted article",meetingReviewService.rebuttal(rebuttalRequest2).getResponseMessage());


        meeting.setStatus(MeetingStatus.reviewPublish);
        meetingRepository.save(meeting);
        articleRepository.save(article);
        RebuttalRequest rebuttalRequest1 = new RebuttalRequest(article.getId().toString(),"asd dsuaio frueify adsdyw",RebuttalStatus.rebuttal);
        assertEquals("failed : current status unable to rebuttal for meeting status isn't being reviewConfirmed",meetingReviewService.rebuttal(rebuttalRequest1).getResponseMessage());

    }

    @Test
    void getRebuttalInfo() {
        meeting.setStatus(MeetingStatus.reviewConfirmed);
        article.setStatus(ArticleStatus.rejected);
        article.setMeetingname(meeting.getMeetingName());
        meetingRepository.save(meeting);
        articleRepository.save(article);
        RebuttalRequest rebuttalRequest = new RebuttalRequest(article.getId().toString(),"asd dsuaio frueify adsdyw",RebuttalStatus.rebuttal);
        assertEquals("failed : no rebuttal for this article exist",meetingReviewService.getRebuttalInfo(article.getId().toString()).getResponseMessage());
        assertNotNull(meetingReviewService.rebuttal(rebuttalRequest));
        assertNotNull(meetingReviewService.getRebuttalInfo(article.getId().toString()));
    }

    @Test
    void finalPublish() {
        FinalPublishRequest finalPublishRequest = new FinalPublishRequest(
                meeting.getMeetingName()
        );

        assertEquals("failed: unable to do final publish for incorrect meeting status",meetingReviewService.finalPublish(finalPublishRequest).getResponseMessage());

        meeting.setStatus(MeetingStatus.reviewFinish);
        meetingRepository.save(meeting);
        assertNotNull(meetingReviewService.finalPublish(finalPublishRequest));
    }
}
package SELab.service.meeting;

import SELab.domain.*;
import SELab.exception.*;
import SELab.repository.*;
import SELab.request.meeting.BeginReviewRequest;
import SELab.request.meeting.BeginSubmissionRequest;
import SELab.request.meeting.ReviewRequest;
import SELab.utility.contract.*;
import TestUtil.TestUtility;
import javafx.util.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class MeetingArticleServiceTest {
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
    private MeetingArticleService meetingArticleService;

    private Meeting meeting;

    private User chair;
    private User pcm1;
    private User pcm2;
    private User author1;
    private Article article;
    private ReviewRelation reviewRelation;
    private PCMemberRelation chairRelation;
    private PCMemberRelation pcm1Relation;
    private PCMemberRelation pcm2Relation;

    @BeforeEach
    void setUp() {
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

        chairRelation = new PCMemberRelation(chair.getId(), meeting.getId(), PCmemberRelationStatus.accepted, meeting.getTopic());
        pcm1Relation = new PCMemberRelation(pcm1.getId(), meeting.getId(), PCmemberRelationStatus.accepted, meeting.getTopic());
        pcm2Relation = new PCMemberRelation(pcm2.getId(), meeting.getId(), PCmemberRelationStatus.accepted, meeting.getTopic());
        pcMemberRelationRepository.save(chairRelation);
        pcMemberRelationRepository.save(pcm1Relation);
        pcMemberRelationRepository.save(pcm2Relation);

        article = new Article(TestUtility.article_UTIL);
        article.setContributorName(author1.getUsername());
        articleRepository.save(article);

        reviewRelation = new ReviewRelation(TestUtility.reviewRelation_UTIL);
        reviewRelation.setArticleId(article.getId());
        reviewRelation.setMeetingId(meeting.getId());
        reviewRelation.setReviewerId(pcm1.getId());
        reviewRelationRepository.save(reviewRelation);

    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        articleRepository.deleteAll();
        meetingRepository.deleteAll();
        pcMemberRelationRepository.deleteAll();
        reviewRelationRepository.deleteAll();
    }

    @Test
    void beginSubmission() {
        //检测错误的会议名称异常抛出
        Exception thrown1 = assertThrows(MeetingOfNoExistenceException.class,
                () -> meetingArticleService.beginSubmission(new BeginSubmissionRequest("WrongName")));
        assertEquals("Meeting Named" + "WrongName" + " didn't exist!",
                thrown1.getMessage());

        meeting.setStatus(MeetingStatus.applyFailed);
        meetingRepository.save(meeting);
        Exception thrown2 = assertThrows(MeetingUnavaliableToOperateException.class,
                () -> meetingArticleService.beginSubmission(new BeginSubmissionRequest(meeting.getMeetingName())));
        assertEquals("Meeting Name" + meeting.getMeetingName() + " isn't avaliable to do this operation for current status.(Must unproccessed when ratify,ApplyPassed when beginSubmission,SubmissionAvailable when submission and begin review)",
                thrown2.getMessage());
        meeting.setStatus(MeetingStatus.applyPassed);
        meetingRepository.save(meeting);

        assertNotNull(meetingArticleService.beginSubmission(new BeginSubmissionRequest(meeting.getMeetingName())));
    }

    @Test
    void getInfoOfReview() {
        Exception thrown1 = assertThrows(UserNamedidntExistException.class,
                () -> meetingArticleService.getInfoOfReview("WrongName", meeting.getMeetingName()));
        assertEquals("username " + "WrongName" + " didn't exist!",
                thrown1.getMessage());

        Exception thrown2 = assertThrows(MeetingOfNoExistenceException.class,
                () -> meetingArticleService.getInfoOfReview(pcm1.getUsername(), "WrongName"));
        assertEquals("Meeting Named" + "WrongName" + " didn't exist!",
                thrown2.getMessage());

        assertNotNull(meetingArticleService.getInfoOfReview(pcm1.getUsername(), meeting.getMeetingName()));
    }

    @Test
    void getInfoOfArticleToReview() {
        Exception thrown1 = assertThrows(UserNamedidntExistException.class,
                () -> meetingArticleService.getInfoOfArticleToReview("WrongName", Long.toString(article.getId())));
        assertEquals("username " + "WrongName" + " didn't exist!",
                thrown1.getMessage());

        Exception thrown2 = assertThrows(RejectToReviewException.class,
                () -> meetingArticleService.getInfoOfArticleToReview(pcm1.getUsername(), Long.toString(-1)));
        assertEquals("User " + pcm1.getUsername() + " didn't have a ReviewRelation with " + -1 + " Or Already Reviewed",
                thrown2.getMessage());

        assertNotNull(meetingArticleService.getInfoOfArticleToReview(pcm1.getUsername(), Long.toString(article.getId())));
    }

    @Test
    void review() {
        reviewRelation.setReviewStatus(ReviewStatus.unReviewed);
        reviewRelationRepository.save(reviewRelation);
        ReviewRequest reviewRequest = new ReviewRequest(
                pcm1.getUsername(),
                Long.toString(article.getId()),
                Integer.toString(ReviewScore.accept),
                ReviewConfidence.high,
                "Instantiating CacheAwareContextLoaderDelegate from class"
        );
        meeting.setStatus(MeetingStatus.reviewing);
        meetingRepository.save(meeting);
        assertNotNull(meetingArticleService.review(reviewRequest));
    }

    @Test
    void getAlreadyReviewedInfo() {
        assertNotNull(meetingArticleService.getInfoOfArticleToReview(pcm1.getUsername(), Long.toString(article.getId())));
    }

    @Test
    void beginReviewInLoadBalancing() {
        BeginReviewRequest beginReviewRequest = new BeginReviewRequest(
                meeting.getMeetingName(),
                ArticleAssignStrategy.loadBalancing
        );

        beginReviewRequest.setMeetingName("WrongName");
        Exception thrown1 = assertThrows(MeetingOfNoExistenceException.class,
                () -> meetingArticleService.beginReview(beginReviewRequest));
        assertEquals("Meeting Named" + "WrongName" + " didn't exist!",
                thrown1.getMessage());
        beginReviewRequest.setMeetingName(meeting.getMeetingName());

        Exception thrown2 = assertThrows(MeetingUnavaliableToOperateException.class,
                () -> meetingArticleService.beginReview(beginReviewRequest));
        assertEquals("Meeting Name" + beginReviewRequest.getMeetingName() + " isn't avaliable to do this operation for current status.(Must unproccessed when ratify,ApplyPassed when beginSubmission,SubmissionAvailable when submission and begin review)",
                thrown2.getMessage());
        meeting.setStatus(MeetingStatus.submissionAvaliable);
        meetingRepository.save(meeting);

        article.setMeetingname("WrongName");
        articleRepository.save(article);
        Exception thrown3 = assertThrows(NoneArticleToReviewException.class,
                () -> meetingArticleService.beginReview(beginReviewRequest));
        assertEquals("Meeting Name" + beginReviewRequest.getMeetingName() + " isn't avaliable to begin review because of none article",
                thrown3.getMessage());
        article.setMeetingname(meeting.getMeetingName());
        articleRepository.save(article);

        pcm2Relation.setStatus(PCmemberRelationStatus.rejected);
        pcMemberRelationRepository.save(pcm2Relation);
        Exception thrown4 = assertThrows(AtLeastThreePCMemberException.class,
                () -> meetingArticleService.beginReview(beginReviewRequest));
        assertEquals("There should be at least three available PCMember besides the chair!",
                thrown4.getMessage());
        pcm2Relation.setStatus(PCmemberRelationStatus.accepted);
        pcMemberRelationRepository.save(pcm2Relation);

//        Author author = new Author(chair.getFullname(),chair.getInstitution(),chair.getRegion(),chair.getEmail());
//        Article article1 = new Article(TestUtility.article_UTIL);
//        Pair<Author,Integer> authorPair = new Pair<>(author, 1);
//        article1.setAuthors(new HashSet<Pair<Author,Integer>>(){{add(authorPair);}});
//        articleRepository.save(article1);
//
//        Author author1 = new Author(pcm1.getFullname(),pcm1.getInstitution(),pcm1.getRegion(),pcm1.getEmail());
//        Article article2 = new Article(TestUtility.article_UTIL);
//        Pair<Author,Integer> authorPair1 = new Pair<>(author1, 1);
//        article1.setAuthors(new HashSet<Pair<Author,Integer>>(){{add(authorPair1);}});
//        articleRepository.save(article2);

        assertNotNull(meetingArticleService.beginReview(beginReviewRequest));
        List<ReviewRelation> y = reviewRelationRepository.findByIdNot(-1);
        int c = 0;
    }

    @Test
    void beginReviewInTopicRelevant() {

//        Author author = new Author(chair.getFullname(),chair.getInstitution(),chair.getRegion(),chair.getEmail());
//        Article article1 = new Article(TestUtility.article_UTIL);
//        Pair<Author,Integer> authorPair = new Pair<>(author, 1);
//        article1.setAuthors(new HashSet<Pair<Author,Integer>>(){{add(authorPair);}});
//        articleRepository.save(article1);

        BeginReviewRequest beginReviewRequest = new BeginReviewRequest(
                meeting.getMeetingName(),
                ArticleAssignStrategy.topicRelevant
        );
        meeting.setStatus(MeetingStatus.submissionAvaliable);
        meetingRepository.save(meeting);
        assertNotNull(meetingArticleService.beginReview(beginReviewRequest));
        List<ReviewRelation> y = reviewRelationRepository.findByIdNot(-1);
        int c = 0;
    }
}
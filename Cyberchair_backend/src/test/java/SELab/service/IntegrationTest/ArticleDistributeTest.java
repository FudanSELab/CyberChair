package SELab.service.IntegrationTest;
import SELab.domain.*;
import SELab.repository.*;
import SELab.request.meeting.BeginReviewRequest;
import SELab.service.meeting.MeetingArticleService;
import SELab.utility.contract.*;
import SELab.utility.response.ResponseWrapper;
import TestUtil.TestUtility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class ArticleDistributeTest {
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
        chair.setUsername("qwert1");
        userRepository.save(chair);
        meeting = new Meeting(TestUtility.MEETING_UTIL);
        meeting.setStatus(MeetingStatus.submissionAvaliable);
        meetingRepository.save(meeting);
        pcm1 = new User(TestUtility.USER_UTIL);
        pcm1.setUsername("qwert2");
        pcm1.setEmail("qwert2@123.com");
        userRepository.save(pcm1);
        pcm2 = new User(TestUtility.USER_UTIL);
        pcm2.setUsername("qwert3");
        pcm2.setEmail("qwert3@123.com");
        userRepository.save(pcm2);
        author1 = new User(TestUtility.USER_UTIL);
        author1.setUsername("qwert4");
        author1.setEmail("qwert4@123.com");
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
        HashSet<String> topics = new HashSet<>();
        topics.add("AI");
        article.setTopic(topics);
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
    void beginReviewInLoadBalancing() {
        BeginReviewRequest beginReviewRequest = new BeginReviewRequest(
                meeting.getMeetingName(),
                ArticleAssignStrategy.loadBalancing
        );
        ResponseWrapper<?> resp = meetingArticleService.beginReview(beginReviewRequest);
        assertEquals("success",resp.getResponseMessage());
    }

    @Test
    void beginReviewInTopicRelevant() {
        BeginReviewRequest beginReviewRequest = new BeginReviewRequest(
                meeting.getMeetingName(),
                ArticleAssignStrategy.topicRelevant
        );
        ResponseWrapper<?> resp = meetingArticleService.beginReview(beginReviewRequest);
        assertEquals("success",resp.getResponseMessage());
    }
}
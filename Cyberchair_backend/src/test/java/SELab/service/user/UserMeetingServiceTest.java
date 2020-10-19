package SELab.service.user;

import SELab.domain.Article;
import SELab.domain.Meeting;
import SELab.domain.PCMemberRelation;
import SELab.domain.User;
import SELab.repository.ArticleRepository;
import SELab.repository.MeetingRepository;
import SELab.repository.PCMemberRelationRepository;
import SELab.repository.UserRepository;
import SELab.utility.contract.MeetingStatus;
import SELab.utility.contract.PCmemberRelationStatus;
import TestUtil.TestUtility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class UserMeetingServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private PCMemberRelationRepository pcMemberRelationRepository;

    @Autowired
    private UserMeetingService userMeetingService;

    private Meeting meeting;

    private User chair;
    private User pcm1;
    private User pcm2;

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
        PCMemberRelation chairRelation = new PCMemberRelation(chair.getId(),meeting.getId(), PCmemberRelationStatus.accepted,meeting.getTopic());
        PCMemberRelation pcm1Relation = new PCMemberRelation(pcm1.getId(),meeting.getId(), PCmemberRelationStatus.accepted,meeting.getTopic());
        PCMemberRelation pcm2Relation = new PCMemberRelation(pcm2.getId(),meeting.getId(), PCmemberRelationStatus.undealed,meeting.getTopic());
        pcMemberRelationRepository.save(chairRelation);
        pcMemberRelationRepository.save(pcm1Relation);
        pcMemberRelationRepository.save(pcm2Relation);
        Article article = new Article(TestUtility.article_UTIL);
        article.setContributorName("pcmember1");

    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        pcMemberRelationRepository.deleteAll();
        meetingRepository.deleteAll();
        articleRepository.deleteAll();
    }

    @Test
    void chairMeeting() {
        HashMap<String, Set<HashMap<String, Object>>> ret = (HashMap<String, Set<HashMap<String, Object>>>)userMeetingService.chairMeeting(chair.getUsername()).getResponseBody();
        Set<HashMap<String, Object>> retSet = ret.get("meetings");
        for(HashMap<String, Object> x: retSet){
            assertEquals(meeting.getMeetingName(),x.get("meetingName"));
        }
    }

    @Test
    void pcMemberMeeting() {
        HashMap<String, Set<HashMap<String, Object>>> ret = (HashMap<String, Set<HashMap<String, Object>>>)userMeetingService.pcMemberMeeting(pcm1.getUsername()).getResponseBody();
        Set<HashMap<String, Object>> retSet = ret.get("meetings");
        for(HashMap<String, Object> x: retSet){
            assertEquals(meeting.getMeetingName(),x.get("meetingName"));
        }
    }

    @Test
    void authorMeeting() {
        HashMap<String, Set<HashMap<String, Object>>> ret = (HashMap<String, Set<HashMap<String, Object>>>)userMeetingService.authorMeeting(pcm1.getUsername()).getResponseBody();
        Set<HashMap<String, Object>> retSet = ret.get("meetings");
        for(HashMap<String, Object> x: retSet){
            assertEquals(meeting.getMeetingName(),x.get("meetingName"));
        }
    }

    @Test
    void availableMeeting() {
        HashMap<String, Set<HashMap<String, Object>>> ret = (HashMap<String, Set<HashMap<String, Object>>>)userMeetingService.availableMeeting(pcm1.getUsername()).getResponseBody();
        Set<HashMap<String, Object>> retSet = ret.get("meetings");
        for(HashMap<String, Object> x: retSet){
            assertEquals(meeting.getMeetingName(),x.get("meetingName"));
        }
    }
}
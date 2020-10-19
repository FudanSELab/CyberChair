package SELab.service.meeting;

import SELab.domain.Article;
import SELab.domain.Meeting;
import SELab.domain.PCMemberRelation;
import SELab.domain.User;
import SELab.exception.*;
import SELab.repository.ArticleRepository;
import SELab.repository.MeetingRepository;
import SELab.repository.PCMemberRelationRepository;
import SELab.repository.UserRepository;
import SELab.request.meeting.MeetingApplicationRequest;
import SELab.request.meeting.PCMemberInvitationRequest;
import SELab.request.meeting.ResultPublishRequest;
import SELab.utility.contract.MeetingStatus;
import SELab.utility.contract.PCmemberRelationStatus;
import SELab.utility.response.ResponseGenerator;
import SELab.utility.response.ResponseWrapper;
import TestUtil.TestUtility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class MeetingUtilServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private PCMemberRelationRepository pcMemberRelationRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MeetingUtilService meetingUtilService;

    @BeforeEach
    void setUp() {
        User user1 = new User(TestUtility.USER_UTIL);
        user1.setUsername("william001");
        userRepository.save(user1);
        Meeting meeting1 = new Meeting(TestUtility.MEETING_UTIL);
        meeting1.setMeetingName("WILLIAM MCT PLUS PRO MAX");
        meeting1.setStatus(MeetingStatus.applyPassed);
        meetingRepository.save(meeting1);

        PCMemberRelation pcMemberRelation = new PCMemberRelation(TestUtility.pcMemberRelation_UTIL);
        pcMemberRelation.setPcmemberId(user1.getId());
        pcMemberRelation.setMeetingId(meeting1.getId());
        pcMemberRelationRepository.save(pcMemberRelation);

        User user2 = new User(TestUtility.USER_UTIL);
        user2.setUsername("PCMember1");
        user2.setEmail("random@email.com");
        userRepository.save(user2);

        User author1 = new User(TestUtility.USER_UTIL);
        author1.setUsername("Author1");
        author1.setEmail("author1@gmail.com");
        userRepository.save(author1);

        Article article = new Article(TestUtility.article_UTIL);
        article.setMeetingname("WILLIAM MCT PLUS PRO MAX");
        article.setContributorName("Author1");
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        meetingRepository.deleteAll();
        pcMemberRelationRepository.deleteAll();
    }

    @Test
    void meetingApplication() {
        //检测错误用户异常抛出
        MeetingApplicationRequest meetingApplicationRequest = new MeetingApplicationRequest(TestUtility.meetingApplicationRequest_UTIL);
        meetingApplicationRequest.setChairName("WrongName");
        Exception thrown1 = assertThrows(UserNamedidntExistException.class,
                ()->meetingUtilService.meetingApplication(meetingApplicationRequest));
        assertEquals("username " + meetingApplicationRequest.getChairName() + " didn't exist!",thrown1.getMessage());
        meetingApplicationRequest.setChairName(TestUtility.meetingApplicationRequest_UTIL.getChairName());

        //检测重复的会议名称错误抛出
        meetingApplicationRequest.setMeetingName("WILLIAM MCT PLUS PRO MAX");
        Exception thrown2 = assertThrows(MeetingNameHasbeenregisteredException.class,
                ()->meetingUtilService.meetingApplication(meetingApplicationRequest));
        assertEquals("Meeting Name" + meetingApplicationRequest.getMeetingName() + " has been registered!",thrown2.getMessage());
        meetingApplicationRequest.setMeetingName(TestUtility.meetingApplicationRequest_UTIL.getMeetingName());

        //检测过少的话题异常抛出
        meetingApplicationRequest.setTopic(new HashSet<>());
        Exception thrown3 = assertThrows(AtLeastOneTopicException.class,
                ()->meetingUtilService.meetingApplication(meetingApplicationRequest));
        assertEquals("There should be at least one topic!",thrown3.getMessage());
        meetingApplicationRequest.setTopic(TestUtility.meetingApplicationRequest_UTIL.getTopic());

        //检测正常情况的功能
        ResponseWrapper<?> ret = meetingUtilService.meetingApplication(meetingApplicationRequest);
        assertEquals(ResponseGenerator.success,ret.getResponseMessage());
    }

    @Test
    void getmeetingInfo() {
        //检测错误的会议名称异常抛出
        Exception thrown1 = assertThrows(MeetingOfNoExistenceException.class,
                ()->meetingUtilService.getmeetingInfo("WrongName"));
        assertEquals("Meeting Named" + "WrongName" + " didn't exist!",thrown1.getMessage());

        //检测功能是否正常
        ResponseWrapper<?> ret = meetingUtilService.getmeetingInfo("WILLIAM MCT PLUS PRO MAX");
        HashMap<String, Object> retContent = ((HashMap<String, HashMap<String, Object>>)ret.getResponseBody()).get("meetingInfo");
        assertEquals("WILLIAM MCT PLUS PRO MAX",retContent.get("meetingName"));
    }

    @Test
    void pcmInvitation() {
        //检测错误的会议名称异常抛出
        Exception thrown1 = assertThrows(MeetingOfNoExistenceException.class,
                ()->meetingUtilService.pcmInvitation(TestUtility.pcMemberInvitationRequest_UTIL));
        assertEquals("Meeting Named" + TestUtility.pcMemberInvitationRequest_UTIL.getMeetingName() + " didn't exist!",thrown1.getMessage());

        //检测邀请自己抛出异常
        PCMemberInvitationRequest pcMemberInvitationRequest = new PCMemberInvitationRequest(TestUtility.pcMemberInvitationRequest_UTIL);
        pcMemberInvitationRequest.setPcMemberName("william001");
        pcMemberInvitationRequest.setMeetingName("WILLIAM MCT PLUS PRO MAX");
        Exception thrown2 = assertThrows(InvitationTargetIsForbiddenException.class,
                ()->meetingUtilService.pcmInvitation(pcMemberInvitationRequest));
        assertEquals("Meeting Named" + pcMemberInvitationRequest.getMeetingName() + " can't add Chair as PCMember again!",thrown2.getMessage());

        //检测不可操作的会议状态异常
        pcMemberInvitationRequest.setPcMemberName(TestUtility.pcMemberInvitationRequest_UTIL.getPcMemberName());
        Meeting meeting = meetingRepository.findByMeetingName("WILLIAM MCT PLUS PRO MAX");
        meeting.setStatus(MeetingStatus.applyFailed);
        meetingRepository.save(meeting);
        Exception thrown3 = assertThrows(MeetingStatusUnavailableForPCMemberInvitationException.class,
                ()->meetingUtilService.pcmInvitation(pcMemberInvitationRequest));
        assertEquals("Meeting Named" + "WILLIAM MCT PLUS PRO MAX" + " can't add PCmember in such Status!",thrown3.getMessage());
        meeting.setStatus(MeetingStatus.applyPassed);
        meetingRepository.save(meeting);

        //检测邀请对象是否存在异常抛出
        pcMemberInvitationRequest.setPcMemberName("WrongName");
        Exception thrown4 = assertThrows(UserNamedidntExistException.class,
                ()->meetingUtilService.pcmInvitation(pcMemberInvitationRequest));
        assertEquals("username " + "WrongName" + " didn't exist!",thrown4.getMessage());
        pcMemberInvitationRequest.setPcMemberName(TestUtility.pcMemberInvitationRequest_UTIL.getPcMemberName());

        //检测功能是否正常
        assertNotNull(meetingUtilService.pcmInvitation(pcMemberInvitationRequest));
    }

    @Test
    void getInvitationStatus() {
        //检测错误的会议名称异常抛出
        Exception thrown1 = assertThrows(MeetingOfNoExistenceException.class,
                ()->meetingUtilService.getInvitationStatus("WrongName"));
        assertEquals("Meeting Named" + "WrongName" + " didn't exist!",thrown1.getMessage());

        //检测功能是否正常
        ResponseWrapper<?> ret = meetingUtilService.getInvitationStatus("WILLIAM MCT PLUS PRO MAX");
        Set<HashMap<String, Object>> retContent = ((HashMap<String, Set<HashMap<String, Object>>>)ret.getResponseBody()).get("invitationStatus");
        for (HashMap<String, Object> x: retContent) {
            assertEquals(PCmemberRelationStatus.accepted,x.get("status"));
        }
    }

    @Test
    void getSubmissionList() {
        //检测错误的会议名称异常抛出
        Exception thrown1 = assertThrows(MeetingOfNoExistenceException.class,
                ()->meetingUtilService.getSubmissionList("Author1","WrongName"));
        assertEquals("Meeting Named" + "WrongName" + " didn't exist!",thrown1.getMessage());
        assertNotNull(meetingUtilService.getSubmissionList("Author1","WILLIAM MCT PLUS PRO MAX"));
    }

    @Test
    void reviewPublish(){
        //检测错误的会议名称异常抛出
        Exception thrown1 = assertThrows(MeetingOfNoExistenceException.class,
                ()->meetingUtilService.reviewPublish(new ResultPublishRequest("WrongName")));
        assertEquals("Meeting Named" + "WrongName" + " didn't exist!",
                thrown1.getMessage());

        Meeting meeting = meetingRepository.findByMeetingName("WILLIAM MCT PLUS PRO MAX");
        meeting.setStatus(MeetingStatus.applyFailed);
        meetingRepository.save(meeting);
        Exception thrown2 = assertThrows(MeetingStatusUnAvailableToReviewException.class,
                ()->meetingUtilService.reviewPublish(new ResultPublishRequest("WILLIAM MCT PLUS PRO MAX")));
        assertEquals("Meeting Status UnAvailable To Review or Publish reviews!",
                thrown2.getMessage());
    }
}
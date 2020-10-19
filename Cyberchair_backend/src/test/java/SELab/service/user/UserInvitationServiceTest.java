package SELab.service.user;

import SELab.domain.Meeting;
import SELab.domain.PCMemberRelation;
import SELab.domain.User;
import SELab.repository.MeetingRepository;
import SELab.repository.PCMemberRelationRepository;
import SELab.repository.UserRepository;
import SELab.request.user.InvitationRepoRequest;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class UserInvitationServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private PCMemberRelationRepository pcMemberRelationRepository;

    @Autowired
    private UserInvitationService userInvitationService;

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
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        pcMemberRelationRepository.deleteAll();
        meetingRepository.deleteAll();
    }

    @Test
    void undealedNotifications() {
        HashMap<String, Set<HashMap<String, Object>>> ret = (HashMap<String, Set<HashMap<String, Object>>>)userInvitationService.undealedNotifications(chair.getUsername()).getResponseBody();
        assertNotNull(ret);
        Set<HashMap<String, Object>> retSet = ret.get("invitations");
        for(HashMap<String, Object> x: retSet) {
            assertEquals(chair.getUsername(), x.get("chairName"));
            assertEquals(meeting.getMeetingName(), x.get("meetingName"));
        }

    }

    @Test
    void invitationRepo() {
        InvitationRepoRequest invitationRepoRequest = new InvitationRepoRequest(TestUtility.invitationRepoRequest_UTIL);
        ResponseWrapper<?> ret = userInvitationService.invitationRepo(invitationRepoRequest);
        assertEquals(ResponseGenerator.success,ret.getResponseMessage());
    }

    @Test
    void undealedNotificationsNum() {
        HashMap<String, Object> ret = (HashMap<String, Object>)userInvitationService.undealedNotificationsNum(pcm2.getUsername()).getResponseBody();
        assertNotNull(ret);
        assertEquals(1, ret.get("undealedNotificationsNum"));
    }

    @Test
    void alreadyDealedNotifications() {
        HashMap<String, Set<HashMap<String, Object>>> ret = (HashMap<String, Set<HashMap<String, Object>>>)userInvitationService.alreadyDealedNotifications(chair.getUsername()).getResponseBody();
        assertNotNull(ret);
        Set<HashMap<String, Object>> retSet = ret.get("alreadyDealedNotifications");
        for(HashMap<String, Object> x: retSet) {
            assertEquals(chair.getUsername(), x.get("chairName"));
            assertEquals(meeting.getMeetingName(), x.get("meetingName"));
        }
    }
}
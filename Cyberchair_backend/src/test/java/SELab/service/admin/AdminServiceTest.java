package SELab.service.admin;

import SELab.domain.Meeting;
import SELab.exception.MeetingOfNoExistenceException;
import SELab.exception.MeetingUnavaliableToOperateException;
import SELab.exception.UsernameHasBeenRegisteredException;
import SELab.repository.MeetingRepository;
import SELab.repository.UserRepository;
import SELab.request.admin.ApplicationRatifyRequest;
import SELab.utility.contract.MeetingStatus;
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
class AdminServiceTest {
    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private AdminService adminService;

    private Meeting meeting = new Meeting(TestUtility.MEETING_UTIL);

    @BeforeEach
    void setUp() {
        Meeting meeting1 = new Meeting(meeting);
        meeting1.setStatus(MeetingStatus.unprocessed);
        meeting1.setMeetingName("TestName1");
        Meeting meeting2 = new Meeting(meeting);
        meeting2.setStatus(MeetingStatus.applyPassed);
        meeting2.setMeetingName("TestName2");
        meetingRepository.save(meeting1);
        meetingRepository.save(meeting2);
    }

    @AfterEach
    void tearDown() {
        meetingRepository.deleteAll();
    }

    @Test
    void getqueueingApplication() {
        Set<HashMap<String, Object>> retSet = ((HashMap<String, Set<HashMap<String, Object>>>)adminService.getqueueingApplication().getResponseBody()).get("queueingApplication");
        assertEquals(1,retSet.size());
        for (HashMap<String, Object> x: retSet) {
            assertEquals("TestName1",x.get("meetingName"));
        }
    }

    @Test
    void getalreadyApplication() {
        Set<HashMap<String, Object>> retSet = ((HashMap<String, Set<HashMap<String, Object>>>)adminService.getalreadyApplication().getResponseBody()).get("alreadyApplication");
        assertEquals(1,retSet.size());
        for (HashMap<String, Object> x: retSet) {
            assertEquals("TestName2",x.get("meetingName"));
        }
    }

    @Test
    void applicationRatify() {
        //检测是否功能正常
        ApplicationRatifyRequest applicationRatifyRequest1 = new ApplicationRatifyRequest(TestUtility.applicationRatifyRequest_UTIL);
        applicationRatifyRequest1.setMeetingName("TestName1");
        assertNotNull(adminService.applicationRatify(applicationRatifyRequest1));
        assertEquals(MeetingStatus.applyPassed,meetingRepository.findByMeetingName("TestName1").getStatus());

        //检测错误用户名异常抛出
        ApplicationRatifyRequest applicationRatifyRequest2 = new ApplicationRatifyRequest(TestUtility.applicationRatifyRequest_UTIL);
        applicationRatifyRequest2.setMeetingName("WrongName1");
        Exception thrown1 = assertThrows(MeetingOfNoExistenceException.class,
                ()->adminService.applicationRatify(applicationRatifyRequest2));
        assertEquals("Meeting Named" + "WrongName1" + " didn't exist!",thrown1.getMessage());

        //检测不可操作的会议状态异常
        applicationRatifyRequest1.setMeetingName("TestName2");
        Exception thrown2 = assertThrows(MeetingUnavaliableToOperateException.class,
                ()->adminService.applicationRatify(applicationRatifyRequest1));
        assertEquals("Meeting Name" + "TestName2" + " isn't avaliable to do this operation for current status.(Must unproccessed when ratify,ApplyPassed when beginSubmission,SubmissionAvailable when submission and begin review)",thrown2.getMessage());
    }
}
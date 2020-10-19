package SELab.service.IntegrationTest;

import SELab.domain.Meeting;
import SELab.repository.*;
import SELab.request.admin.ApplicationRatifyRequest;
import SELab.service.admin.AdminService;
import SELab.utility.contract.MeetingStatus;
import TestUtil.TestUtility;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MeetingApproveAndRejectTest {
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private AdminService adminService;
    private Meeting meeting = new Meeting(TestUtility.MEETING_UTIL);
    void setUp(){
        Meeting meeting1 = new Meeting(meeting);
        meeting1.setStatus(MeetingStatus.unprocessed);
        meeting1.setMeetingName("TestMeeting1");
        Meeting meeting2 = new Meeting(meeting);
        meeting2.setStatus(MeetingStatus.unprocessed);
        meeting2.setMeetingName("TestMeeting2");
        meetingRepository.save(meeting1);
        meetingRepository.save(meeting2);
    }
    void tearDown() {
        meetingRepository.deleteAll();
    }

    @Test
    public void ApproveAndRejectTest(){
        setUp();
        ApplicationRatifyRequest applicationRatifyRequest1 = new ApplicationRatifyRequest(TestUtility.applicationRatifyRequest_UTIL);
        applicationRatifyRequest1.setMeetingName("TestMeeting1");
        assertNotNull(adminService.applicationRatify(applicationRatifyRequest1));
        assertEquals(MeetingStatus.applyPassed,meetingRepository.findByMeetingName("TestMeeting1").getStatus());
        ApplicationRatifyRequest applicationRatifyRequest2 = new ApplicationRatifyRequest(TestUtility.applicationRatifyRequest_UTIL);
        applicationRatifyRequest2.setMeetingName("TestMeeting2");
        applicationRatifyRequest2.setApprovalStatus(MeetingStatus.applyFailed);
        assertNotNull(adminService.applicationRatify(applicationRatifyRequest2));
        assertEquals(MeetingStatus.applyFailed,meetingRepository.findByMeetingName("TestMeeting2").getStatus());
        tearDown();
    }
}
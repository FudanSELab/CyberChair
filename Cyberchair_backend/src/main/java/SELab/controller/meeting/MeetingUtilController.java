package SELab.controller.meeting;

import SELab.request.meeting.MeetingApplicationRequest;
import SELab.request.meeting.PCMemberInvitationRequest;
import SELab.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeetingUtilController {
    Logger logger = LoggerFactory.getLogger(MeetingUtilController.class);

    private Service service;

    @Autowired
    public MeetingUtilController(Service service) { this.service = service; }

    @PostMapping("/meeting/application")
    public ResponseEntity<?> meetingApplication(@RequestBody MeetingApplicationRequest request) {
        logger.debug("Meeting application: " + request.toString());
        return ResponseEntity.ok(service.meetingApplication(request));
    }

    @GetMapping("/meeting/meetingInfo")
    public ResponseEntity<?> getmeetingInfo(String meetingName) {
        logger.debug("Meeting Information: " + meetingName);
        return ResponseEntity.ok(service.getmeetingInfo(meetingName));
    }

    @PostMapping("/meeting/pcmInvitation")
    public ResponseEntity<?> pcmInvitation(@RequestBody PCMemberInvitationRequest request) {
        logger.debug("PCMember Invitation: " + request.toString());
        return ResponseEntity.ok(service.pcmInvitation(request));
    }

    @GetMapping("/meeting/invitationStatus")
    public ResponseEntity<?> getInvitationStatus(String meetingName) {
        logger.debug("Invitation Status: " + meetingName);
        return ResponseEntity.ok(service.getInvitationStatus(meetingName));
    }

    @GetMapping("/meeting/submissionList")
    public ResponseEntity<?> getSubmissionList(String authorName,String meetingName) {
        logger.debug("Submission List");
        return ResponseEntity.ok(service.getSubmissionList(authorName,meetingName));
    }

}

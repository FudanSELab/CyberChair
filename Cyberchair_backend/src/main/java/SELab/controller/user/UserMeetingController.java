package SELab.controller.user;

import SELab.controller.util.UtilController;
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
public class UserMeetingController {

    Logger logger = LoggerFactory.getLogger(UserMeetingController.class);
    private Service service;

    @Autowired
    UserMeetingController(Service service){
        this.service = service;
    }

    @GetMapping("/user/chairMeeting")
    public ResponseEntity<?> chairMeeting(String username){
        logger.debug("Get chair meeting info: "+username);
        return ResponseEntity.ok(service.chairMeeting(username));
    }

    @GetMapping("/user/pcMemberMeeting")
    public ResponseEntity<?> pcMemberMeeting(String username){
        logger.debug("Get pcMemberMeeting info : "+username);
        return  ResponseEntity.ok(service.pcMemberMeeting(username));
    }

    @GetMapping("/user/authorMeeting")
    public ResponseEntity<?> authorMeeting(String username){
        logger.debug("Get author meeting info : "+username);
        return  ResponseEntity.ok(service.authorMeeting(username));
    }

    @GetMapping("/user/availableMeeting")
    public ResponseEntity<?> availableMeeting(String username){
        logger.debug("Get available meeting info : "+username);
        return  ResponseEntity.ok(service.availableMeeting(username));
    }
}

package SELab.controller.user;

import SELab.request.user.InvitationRepoRequest;
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
public class UserInvitationController {
    Logger logger = LoggerFactory.getLogger(UserInvitationController.class);
    private Service service;

    @Autowired
    UserInvitationController(Service service){
        this.service = service;
    }

    @GetMapping("/user/undealedNotifications")
    public ResponseEntity<?> undealedNotifications(String username){
        logger.debug("Get undealedNotifications info : "+username);
        return ResponseEntity.ok(service.undealedNotifications(username));
    }

    @PostMapping("/user/invitationRepo")
    public ResponseEntity<?> invitationRepo(@RequestBody InvitationRepoRequest request){
        logger.debug("Post invitationRepo info : "+request.toString());
        return ResponseEntity.ok(service.invitationRepo(request));
    }

    @GetMapping("/user/undealedNotificationsNum")
    public ResponseEntity<?> undealedNotificationsNum(String username){
        logger.debug("Get undealedNotificationsNum info : "+username);
        return  ResponseEntity.ok(service.undealedNotificationsNum(username));
    }

    @GetMapping("/user/alreadyDealedNotifications")
    public ResponseEntity<?> alreadyDealedNotifications(String username){
        logger.debug("Get alreadyDealedNotifications info : "+username);
        return  ResponseEntity.ok(service.alreadyDealedNotifications(username));
    }

}

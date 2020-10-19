package SELab.controller.admin;

import SELab.request.admin.ApplicationRatifyRequest;
import SELab.service.Service;
import SELab.utility.response.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    Logger logger = LoggerFactory.getLogger(AdminController.class);
    private Service service;
    @Autowired
    public AdminController(Service service){this.service = service;}


    @GetMapping("/admin/queueingApplication")
    public ResponseEntity<?> getqueueingApplication() {
        logger.debug("Get queuing applications by admin");
        return ResponseEntity.ok(service.getqueueingApplication());
    }

    @GetMapping("/admin/alreadyApplication")
    public ResponseEntity<?> getalreadyApplication() {
        logger.debug("Get dealed applications by admin");
        return ResponseEntity.ok(service.getalreadyApplication());
    }

    @PostMapping("/admin/ratify")
    public ResponseEntity<?> applicationRatify(@RequestBody ApplicationRatifyRequest request) {
        logger.debug("Ratification for Meeting named "+ request.getMeetingName());
        return ResponseEntity.ok(service.applicationRatify(request));
    }


}

package SELab.controller.meeting;

import SELab.request.meeting.*;
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
public class MeetingReviewController {
    Logger logger = LoggerFactory.getLogger(MeetingArticleController.class);

    private Service service;

    @Autowired
    public MeetingReviewController(Service service) { this.service = service; }

    @PostMapping("/meeting/reviewPost")
    public ResponseEntity<?> reviewPost(@RequestBody ReviewPostRequest request) {
        logger.debug("Review post: " + request.toString());
        return ResponseEntity.ok(service.reviewPost(request));
    }

    @GetMapping("/meeting/postList")
    public ResponseEntity<?> getPostList(String articleId,String postStatus) {
        logger.debug("Get postList article: ID " + articleId +" Post Status: " + postStatus);
        return ResponseEntity.ok(service.getPostList(articleId,postStatus));
    }

    @PostMapping("/meeting/updateReview")
    public ResponseEntity<?> updateReview(@RequestBody UpdateReviewRequest request) {
        logger.debug("update Review: " + request.toString());
        return ResponseEntity.ok(service.updateReview(request));
    }

    @PostMapping("/meeting/reviewConfirm")
    public ResponseEntity<?> reviewConfirm(@RequestBody ReviewConfirmRequest request) {
        logger.debug("Review Confirm: " + request.toString());
        return ResponseEntity.ok(service.reviewConfirm(request));
    }

    @PostMapping("/meeting/rebuttal")
    public ResponseEntity<?> rebuttal(@RequestBody RebuttalRequest request) {
        logger.debug("Rebuttal: " + request.toString());
        return ResponseEntity.ok(service.rebuttal(request));
    }

    @GetMapping("/meeting/rebuttalInfo")
    public ResponseEntity<?> getRebuttalInfo(String articleId) {
        logger.debug("Get Rebuttal Info for article: ID " + articleId);
        return ResponseEntity.ok(service.getRebuttalInfo(articleId));
    }

    @PostMapping("/meeting/finalPublish")
    public ResponseEntity<?> finalPublish(@RequestBody FinalPublishRequest request) {
        logger.debug("Final Publish: " + request.toString());
        return ResponseEntity.ok(service.finalPublish(request));
    }
}

package SELab.controller.meeting;

import SELab.request.meeting.BeginReviewRequest;
import SELab.request.meeting.BeginSubmissionRequest;
import SELab.request.meeting.ResultPublishRequest;
import SELab.request.meeting.ReviewRequest;
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
public class MeetingArticleController {
    Logger logger = LoggerFactory.getLogger(MeetingArticleController.class);

    private Service service;

    @Autowired
    public MeetingArticleController(Service service) { this.service = service; }

    @PostMapping("/meeting/beginSubmission")
    public ResponseEntity<?> beginSubmission(@RequestBody BeginSubmissionRequest request) {
        logger.debug("Begin Submission: " + request.toString());
        return ResponseEntity.ok(service.beginSubmission(request));
    }

    @GetMapping("/meeting/reviewArticles")
    public ResponseEntity<?> getInfoOfReview(String pcMemberName,String meetingName) {
        logger.debug("Get review information: " + meetingName + " " + pcMemberName);
        return ResponseEntity.ok(service.getInfoOfReview(pcMemberName,meetingName));
    }

    @GetMapping("/meeting/reviewArticle")
    public ResponseEntity<?> getInfoOfArticleToReview(String pcMemberName,String articleId) {
        logger.debug("Get Article information: " + articleId + " Reviewer: " + pcMemberName);
        return ResponseEntity.ok(service.getInfoOfArticleToReview(pcMemberName,articleId));
    }

    @PostMapping("/meeting/reviewer")
    public ResponseEntity<?> review(@RequestBody ReviewRequest request) {
        logger.debug("Review: " + request.toString());
        return ResponseEntity.ok(service.review(request));
    }

    @GetMapping("/meeting/alreadyReviewedInfo")
    public ResponseEntity<?> getAlreadyReviewedInfo(String pcMemberName,String articleId) {
        logger.debug("Get Review information: " + articleId + " Reviewer: " + pcMemberName);
        return ResponseEntity.ok(service.getAlreadyReviewedInfo(pcMemberName,articleId));
    }

    @PostMapping("/meeting/beginReview")
    public ResponseEntity<?> beginReview(@RequestBody BeginReviewRequest request) {
        logger.debug("Begin Review: " + request.toString());
        return ResponseEntity.ok(service.beginReview(request));
    }

    @PostMapping("/meeting/publish")
    public ResponseEntity<?> reviewPublish(@RequestBody ResultPublishRequest request) {
        logger.debug("Review Request to Publish: " + request.toString());
        return ResponseEntity.ok(service.reviewPublish(request));
    }
}

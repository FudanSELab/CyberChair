package SELab.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ReviewRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long reviewerId;
    private Long meetingId;
    private Long articleId;

    private String reviewStatus;
    private int score;
    private String confidence;
    private String reviews;

    public ReviewRelation(Long reviewerId, Long meetingId, Long articleId, String reviewStatus, int score, String confidence, String reviews) {
        this.reviewerId = reviewerId;
        this.meetingId = meetingId;
        this.articleId = articleId;
        this.reviewStatus = reviewStatus;
        this.score = score;
        this.confidence = confidence;
        this.reviews = reviews;
    }

    public ReviewRelation(ReviewRelation reviewRelation) {
        this.reviewerId = reviewRelation.getReviewerId();
        this.meetingId = reviewRelation.getMeetingId();
        this.articleId = reviewRelation.getArticleId();
        this.reviewStatus = reviewRelation.getReviewStatus();
        this.score = reviewRelation.getScore();
        this.confidence = reviewRelation.getConfidence();
        this.reviews = reviewRelation.getReviews();
    }

    public Long getId() {
        return id;
    }

    public ReviewRelation(){}

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }
}

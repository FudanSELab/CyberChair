package SELab.request.meeting;

public class UpdateReviewRequest {
    private String pcMemberName;
    private String articleId;
    private String score;
    private String confidence;
    private String reviews;
    private String status;

    public UpdateReviewRequest(){}

    public UpdateReviewRequest(String pcMemberName, String articleId, String score, String confidence, String reviews, String status) {
        this.pcMemberName = pcMemberName;
        this.articleId = articleId;
        this.score = score;
        this.confidence = confidence;
        this.reviews = reviews;
        this.status = status;
    }

    public String getPcMemberName() {
        return pcMemberName;
    }

    public void setPcMemberName(String pcMemberName) {
        this.pcMemberName = pcMemberName;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

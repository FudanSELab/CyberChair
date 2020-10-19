package SELab.request.meeting;

public class ReviewRequest {
    private String pcMemberName;
    private String articleid;
    private String score;
    private String confidence;
    private String reviews;

    public ReviewRequest(String pcMemberName, String articleid, String score, String confidence, String reviews) {
        this.pcMemberName = pcMemberName;
        this.articleid = articleid;
        this.score = score;
        this.confidence = confidence;
        this.reviews = reviews;
    }

    public ReviewRequest(){}

    public String getPcMemberName() {
        return pcMemberName;
    }

    public void setPcMemberName(String pcMemberName) {
        this.pcMemberName = pcMemberName;
    }

    public String getArticleid() {
        return articleid;
    }

    public void setArticleid(String articleid) {
        this.articleid = articleid;
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
}

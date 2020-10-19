package SELab.request.meeting;

public class ReviewPostRequest {
    private String posterName;
    private String articleId;
    private String targetId;
    private String content;
    private String status;

    public ReviewPostRequest(){}

    public ReviewPostRequest(String posterName, String articleId, String targetId, String content, String status) {
        this.posterName = posterName;
        this.articleId = articleId;
        this.targetId = targetId;
        this.content = content;
        this.status = status;
    }

    public ReviewPostRequest(ReviewPostRequest reviewPostRequest){
        this.posterName = reviewPostRequest.getPosterName();
        this.articleId = reviewPostRequest.getArticleId();
        this.targetId = reviewPostRequest.getTargetId();
        this.content = reviewPostRequest.getContent();
        this.status = reviewPostRequest.getStatus();
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

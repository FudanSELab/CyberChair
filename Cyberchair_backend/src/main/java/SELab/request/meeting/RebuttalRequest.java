package SELab.request.meeting;

public class RebuttalRequest {

    private String articleId;
    private String content;
    private String status;

    public RebuttalRequest(){}

    public RebuttalRequest(String articleId, String content, String status) {
        this.articleId = articleId;
        this.content = content;
        this.status = status;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
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

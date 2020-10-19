package SELab.request.meeting;

public class ReviewConfirmRequest {

    private String pcMemberName;
    private String articleId;
    private String status;

    public ReviewConfirmRequest(){}

    public ReviewConfirmRequest(String pcMemberName, String articleId, String status) {
        this.pcMemberName = pcMemberName;
        this.articleId = articleId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

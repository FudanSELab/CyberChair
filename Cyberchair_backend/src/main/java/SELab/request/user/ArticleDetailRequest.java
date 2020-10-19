package SELab.request.user;

public class ArticleDetailRequest {
    private Long articleId;

    public ArticleDetailRequest(){}

    public ArticleDetailRequest(Long articleId) {
        this.articleId = articleId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }
}

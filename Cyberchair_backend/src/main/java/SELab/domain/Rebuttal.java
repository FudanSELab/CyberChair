package SELab.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class Rebuttal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private long articleId;
    private String content;
    private String status;


    public Rebuttal(){}

    public Rebuttal(Rebuttal rebuttal){
        new Rebuttal(rebuttal.getArticleId(),rebuttal.getContent(),rebuttal.getStatus());
    }

    public Rebuttal(long articleId, String content, String status) {
        this.articleId = articleId;
        this.content = content;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
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

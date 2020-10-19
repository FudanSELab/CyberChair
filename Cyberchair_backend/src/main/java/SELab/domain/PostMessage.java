package SELab.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PostMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private long posterId;
    private long articleId;
    private long targetId;
    private String content;
    private String status;
    private String timeStamp;


    public PostMessage() {}

    public PostMessage(long posterId, long articleId, long targetId, String content, String status, String timeStamp) {
        this.posterId = posterId;
        this.articleId = articleId;
        this.targetId = targetId;
        this.content = content;
        this.status = status;
        this.timeStamp = timeStamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getPosterId() {
        return posterId;
    }

    public void setPosterId(long posterId) {
        this.posterId = posterId;
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}

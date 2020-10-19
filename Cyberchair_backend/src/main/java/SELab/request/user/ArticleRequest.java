package SELab.request.user;

import SELab.domain.Author;
import javafx.util.Pair;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public class ArticleRequest {
    private String meetingName;
    private String username;
    private String essayTitle;
    private String essayAbstract;
    private String submitDate;
    private Set<String> topic;
    private Set<Pair<Author, Integer>> authors;
    private MultipartFile file;

    public  ArticleRequest(){}

    public ArticleRequest(String meetingName, String username, String essayTitle, String essayAbstract,
                          String submitDate, MultipartFile file, Set<String> topic, Set<Pair<Author, Integer>> authors) {
        this.meetingName = meetingName;
        this.username = username;
        this.essayTitle = essayTitle;
        this.essayAbstract = essayAbstract;
        this.submitDate = submitDate;
        this.file = file;
        this.topic = topic;
        this.authors = authors;
    }

    public ArticleRequest(ArticleRequest articleRequest) {
        this.meetingName = articleRequest.getMeetingName();
        this.username = articleRequest.getUsername();
        this.essayTitle = articleRequest.getEssayTitle();
        this.essayAbstract = articleRequest.getEssayAbstract();
        this.submitDate = articleRequest.getSubmitDate();
        this.file = articleRequest.getFile();
        this.topic = articleRequest.getTopics();
        this.authors = articleRequest.getAuthors();
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEssayTitle() {
        return essayTitle;
    }

    public void setEssayTitle(String essayTitle) {
        this.essayTitle = essayTitle;
    }

    public String getEssayAbstract() {
        return essayAbstract;
    }

    public void setEssayAbstract(String essayAbstract) {
        this.essayAbstract = essayAbstract;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate;
    }

    public Set<String> getTopics() {
        return topic;
    }

    public void setTopics(Set<String> topic) {
        this.topic = topic;
    }


    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public Set<Pair<Author, Integer>> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Pair<Author, Integer>> authors) {
        this.authors = authors;
    }
}

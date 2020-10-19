package SELab.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String chairName;
    private String meetingName;
    private String acronym;
    private String region;
    private String city;
    private String venue;
    @ElementCollection
    private Set<String> topic;
    private String organizer;
    private String webPage;
    private String submissionDeadlineDate;
    private String notificationOfAcceptanceDate;
    private String conferenceDate;
    private String status;//许可

    public Meeting(){}

    public Meeting(String chairName, String meetingName, String acronym, String region, String city, String venue, Set<String> topic, String organizer, String webPage, String submissionDeadlineDate, String notificationOfAcceptanceDate, String conferenceDate, String approval) {
        this.chairName = chairName;
        this.meetingName = meetingName;
        this.acronym = acronym;
        this.region = region;
        this.city = city;
        this.venue = venue;
        this.topic = topic;
        this.organizer = organizer;
        this.webPage = webPage;
        this.submissionDeadlineDate = submissionDeadlineDate;
        this.notificationOfAcceptanceDate = notificationOfAcceptanceDate;
        this.conferenceDate = conferenceDate;
        this.status = approval;
    }

    public Meeting(Meeting meeting) {
        this.chairName = meeting.getChairName();
        this.meetingName = meeting.getMeetingName();
        this.acronym = meeting.getAcronym();
        this.region = meeting.getRegion();
        this.city = meeting.getCity();
        this.venue = meeting.getVenue();
        this.topic = meeting.getTopic();
        this.organizer = meeting.getOrganizer();
        this.webPage = meeting.getWebPage();
        this.submissionDeadlineDate = meeting.getSubmissionDeadlineDate();
        this.notificationOfAcceptanceDate = meeting.getNotificationOfAcceptanceDate();
        this.conferenceDate = meeting.getConferenceDate();
        this.status = meeting.getStatus();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChairName() {
        return chairName;
    }

    public void setChairName(String chairName) {
        this.chairName = chairName;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Set<String> getTopic() {
        return topic;
    }

    public void setTopic(Set<String> topic) {
        this.topic = topic;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getWebPage() {
        return webPage;
    }

    public void setWebPage(String webPage) {
        this.webPage = webPage;
    }

    public String getSubmissionDeadlineDate() {
        return submissionDeadlineDate;
    }

    public void setSubmissionDeadlineDate(String submissionDeadlineDate) {
        this.submissionDeadlineDate = submissionDeadlineDate;
    }

    public String getNotificationOfAcceptanceDate() {
        return notificationOfAcceptanceDate;
    }

    public void setNotificationOfAcceptanceDate(String notificationOfAcceptanceDate) {
        this.notificationOfAcceptanceDate = notificationOfAcceptanceDate;
    }

    public String getConferenceDate() {
        return conferenceDate;
    }

    public void setConferenceDate(String conferenceDate) {
        this.conferenceDate = conferenceDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String approval) {
        this.status = approval;
    }
}

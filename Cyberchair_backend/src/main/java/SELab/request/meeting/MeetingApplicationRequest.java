package SELab.request.meeting;

import java.util.Set;

public class MeetingApplicationRequest {
    private String chairName;
    private String meetingName;
    private String acronym;
    private String region;
    private String city;
    private String venue;
    private Set<String> topic;
    private String organizer;
    private String webPage;
    private String submissionDeadlineDate;
    private String notificationOfAcceptanceDate;
    private String conferenceDate;

    public MeetingApplicationRequest() {}

    public MeetingApplicationRequest(String chairName, String meetingName, String acronym, String region, String city, String venue, Set<String> topic, String organizer, String webPage, String submissionDeadlineDate, String notificationOfAcceptanceDate, String conferenceDate) {
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
    }

    public MeetingApplicationRequest(MeetingApplicationRequest meetingApplicationRequest) {
        this.chairName = meetingApplicationRequest.getChairName();
        this.meetingName = meetingApplicationRequest.getMeetingName();
        this.acronym = meetingApplicationRequest.getAcronym();
        this.region = meetingApplicationRequest.getRegion();
        this.city = meetingApplicationRequest.getCity();
        this.venue = meetingApplicationRequest.getVenue();
        this.topic = meetingApplicationRequest.getTopic();
        this.organizer = meetingApplicationRequest.getOrganizer();
        this.webPage = meetingApplicationRequest.getWebPage();
        this.submissionDeadlineDate = meetingApplicationRequest.getSubmissionDeadlineDate();
        this.notificationOfAcceptanceDate = meetingApplicationRequest.getNotificationOfAcceptanceDate();
        this.conferenceDate = meetingApplicationRequest.getConferenceDate();
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
}

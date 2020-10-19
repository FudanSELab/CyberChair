package SELab.request.meeting;

public class FinalPublishRequest {
    private String meetingName;

    public FinalPublishRequest(){}

    public FinalPublishRequest(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }
}

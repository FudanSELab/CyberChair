package SELab.request.meeting;

public class ResultPublishRequest {
    private String meetingName;

    public ResultPublishRequest(){}

    public ResultPublishRequest(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }
}

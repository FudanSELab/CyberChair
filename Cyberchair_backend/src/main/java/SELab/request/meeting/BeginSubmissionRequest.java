package SELab.request.meeting;

public class BeginSubmissionRequest {
    private String meetingName;

    public BeginSubmissionRequest(){}

    public BeginSubmissionRequest(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }
}

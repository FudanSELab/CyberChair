package SELab.request.meeting;

public class BeginReviewRequest {
    private String meetingName;
    private String assignStrategy;

    public BeginReviewRequest(){}

    public BeginReviewRequest(String meetingName, String assignStrategy) {
        this.meetingName = meetingName;
        this.assignStrategy = assignStrategy;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getAssignStrategy() {
        return assignStrategy;
    }

    public void setAssignStrategy(String assignStrategy) {
        this.assignStrategy = assignStrategy;
    }
}

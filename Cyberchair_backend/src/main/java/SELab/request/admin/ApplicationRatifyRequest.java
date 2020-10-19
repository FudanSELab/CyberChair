package SELab.request.admin;

public class ApplicationRatifyRequest {
    private String meetingName;
    private String approvalStatus;//ApplyPassed/ApplyFailed

    public ApplicationRatifyRequest(){}

    public ApplicationRatifyRequest(String meetingName, String approvalStatus) {
        this.meetingName = meetingName;
        this.approvalStatus = approvalStatus;
    }

    public ApplicationRatifyRequest(ApplicationRatifyRequest applicationRatifyRequest) {
        this.meetingName = applicationRatifyRequest.getMeetingName();
        this.approvalStatus = applicationRatifyRequest.getApprovalStatus();
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
}

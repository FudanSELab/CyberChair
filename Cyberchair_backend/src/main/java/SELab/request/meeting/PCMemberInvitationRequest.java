package SELab.request.meeting;

import java.util.Set;

public class PCMemberInvitationRequest {
    private String meetingName;
    private String pcMemberName;

    public PCMemberInvitationRequest(){}

    public PCMemberInvitationRequest(String meetingName, String pcMemberName) {
        this.meetingName = meetingName;
        this.pcMemberName = pcMemberName;
    }

    public PCMemberInvitationRequest(PCMemberInvitationRequest pcMemberInvitationRequest) {
        this.meetingName = pcMemberInvitationRequest.getMeetingName();
        this.pcMemberName = pcMemberInvitationRequest.getPcMemberName();
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getPcMemberName() {
        return pcMemberName;
    }

    public void setPcMemberName(String pcMemberName) {
        this.pcMemberName = pcMemberName;
    }
}

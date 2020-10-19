package SELab.request.user;

import java.util.Set;

public class InvitationRepoRequest {
    private String username;
    private String meetingName;
    private String response;
    private Set<String> topic;

    public InvitationRepoRequest(){}

    public InvitationRepoRequest(String username, String meetingName, String response, Set<String>topic){
        this.username = username;
        this.meetingName = meetingName;
        this.response = response;
        this.topic = topic;
    }

    public InvitationRepoRequest(InvitationRepoRequest invitationRepoRequest){
        this.username = invitationRepoRequest.getUsername();
        this.meetingName = invitationRepoRequest.getMeetingName();
        this.response = invitationRepoRequest.getResponse();
        this.topic = invitationRepoRequest.getTopics();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Set<String> getTopics() {
        return topic;
    }

    public void setTopics(Set<String> topic) {
        this.topic = topic;
    }
}

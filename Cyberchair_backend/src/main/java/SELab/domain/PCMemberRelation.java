package SELab.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
public class PCMemberRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long pcmemberId;
    private Long meetingId;
    private String status;
    @ElementCollection
    private Set<String> topic;

    public PCMemberRelation(){}

    public PCMemberRelation(Long pcmemberId, Long meetingId, String status, Set<String> topic) {
        this.pcmemberId = pcmemberId;
        this.meetingId = meetingId;
        this.status = status;
        this.topic = topic;
    }

    public PCMemberRelation(PCMemberRelation pcMemberRelation) {
        this.pcmemberId = pcMemberRelation.getPcmemberId();
        this.meetingId = pcMemberRelation.getMeetingId();
        this.status = pcMemberRelation.getStatus();
        this.topic = pcMemberRelation.getTopic();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPcmemberId() {
        return pcmemberId;
    }

    public void setPcmemberId(Long pcmemberId) {
        this.pcmemberId = pcmemberId;
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<String> getTopic() {
        return topic;
    }

    public void setTopic(Set<String> topic) {
        this.topic = topic;
    }
}

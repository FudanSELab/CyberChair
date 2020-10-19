package SELab.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ChairRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private long chairId;
    private long meetingId;
    private String status;

    public ChairRelation(){}

    public ChairRelation(long chairId, long meetingId, String status) {
        this.chairId = chairId;
        this.meetingId = meetingId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getChairId() {
        return chairId;
    }

    public void setChairId(long chairId) {
        this.chairId = chairId;
    }

    public long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(long meetingId) {
        this.meetingId = meetingId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

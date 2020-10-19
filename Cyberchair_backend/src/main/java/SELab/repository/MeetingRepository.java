package SELab.repository;

import SELab.domain.Meeting;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends CrudRepository<Meeting, Long> {
    Meeting findById(long id);
    Meeting findByMeetingName(String meetingName);
    List<Meeting> findByStatus(String status);
    List<Meeting> findByStatusNot(String status);
    List<Meeting> findByChairName(String chairName);
    List<Meeting> findByStatusAndChairNameNot(String status, String chairName);
}
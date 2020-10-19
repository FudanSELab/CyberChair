package SELab.repository;

import SELab.domain.PCMemberRelation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PCMemberRelationRepository extends CrudRepository<PCMemberRelation, Long> {
    PCMemberRelation findById(long id);
    List<PCMemberRelation> findByMeetingId(long meetingId);
    List<PCMemberRelation> findByPcmemberIdAndStatus(long pcmemberId, String status);
    List<PCMemberRelation> findByPcmemberIdAndMeetingId(long pcmemberId, long meetingId);
    List<PCMemberRelation> findByMeetingIdAndStatus(long meetingId,String status);
    List<PCMemberRelation> findByPcmemberIdAndStatusNot(long pcmemberId, String status);
}

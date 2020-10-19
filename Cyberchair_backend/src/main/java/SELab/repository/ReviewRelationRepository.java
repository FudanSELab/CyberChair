package SELab.repository;

import SELab.domain.ReviewRelation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ReviewRelationRepository extends CrudRepository<ReviewRelation, Long> {
    ReviewRelation findById(long id);

    Set<ReviewRelation> findReviewRelationsByArticleId(Long articleId);

    List<ReviewRelation> findByReviewerIdAndMeetingId(Long reviewerId, Long meetingId);

    ReviewRelation findByReviewerIdAndArticleId(Long reviewerId, Long articleId);
    List<ReviewRelation> findByIdNot(long id);
    List<ReviewRelation> findByReviewStatus(String reviewStatus);
    List<ReviewRelation> findByArticleId(long articleId);
    List<ReviewRelation> findByReviewStatusAndMeetingId(String reviewStatus,long meetingId);
    List<ReviewRelation> findByMeetingId(long meetingId);

}

package SELab.repository;

import SELab.domain.ChairRelation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChairRelationRepository extends CrudRepository<ChairRelation, Long> {
    ChairRelation findById(long id);
}

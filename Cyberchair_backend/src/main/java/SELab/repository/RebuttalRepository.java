package SELab.repository;

import SELab.domain.Rebuttal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RebuttalRepository extends CrudRepository<Rebuttal, Long> {
    Rebuttal findById(long id);
    List<Rebuttal> findByArticleId(long articleId);
    List<Rebuttal> findByIdNot(long id);
}

package SELab.repository;

import SELab.domain.PostMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<PostMessage, Long> {
    PostMessage findById(long id);
    List<PostMessage> findByArticleIdAndStatus(long articleId, String status);
}
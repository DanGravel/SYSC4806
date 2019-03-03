package app;

import app.models.Article;
import app.models.User;
import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    Article findBySubmitter(User submitter);
}

package app;

import app.models.Article;
import app.models.User;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    List<Article> findByUsers(User users);
    List<Article> findByUsersOrderByDateAsc(User users);
}

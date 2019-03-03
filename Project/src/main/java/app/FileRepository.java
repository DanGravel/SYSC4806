package app;

import app.models.File;
import app.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FileRepository extends CrudRepository<File, Long> {
    List<File> findByUser(User user);
}

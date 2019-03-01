package app;

import app.models.File;
import app.models.User;
import org.springframework.data.repository.CrudRepository;

public interface FileRepository extends CrudRepository<File, Long> {
    File findByUser(User user);

}

package app;

import app.models.Role;
import app.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    User findById(long id);

    User findByUsername(String username);

    List<User> findByRole(Role role);
}

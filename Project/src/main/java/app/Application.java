package app;

import app.models.Article;
import app.models.Role;
import app.models.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    //Temporary test data
    @Bean
    public CommandLineRunner demo(ArticleRepository articleRepository, RoleRepository roleRepository, UserRepository userRepository, InMemoryUserDetailsManager inMemoryUserDetailsManage) {
        return (args) -> {
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            // save a couple of customers
            Article article = new Article();
            User user1 = new User("test1", encoder.encode("password"), Role.submitter);
            User user2 = new User("test2", encoder.encode("password"), Role.editor);
            User user3 = new User("test3", encoder.encode("password"), Role.reviewer);

            article.setFileName("FileTest1");
            List<User> users = new ArrayList<User>();
            users.add(user1);
            users.add(user3);
            article.setAuthorizedUsers(users);
            user1.addAuthorizedArticle(article);

            roleRepository.save(user1.getRole());
            roleRepository.save(user2.getRole());
            roleRepository.save(user3.getRole());

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);

            inMemoryUserDetailsManage.createUser(org.springframework.security.core.userdetails.User.withUsername(user1.getUsername()).password(user1.getPassword()).roles(user1.getRole().toString()).build());
            inMemoryUserDetailsManage.createUser(org.springframework.security.core.userdetails.User.withUsername(user2.getUsername()).password(user2.getPassword()).roles(user2.getRole().toString()).build());
            inMemoryUserDetailsManage.createUser(org.springframework.security.core.userdetails.User.withUsername(user3.getUsername()).password(user3.getPassword()).roles(user3.getRole().toString()).build());

            articleRepository.save(article);
        };
    }
}
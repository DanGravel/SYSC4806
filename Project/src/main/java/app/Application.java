package app;

import app.models.ArticleStatus;
import app.models.File;
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

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    //Temporary test data
    @Bean
    public CommandLineRunner demo(FileRepository fileRepository, UserRepository userRepository, InMemoryUserDetailsManager inMemoryUserDetailsManage) {
        return (args) -> {
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            // save a couple of customers
            File file = new File();
            User user1 = new User("test1", encoder.encode("password"), Role.SUBMITTER.toString());
            User user2 = new User("test2", encoder.encode("password"), Role.EDITOR.toString());

            file.setFileName("Test1");
            file.setUser(user1);
            file.setStatus(ArticleStatus.SUBMITTED.toString());
            user1.addFiles(file);

            userRepository.save(user1);
            userRepository.save(user2);
            inMemoryUserDetailsManage.createUser(org.springframework.security.core.userdetails.User.withUsername(user1.getUsername()).password(user1.getPassword()).roles(user1.getRole()).build());
            inMemoryUserDetailsManage.createUser(org.springframework.security.core.userdetails.User.withUsername(user2.getUsername()).password(user2.getPassword()).roles(user2.getRole()).build());

            fileRepository.save(file);

        };
    }
}
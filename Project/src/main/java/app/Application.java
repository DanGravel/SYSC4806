package app;

import app.setup.TestDBSetup;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    //Temporary test data
    @Bean
    public CommandLineRunner demo(ArticleRepository articleRepository, UserRepository userRepository, InMemoryUserDetailsManager inMemoryUserDetailsManage) {
        return (args) -> {
            TestDBSetup.setupTestDB(userRepository, articleRepository, inMemoryUserDetailsManage);
        };
    }
}
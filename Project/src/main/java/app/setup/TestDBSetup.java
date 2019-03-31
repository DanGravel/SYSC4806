package app.setup;

import app.ArticleRepository;
import app.UserRepository;
import app.models.Article;
import app.models.Role;
import app.models.User;
import java.util.Date;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

public class TestDBSetup {

    private static void createUser(UserRepository userRepository, String name, Role role, InMemoryUserDetailsManager inMemoryUserDetailsManage) {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        User user = new User(name, encoder.encode("password"), role);
        userRepository.save(user);
        inMemoryUserDetailsManage.createUser(org.springframework.security.core.userdetails.User.withUsername(user.getUsername()).password(user.getPassword()).roles(new String[]{user.getRole().toString()}).build());
    }

    private static void createArticle(ArticleRepository articleRepository, User user, User reviewer, int uniqueIdent) {
        Article article = new Article();
        article.setFileName("FileTest" + uniqueIdent + ".txt");
        article.setData("TEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEST".getBytes());
        article.setFileType("text/plain");
        article.setDate(new Date());
        article.addAuthorizedUser(user);
        if (reviewer != null) {
            article.addReviewer(Role.EDITOR, reviewer);
            article.setReviewFileType("text/plain");
            article.setReview("TEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEST".getBytes());
        }

        articleRepository.save(article);
    }

    public static void setupTestDB(UserRepository userRepository, ArticleRepository articleRepository, InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        createUser(userRepository, "test1", Role.SUBMITTER, inMemoryUserDetailsManager);
        createUser(userRepository, "test7", Role.SUBMITTER, inMemoryUserDetailsManager);
        createUser(userRepository, "test2", Role.EDITOR, inMemoryUserDetailsManager);
        createUser(userRepository, "test6", Role.EDITOR, inMemoryUserDetailsManager);
        createUser(userRepository, "test3", Role.REVIEWER, inMemoryUserDetailsManager);
        createUser(userRepository, "test4", Role.REVIEWER, inMemoryUserDetailsManager);
        createUser(userRepository, "test5", Role.REVIEWER, inMemoryUserDetailsManager);
        createArticle(articleRepository, userRepository.findByUsername("test1"), userRepository.findByUsername("test3"), 100);
        for(int i = 0; i < 20; ++i) {
            if (i < 10) {
                createArticle(articleRepository, userRepository.findByUsername("test1"), userRepository.findByUsername("test3"), i);
            } else {
                createArticle(articleRepository, userRepository.findByUsername("test7"), null, i);
            }
        }

    }
}

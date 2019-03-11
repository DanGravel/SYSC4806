package app;

import app.models.Article;
import app.models.Role;
import app.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class EditorController extends app.Controller {

    /***
     * Adds all the reviewers to the model for the dropdown menu in editor.html
     * @return List<User> of all users with the Role.REVIEWER role
     */
    @ModelAttribute("reviewers")
    public List<User> populateReviewers() {
        return userRepository.findByRole(Role.REVIEWER);
    }

    /***
     * Lists files users are authorized for and shows actions based on role
     * @param model The model to return the files
     * @return The editor.html view
     */
    @GetMapping("/editor")
    public String getDashboard(Model model) {
        User user = getUser();
        Iterable<Article> articles;
        if (user.getRole() == Role.EDITOR) {
            articles = articleRepository.findAll();
        }
        else {
            articles = articleRepository.findByUsers(getUser());
        }

        model.addAttribute("articles", articles);
        model.addAttribute("role", getUser().getRole());
        return "editor";
    }

    /***
     * Adds a reviewer to the article given by articleId and userId
     * @param articleId The article in the table row clicked on
     * @param userId The user in the drop down menu
     * @return The editor.html view reloaded
     */
    @GetMapping("/addReviewer")
    public String addReviewer(@RequestParam("articleId") long articleId, @RequestParam("reviewer") long userId) {
        User callingUser = getUser();
        User reviewingUser = userRepository.findById(userId);
        // throws NoSuchElementException due to Optional<T> type
        Article article = articleRepository.findById(articleId).get();
        article.addReviewer(callingUser.getRole(), reviewingUser);
        articleRepository.save(article);
        return "redirect:/editor";
    }

    /***
     * Accepts or rejects the article given by articleId and isAccepted
     * @param articleId The article in the table row clicked on
     * @param isAccepted True if accepted was clicked false if reject was clicked
     * @return The editor.html view reloaded
     */
    @GetMapping("/acceptArticle")
    public String acceptArticle(@RequestParam("articleId") long articleId, @RequestParam("isAccepted") boolean isAccepted) {
        // throws NoSuchElementException due to Optional<T> type
        Article article = articleRepository.findById(articleId).get();
        article.setAccepted(isAccepted, getUser().getRole());
        articleRepository.save(article);
        return "redirect:/editor";
    }
}

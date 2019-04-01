package app;

import app.models.Article;
import app.models.ArticleState;
import app.models.Role;
import app.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class EditorController extends app.Controller {
    private static final int NO_USER = 0;
    private static final int REMOVE_USER = -1;
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
        articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        model.addAttribute("role", user.getRole());
        return "editor";
    }

    /***
     * Adds a reviewer to the article given by articleId and userId
     * @param articleId The article in the table row clicked on
     * @param userId The user in the drop down menu
     * @return The editor.html view reloaded
     */
    @GetMapping("/updateReviewer")
    public String updateReviewer(@RequestParam("articleId") long articleId, @RequestParam("reviewer") long userId) {
        Article article = articleRepository.findById(articleId).get();
        if(userId == REMOVE_USER) {
            article.removeReviewer();

        } else if(userId != NO_USER){
            User callingUser = getUser();
            User reviewingUser = userRepository.findById(userId);
            if(article.getState().equals(ArticleState.IN_REVIEW.toString()))
            {
                article.removeReviewer();
                article.setReview(null);
            }
            article.addReviewer(callingUser.getRole(), reviewingUser);
        }
        articleRepository.save(article);

        return "redirect:/editor";
    }

    /***
     * Updates a review due date for a specified article
     * @param articleId The article in the table row clicked on
     * @param date The date chosen by the datepicker
     * @return The editor.html view reloaded
     */
    @GetMapping("/updateDueDate")
    public String updateDueDate(@RequestParam("articleId") long articleId, @RequestParam("date") String date) {
        Article article = articleRepository.findById(articleId).get();
        try {
            Date date1= new SimpleDateFormat("EEEE, MMMM d, yyyy hh:mm a").parse(date);
            article.setReviewDueDate(date1);
            articleRepository.save(article);
            return "redirect:/editor";
        } catch (Exception exception) {
            return "redirect:/editor";
        }
    }

    /***
     * Accepts or rejects the article given by articleId and isAccepted
     * @param articleId The article in the table row clicked on
     * @param isAccepted True if accepted was clicked false if reject was clicked
     * @return The editor.html view reloaded
     */
    @GetMapping("/status")
    public String acceptArticle(@RequestParam("articleId") long articleId, @RequestParam("isAccepted") boolean isAccepted) {
        // throws NoSuchElementException due to Optional<T> type
        Article article = articleRepository.findById(articleId).get();
        try {
            // Only update if the article is not already accepted
            article.setAccepted(isAccepted, getUser().getRole());
            articleRepository.save(article);

            return "redirect:/editor";
        } catch (Exception exception) {
            return "redirect:/editor";
        }
    }


}

package app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import app.models.Article;

@Controller
public class ReviewerController extends app.Controller {

    /***
     * Gets a list of files for a reviewer in ascending order of the date
     * @param model The model to return the files
     * @return The reviewer view
     */
    @GetMapping("/reviewer")
    public String getReviewer(Model model) {
        model.addAttribute("articles", articleRepository.findByUsersOrderByDateAsc(super.getUser()));
        return "reviewer";
    }

    /***
     * Gets the information about a single article
     * @param model The model to return the files
     * @param id The id of the article
     * @return The review view
     */
    @GetMapping("/review/{id}")
    public String getReviewer(Model model, @PathVariable String id) {
        model.addAttribute("article", articleRepository.findById(Long.parseLong(id)).get());
        return "review";
    }

    /***
     * Sets the new review and redirects to /reviewer
     * @param model The model to return the files
     * @param articleId The id of the article
     * @param review The review
     * @return Redirect to the reviewer view
     */
    @PostMapping("/setReview")
    public String setReview(@RequestParam String review, @RequestParam String articleId) {
        articleRepository.findById(Long.parseLong(articleId)).ifPresent((Article article) -> {
            article.setReview(review);
            articleRepository.save(article);
        });
        return "redirect:/reviewer";
    }
}

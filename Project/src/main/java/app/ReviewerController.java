package app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import app.models.Article;
import java.util.List;
import java.util.Optional;

@Controller
public class ReviewerController extends app.Controller {


    @GetMapping("/reviewer")
    public String getReviewer(Model model) {
        model.addAttribute("articles", articleRepository.findByUsersOrderByDateAsc(super.getUser()));
        return "reviewer";
    }

    @GetMapping("/review")
    public String getReviewer(Model model, @RequestParam String id) {
        model.addAttribute("article", articleRepository.findById(Long.parseLong(id)).get());
        return "review";
    }

    @ModelAttribute("article")
    public Article getArticle() {
        return new Article();
    }

    @PostMapping("/setReview")
    public String setReview(@RequestParam String review, @RequestParam String articleId) {
        articleRepository.findById(Long.parseLong(articleId)).ifPresent((Article article) -> {
            article.setReview(review);
            articleRepository.save(article);
            System.out.println(review);
        });
        return "redirect:/reviewer";
    }
}

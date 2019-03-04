package app;

import app.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class FileUploadController extends app.Controller {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/upload")
    public String getFileUploader(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        model.addAttribute("role", super.getUser().getRole().getClass());
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadFile(Model model, @RequestParam("file") MultipartFile file) throws IOException {
        Article newArticle = new Article();
        newArticle.setFileName(file.getName());
        newArticle.setFileType(file.getContentType());
        newArticle.setData(file.getBytes());
        newArticle.setSubmitter(super.getUser());
        Article article = new Article();

        articleRepository.save(newArticle);

        return "redirect:/upload";
    }

    @GetMapping("/addReviewer")
    public String addReviewer(@RequestParam("articleId") String id) {
        User user = super.getUser();
        if (user.getRole() instanceof Reviewer) {
            Reviewer reviewer = (Reviewer) user.getRole();

            // throws NoSuchElementException
            Article article = articleRepository.findById(Long.parseLong(id)).get();
            reviewer.assignArticle(article);
            articleRepository.save(article);
        }
        return "redirect:/upload";
    }

    @GetMapping("/acceptArticle")
    public String acceptArticle(@RequestParam("articleId") String id) {
        User user = super.getUser();
        if (user.getRole() instanceof Editor) {
            Editor editor = (Editor) user.getRole();

            // throws NoSuchElementException
            Article article = articleRepository.findById(Long.parseLong(id)).get();
            editor.acceptArticle(article);
            articleRepository.save(article);
        }
        return "redirect:/upload";
    }

    @GetMapping("/rejectArticle")
    public String rejectArticle(@RequestParam("articleId") String id) {
        User user = super.getUser();
        if (user.getRole() instanceof Editor) {
            Editor editor = (Editor) user.getRole();

            // throws NoSuchElementException
            Article article = articleRepository.findById(Long.parseLong(id)).get();
            editor.rejectArticle(article);
            articleRepository.save(article);
        }
        return "redirect:/upload";
    }

}

package app;

import app.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class FileUploadController extends app.Controller {
    @Autowired
    private ArticleRepository articleRepository;

    /***
     * Gets a list of files for a user
     * @param model The model to return the files
     * @return The upload view
     */
    @GetMapping("/upload")
    public String getFileUploader(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        model.addAttribute("role", super.getUser().getRole().getClass());
        return "upload";
    }

    @ModelAttribute("reviewers")
    public List<User> populateReviewers() {
        return userRepository.findByRole_Name(Role.reviewer);
    }

    /***
     * Uploads a file from form data
     * @param file The Multipart file that got uploaded
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public String uploadFile(Model model, @RequestParam("file") MultipartFile file) throws IOException {
        Article newArticle = new Article();
        newArticle.setFileName(file.getName());
        newArticle.setFileType(file.getContentType());
        newArticle.setData(file.getBytes());
        newArticle.addAuthorizedUser(super.getUser());
        Article article = new Article();

        articleRepository.save(newArticle);

        return "redirect:/upload";
    }

    @GetMapping("/addReviewer")
    public String addReviewer(@RequestParam("articleId") String id, @RequestParam("reviewer") String userId) {
        User callingUser = super.getUser();
        User reviewingUser = userRepository.findById(Long.parseLong(userId));
        if (reviewingUser.getRole() instanceof Reviewer && callingUser.getRole() instanceof Editor) {
            Editor editor = (Editor) callingUser.getRole();

            // throws NoSuchElementException
            Article article = articleRepository.findById(Long.parseLong(id)).get();

            editor.assignArticle(article, reviewingUser);
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

    /***
     * Endpoint to download a file from the database
     * @param id The ID of the file that is to be downloaded
     * @return
     */
    @GetMapping("/getfile/{id}")
    public ResponseEntity<byte[]> getFileById(@PathVariable(value = "id")Long id){
        Article article = articleRepository.findById(id).get();
        if (!article.getAuthorizedUsers().contains(super.getUser())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(article.getFileType()));
        //sets the file name
        headers.setContentDispositionFormData(article.getFileName(), article.getFileName());
        //I admittedly have no idea what these cache control things are
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(article.getData(), headers, HttpStatus.OK);
        return response;
    }

}

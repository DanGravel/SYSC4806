package app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import app.models.Article;
import java.util.List;
import java.util.Optional;
import app.models.Role;
import app.models.User;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import app.Utils.StringUtils;
import app.exceptions.BadFileException;

@Controller
public class ReviewerController extends app.Controller {

    /***
     * Gets a list of files for a reviewer in ascending order of the date
     * @param model The model to return the files
     * @return The reviewer view
     */
    @GetMapping("/reviewer")
    public String getReviewer(Model model) {
        model.addAttribute("articles", articleRepository.findByUsersOrderByReviewDueDateAsc(super.getUser()));
        return "reviewer";
    }

    /***
     * Uploads a file to a review
     * @param file The Multipart file that got uploaded
     * @return
     * @throws IOException
     */
    @PostMapping("/review/{id}")
    public String uploadFile(@PathVariable(value="id") Long id, @RequestParam("file") MultipartFile file) throws IOException {
        if (file == null || StringUtils.isNullOrEmpty(file.getOriginalFilename())
                || StringUtils.isNullOrEmpty(file.getContentType()) || file.getBytes().length <= 0) {
            //This is how spring handles bad requests, throws exceptions.
            throw new BadFileException();
        }
        articleRepository.findById(id).ifPresent((Article article)  -> {
            try{
                article.setReview(file.getBytes());
                article.setReviewFileType(file.getContentType());
            } catch(IOException e) {}
            articleRepository.save(article);
        });
        return "redirect:/reviewer";
    }

    /***
     * Downloads a review
     * @param id The ID of the file whose review is to be downloaded
     * @return
     */
    @GetMapping("/review/{id}")
    public ResponseEntity<byte[]> getFileById(@PathVariable(value = "id")Long id){
        Article article = articleRepository.findById(id).get();
        User currentUser = super.getUser();
        // allow editors to download all files
        if (!article.getUsers().contains(currentUser) && currentUser.getRole() != Role.EDITOR) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(article.getReviewFileType()));
        //sets the file name
        headers.setContentDispositionFormData("review-" + id + ".txt", "review-" + id + ".txt");
        //I admittedly have no idea what these cache control things are
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(article.getReview(), headers, HttpStatus.OK);
        return response;
    }
}

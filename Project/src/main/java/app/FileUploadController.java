package app;

import app.Utils.StringUtils;
import app.exceptions.BadFileException;
import app.models.Article;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Controller
public class FileUploadController extends app.Controller {
    /***
     * Gets a list of files for a user
     * @param model The model to return the files
     * @return The upload view
     */
    @GetMapping("/upload")
    public String getFileUploader(Model model) {
        model.addAttribute("articles", articleRepository.findByUsers(getUser()));
        return "upload";
    }
    /***
     * Uploads a file from form data
     * @param file The Multipart file that got uploaded
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file == null || StringUtils.isNullOrEmpty(file.getOriginalFilename())
                                || StringUtils.isNullOrEmpty(file.getContentType()) || file.getBytes().length <= 0) {
            //This is how spring handles bad requests, throws exceptions.
            throw new BadFileException();
        }
        Article newArticle = new Article();
        newArticle.setFileName(file.getOriginalFilename());
        newArticle.setFileType(file.getContentType());
        newArticle.setData(file.getBytes());
        newArticle.addAuthorizedUser(super.getUser());
        newArticle.setDate(new Date());
        articleRepository.save(newArticle);
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
        if (!article.getUsers().contains(super.getUser())) {
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

    /**
     * Deletes an article from the article repository
     * @param id The id of the article to be deleted
     * @return
     */
    @DeleteMapping("/upload/{id}")
    public ResponseEntity<Void> deleteFileById(@PathVariable(value = "id") Long id) {
        if (id <= 0 || !articleRepository.existsById(id)) throw new BadFileException();
        articleRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

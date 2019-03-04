package app;

import app.models.ArticleStatus;
import app.models.File;
import app.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class FileUploadController extends app.Controller {
    @Autowired
    private FileRepository repository;

    /***
     * Gets a list of files for a user
     * @param model The model to return the files
     * @return The upload view
     */
    @GetMapping("/upload")
    public String getFileUploader(Model model) {
        User user = super.getUser();
        model.addAttribute("files", repository.findByUser(user));
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
        File newFile = new File();
        newFile.setFileName(file.getOriginalFilename());
        newFile.setFileType(file.getContentType());
        newFile.setData(file.getBytes());
        newFile.setStatus(ArticleStatus.SUBMITTED.toString());
        List users = new ArrayList<User>();
        users.add(super.getUser());
        newFile.setUser(users);
        repository.save(newFile);

        return "redirect:/upload";
    }

    /***
     * Endpoint to download a file from the database
     * @param id The ID of the file that is to be downloaded
     * @return
     */
    @GetMapping("/getfile/{id}")
    public ResponseEntity<byte[]> getFileById(@PathVariable(value = "id")Long id){
        Optional<File> fileOptional = repository.findById(id);
        File file = fileOptional.get();
        if (!file.getUser().contains(super.getUser())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(file.getFileType()));
        //sets the file name
        headers.setContentDispositionFormData(file.getFileName(), file.getFileName());
        //I admittedly have no idea what these cache control things are
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(file.getData(), headers, HttpStatus.OK);
        return response;
    }

}

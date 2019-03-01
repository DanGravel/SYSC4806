package app;

import app.models.File;
import app.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private FileRepository repository;

    @GetMapping("/upload")
    public String getFileUploader(Model model) {
        model.addAttribute("files", repository.findAll());
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        File newFile = new File();
        newFile.setFileName(file.getName());
        newFile.setFileType(file.getContentType());
        newFile.setData(file.getBytes());
        newFile.setUser(super.getUser());

        repository.save(newFile);

        return "redirect:/upload";
    }

}

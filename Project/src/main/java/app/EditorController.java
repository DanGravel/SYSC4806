package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EditorController extends app.Controller {
    @Autowired
    private FileRepository repository;

    /***
     * Gets a list of files for all users
     * @param model The model to return the files
     * @return The upload view
     */
    @GetMapping("/editor")
    public String getFileUploader(Model model) {
        model.addAttribute("files", repository.findAll());
        return "editor";
    }
}

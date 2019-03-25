package app;

import app.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for base website address
 */
@Controller
public class BaseController extends app.Controller {

    /**
     * Controller for base website address
     *
     * @return Redirect to users homepage
     * @throws Exception for invalid role
     */
    @GetMapping("/")
    public String defaultPage() throws Exception {
        User user = getUser();
        switch(user.getRole()) {
            case EDITOR:
                return "redirect:/editor";
            case REVIEWER:
                return "redirect:/reviewer";
            case SUBMITTER:
                return "redirect:/upload";
            default:
                throw new Exception("Invalid Role");
        }
    }
}
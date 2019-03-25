package app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for /login
 */
@Controller
public class LoginController {

    /**
     * Handles get request for /login
     * @return login view
     */
    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }
}
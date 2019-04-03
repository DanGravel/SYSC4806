package app;

import app.exceptions.UserExistsException;
import app.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

/**
 * Register page controller
 */
@Controller
public class RegisterController extends app.Controller {

    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Autowired
    public RegisterController(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
    }

    /**
     * Handles get request for register page
     *
     * @param model used to add user attribute
     * @return
     */
    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * Checks if user already exists
     *
     * @param username checks if username is already in use
     * @return json response with userExists as the only key
     */
    @RequestMapping("/register/user")
    @ResponseBody
    public Map<String, Boolean> userExists(@RequestParam(value = "username") String username) {
        User user = userRepository.findByUsername(username);
        return Collections.singletonMap("userExists", user != null);
    }

    /**
     * Creates new user
     *
     * @param user    user that should be created
     * @param request request being sent
     * @return redirects to user homepage
     */
    @PostMapping("/register")
    public String newUser(@ModelAttribute User user, HttpServletRequest request) throws ServletException {

        String unencryptedPass = user.getPassword();
        user.setPassword(encoder.encode(user.getPassword()));

        try {
            inMemoryUserDetailsManager.createUser(org.springframework.security.core.userdetails.User.withUsername(user.getUsername()).password(user.getPassword()).roles(user.getRole().toString()).build());
            userRepository.save(user);
        } catch (Exception e) {
            throw new UserExistsException();
        }
        request.login(user.getUsername(), unencryptedPass);

        return "redirect:/";
    }
}
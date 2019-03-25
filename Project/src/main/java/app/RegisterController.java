package app;

import app.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

@Controller
public class RegisterController extends app.Controller {

    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Autowired
    public RegisterController(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }


    @RequestMapping("/register/user")
    @ResponseBody
    public Map<String, Boolean> userExists(@RequestParam(value = "username")String username) {
        User user = userRepository.findByUsername(username);
        return Collections.singletonMap("userExists", user != null);
    }

    @PostMapping("/register")
    public String newUser(@ModelAttribute User user,  HttpServletRequest request){

        String unencryptedPass = user.getPassword();
        user.setPassword(encoder.encode(user.getPassword()));

        try {
            inMemoryUserDetailsManager.createUser(org.springframework.security.core.userdetails.User.withUsername(user.getUsername()).password(user.getPassword()).roles(user.getRole().toString()).build());
            userRepository.save(user);
        } catch (Exception e) {
            throw new UserExistsException();
        }

        try {
            request.login(user.getUsername(), unencryptedPass);
        } catch (ServletException e) {
            return "redirect:/register";
        }

        return "redirect:/";
    }

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="User already exists")
    public class UserExistsException extends RuntimeException {}

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

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }
}
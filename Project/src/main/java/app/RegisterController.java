package app;

import app.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/register")
    public String newUser(@ModelAttribute User user, @RequestParam("roleString")String roleString){
        user.setRole(roleString);
        user.setPassword(encoder.encode(user.getPassword()));
        roleRepository.save(user.getRole());
        userRepository.save(user);
        inMemoryUserDetailsManager.createUser(org.springframework.security.core.userdetails.User.withUsername(user.getUsername()).password(user.getPassword()).roles(roleString).build());
        return "redirect:/login";
    }

    @GetMapping("/")
    public String defaultPage(Model model) {
        User user = getUser();
        model.addAttribute("user", user);
        return "default";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }
}
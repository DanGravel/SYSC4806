package app;

import app.models.Role;
import app.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestApplication.class, initializers = ConfigFileApplicationContextInitializer.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegisterTest {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

     /**
     * Tests if register page renders correctly
     * @throws Exception
     */

     @Test
    public void registerGetTest() throws Exception {
        mockMvc.perform(get("/register")).andExpect(status().isOk());
    }

    /**
     * Tests registering a user
     * @throws Exception
     */
    @Test
    public void registerUserTest() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "test")
                        .param("password", "pass")
                        .param("role", "SUBMITTER")
                        .with(csrf())).andExpect(status().isFound());
    }


    /**
     * Tests registration with user that already has username
     * @throws Exception
     */
    @Test
    @WithMockUser(username="user", password = "password", roles={"SUBMITTER"})
    public void registerUsernameUsedTest() throws Exception {
        mockMvc.perform(post("/register")
                .param("username", "user")
                .param("password", "password")
                .param("role", "SUBMITTER")
                .with(csrf())).andExpect(status().is5xxServerError());
    }

    /**
     * Checks if a user exists with a user that is already registered
     * @throws Exception
     */
    @Test
    public void checkUserExistsTest() throws Exception {
        userRepository.save(new User("testUser", "password", Role.EDITOR));
        mockMvc.perform(get("/register/user")
                .param("username", "testUser"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("{\"userExists\":true}"));
    }

    /**
     * Checks if a user exists with a user that is not in the system
     * @throws Exception
     */
    @Test
    public void checkUserDoesntExistsTest() throws Exception {
        mockMvc.perform(get("/register/user")
                .param("username", "fakeUser"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("{\"userExists\":false}"));
    }
}
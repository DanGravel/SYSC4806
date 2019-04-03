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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestApplication.class, initializers = ConfigFileApplicationContextInitializer.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BaseTest {
    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username="userSubmitter", roles={"SUBMITTER"})
    public void submitterTest() throws Exception {
        userRepository.save(new User("userSubmitter", "password", Role.SUBMITTER));
        mockMvc.perform(get("/")).andExpect(redirectedUrl("/upload"));
    }

    @Test
    @WithMockUser(username="userEditor", roles={"REVIEWER"})
    public void reviewerTest() throws Exception {
        userRepository.save(new User("userEditor", "password", Role.EDITOR));

        mockMvc.perform(get("/")).andExpect(redirectedUrl("/editor"));
    }

    @Test
    @WithMockUser(username="userReviewer", roles={"EDITOR"})
    public void editorTest() throws Exception {
        userRepository.save(new User("userReviewer", "password", Role.REVIEWER));
        mockMvc.perform(get("/")).andExpect(redirectedUrl("/reviewer"));
    }
}
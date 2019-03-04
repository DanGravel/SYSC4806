package app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegisterTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Tests registering a user
     * @throws Exception
     */
    @Test
    public void register() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "test")
                        .param("password", "pass")
                        .param("role", "SUBMITTER")
                        .with(csrf())).andExpect(status().isFound());
    }

    /**
     * Tests login
     * @throws Exception
     */
    @Test
    @WithMockUser(username="user", password = "password", roles={"SUBMITTER"})
    public void login() throws Exception {

        mockMvc.perform(formLogin())
                .andExpect(status().isFound());
    }



}
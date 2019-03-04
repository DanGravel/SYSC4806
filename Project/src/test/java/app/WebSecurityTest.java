package app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WebSecurityTest {

    @Autowired
    private MockMvc mockMvc;


    /**
     * Tests the redirect from going to a protected path while logged out
     * @throws Exception
     */
    @Test
    public void loginProtection() throws Exception {
        this.mockMvc.perform(get("/upload").accept(MediaType.APPLICATION_JSON))
        .andExpect(redirectedUrl("http://localhost/login"));
    }

    /**
     * Tests path protection with different roles
     * @throws Exception
     */
    @Test
    public void protectedPath() throws Exception {
        this.mockMvc.perform(get("/upload").with(user("test").password("pass").roles("SUBMITTER"))).andExpect(status().isOk());
        this.mockMvc.perform(get("/upload").with(user("test").password("pass").roles("REVIEWER"))).andExpect(status().isForbidden());
    }

}
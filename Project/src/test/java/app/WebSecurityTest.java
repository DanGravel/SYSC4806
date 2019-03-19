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
     * Tests open endpoints that dont require authentication
     * @throws Exception
     */
    @Test
    public void openEndpoints() throws Exception {
        this.mockMvc.perform(get("/register")).andExpect(status().isOk());
        this.mockMvc.perform(get("/webjars")).andExpect(status().is4xxClientError());
        this.mockMvc.perform(get("/js")).andExpect(status().isOk());
        this.mockMvc.perform(get("/css")).andExpect(status().isOk());
    }

    /**
     * Tests path protection for article submitter
     * @throws Exception
     */
    @Test
    public void protectedPathSubmitter() throws Exception {
        this.mockMvc.perform(get("/upload").with(user("test1").password("pass").roles("SUBMITTER"))).andExpect(status().isOk());
        this.mockMvc.perform(get("/upload").with(user("test2").password("pass").roles("REVIEWER"))).andExpect(status().isForbidden());
        this.mockMvc.perform(get("/upload").with(user("test3").password("pass").roles("EDITOR"))).andExpect(status().isForbidden());
    }

    /**
     * Tests path protection for article reviewer
     * @throws Exception
     */
    @Test
    public void protectedPathReviewer() throws Exception {
        this.mockMvc.perform(get("/reviewer").with(user("test1").password("pass").roles("REVIEWER"))).andExpect(status().isOk());
        this.mockMvc.perform(get("/reviewer").with(user("test2").password("pass").roles("SUBMITTER"))).andExpect(status().isForbidden());
        this.mockMvc.perform(get("/reviewer").with(user("test3").password("pass").roles("EDITOR"))).andExpect(status().isForbidden());
    }

    /**
     * Tests path protection for editor
     * @throws Exception
     */
    @Test
    public void protectedPathEditor() throws Exception {
        this.mockMvc.perform(get("/editor").with(user("test1").password("pass").roles("EDITOR"))).andExpect(status().isOk());
        this.mockMvc.perform(get("/editor").with(user("test2").password("pass").roles("SUBMITTER"))).andExpect(status().isForbidden());
        this.mockMvc.perform(get("/editor").with(user("test3").password("pass").roles("REVIEWER"))).andExpect(status().isForbidden());
    }

}
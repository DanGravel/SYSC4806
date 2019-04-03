package app;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestApplication.class, initializers = ConfigFileApplicationContextInitializer.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EditorTest {
    @Autowired
    private MockMvc mock;

    @Test
    @WithMockUser(username = "test2", password = "password", roles = {"EDITOR"})
    public void testGetDashboard() throws Exception {
        mock.perform(get("/editor"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("articles", "role", "reviewers"));
    }

    @Test
    @WithMockUser(username = "test3", password = "password", roles = {"REVIEWER"})
    public void testGetDashboardNotEditor() throws Exception {
        mock.perform(get("/editor"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test2", password = "password", roles = {"EDITOR"})
    public void testUpdateDueDate() throws Exception {
        mock.perform(get("/updateDueDate")
                .param("articleId", "1")
                .param("date", "Saturday, April 6, 2019 1:52 PM"))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(username = "test2", password = "password", roles = {"EDITOR"})
    public void testUpdateDueDate_BadDate() throws Exception {
        mock.perform(get("/updateDueDate")
                .param("articleId", "1")
                .param("date", "asdfasdfasdf"))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(username = "test2", password = "password", roles = {"EDITOR"})
    public void testAcceptArticle() throws Exception {
        mock.perform(get("/status")
                .param("articleId", "1")
                .param("isAccepted", "true"))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(username = "test2", password = "password", roles = {"EDITOR"})
    public void testAcceptArticle_NoReview() throws Exception {
        mock.perform(get("/status")
                .param("articleId", "15")
                .param("isAccepted", "true"))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(username = "test2", password = "password", roles = {"EDITOR"})
    public void testUpdateReviewer_NoReviewer() throws Exception {
        mock.perform(get("/updateReviewer")
                .param("articleId", "2")
                .param("reviewer", "0"))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(username = "test2", password = "password", roles = {"EDITOR"})
    public void testUpdateReviewer_RemoveReviewer() throws Exception {
        mock.perform(get("/updateReviewer")
                .param("articleId", "2")
                .param("reviewer", "-1"))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(username = "test2", password = "password", roles = {"EDITOR"})
    public void testUpdateReviewer() throws Exception {
        mock.perform(get("/updateReviewer")
                .param("articleId", "2")
                .param("reviewer", "5"))
                .andExpect(status().isFound());
    }


}

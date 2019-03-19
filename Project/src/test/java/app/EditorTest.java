package app;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
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
                .andExpect(model().attributeExists("articles", "role", "reviewers"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "test3", password = "password", roles = {"Reviewer"})
    public void testGetDashboardNotEditor() throws Exception {
        mock.perform(get("/editor"))
                .andExpect(status().isForbidden())
                .andReturn();
    }
    @Test
    @WithMockUser(username = "test2", password = "password", roles = {"EDITOR"})
    public void testUpdateDueDate() throws Exception {
        mock.perform(get("/updateDueDate")
                .param("articleId", "1")
                .param("date", "Saturday, April 6, 2019 1:52 PM"))
            .andExpect(status().isFound())
            .andReturn();
    }

    @Test
    @WithMockUser(username = "test2", password = "password", roles = {"EDITOR"})
    public void testAcceptArticle() throws Exception {
        mock.perform(get("/acceptArticle")
                .param("articleId", "1")
                .param("isAccepted", "true"))
            .andExpect(status().isFound())
            .andReturn();
    }

    @Test
    @WithMockUser(username = "test2", password = "password", roles = {"EDITOR"})
    public void testUpdateReviewer() throws Exception {
        mock.perform(get("/updateReviewer")
                .param("articleId", "2")
                .param("reviewer", "5"))
            .andExpect(status().isFound())
            .andReturn();
    }



}

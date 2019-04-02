package app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.rmi.server.ExportException;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ReviewerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user", password = "password", roles = {"REVIEWER"})
    public void testGetReviewer() throws Exception {
        mockMvc.perform(get("/reviewer"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("articles"));
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = {"SUBMITTER"})
    public void testNotReviewer() throws Exception {
        mockMvc.perform(get("/reviewer"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = {"SUBMITTER"})
    public void testReviewArticleDoesntExist() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file","filename.txt","application/pdf", "wewewewewewewewew".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/review")
                .file(mockFile)
                .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test3", password = "password", roles= {"REVIEWER"})
    public void testReviewUpload() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file","review.txt","application/pdf", "wewewewewewewewew".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/review/2")
                .file(mockFile)
                .with(csrf()))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(username = "test3", password = "password", roles = {"REVIEWER"})
    public void testUploadReview_NoFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart("/review/1")
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test3", password = "password", roles = {"REVIEWER"})
    public void testUploadReview_NoFileName() throws Exception {
        //file without original filename
        MockMultipartFile mockFile = new MockMultipartFile("file","","application/pdf", "wewewewewewewewew".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/review/1")
                .file(mockFile)
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test3", password = "password", roles= {"REVIEWER"})
    public void testReviewDownload() throws Exception {
        MvcResult result =
                mockMvc.perform(get("/review/1"))
                        .andExpect(status().isOk())
                        .andReturn();

        String content = result.getResponse().getContentAsString();

        assert(content.equals("TEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEST"));
    }

    @Test
    @WithMockUser(username = "test5", password = "password", roles={"SUBMITTER"})
    public void testReviewDownloadWrongUser() throws Exception {
                mockMvc.perform(get("/review/1"))
                        .andExpect(status().isUnauthorized());
    }
}
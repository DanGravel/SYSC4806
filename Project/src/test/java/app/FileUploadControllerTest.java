package app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FileUploadControllerTest {
    @Autowired
    private MockMvc mock;

    @Autowired
    private FileUploadController uploadController;

    @Test
    @WithMockUser(username = "user", password = "password", roles = {"SUBMITTER"})
    public void testGetFileUpload() throws Exception {
        mock.perform(get("/upload"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("files"))
                .andReturn();

    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = {"REVIEWER"})
    public void testGetFileUpload_NonSubmitter() throws Exception {
        mock.perform(get("/upload"))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = {"SUBMITTER"})
    public void testUploadFile() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file","filename.txt","application/pdf", "wewewewewewewewew".getBytes());
        mock.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(mockFile))
                    .andExpect(status().isOk());
    }

}

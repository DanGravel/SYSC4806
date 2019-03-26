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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FileUploadControllerTest {
    @Autowired
    private MockMvc mock;

    @Test
    @WithMockUser(username = "test1", password = "password", roles = {"SUBMITTER"})
    public void testGetFileUpload() throws Exception {
        mock.perform(get("/upload"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("articles"));
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = {"REVIEWER"})
    public void testGetFileUpload_NonSubmitter() throws Exception {
        mock.perform(get("/upload"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = {"SUBMITTER"})
    public void testUploadFile() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file","filename.txt","application/pdf", "wewewewewewewewew".getBytes());

        mock.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(mockFile)
                        .with(csrf()))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(username = "test1", password = "password", roles = {"SUBMITTER"})
    public void testUploadFile_NoFileName() throws Exception {
        //file without original filename
        MockMultipartFile mockFile = new MockMultipartFile("file","","application/pdf", "wewewewewewewewew".getBytes());

        mock.perform(MockMvcRequestBuilders.multipart("/upload")
                .file(mockFile)
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test1", password = "password", roles= {"SUBMITTER"})
    public void testGetFileById() throws Exception {
        MvcResult result =
                mock.perform(get("/getfile/1"))
                    .andExpect(status().isOk())
                    .andReturn();

        String content = result.getResponse().getContentAsString();

        assert(content.equals("TEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEST"));
    }

    @Test
    @WithMockUser(username = "test1", password = "password", roles = {"SUBMITTER"})
    public void testDeleteFile() throws Exception {
        mock.perform(delete("/upload/1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test1", password = "password", roles = {"SUBMITTER"})
    public void testDeleteFile_FileNoExist() throws Exception {
        mock.perform(delete("/upload/75")
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }
}
package app;

import app.models.Article;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FileUploadControllerTest {
    @Autowired
    private MockMvc mock;

    @MockBean
    private FileUploadController uploadControllerMock;

    @Before
    public void setup() {
        Article a = new Article();
        a.setId(1);
        a.setData("lksdjhgpiuasdhjg9asorehng;klasjdghas;ldkf0".getBytes());
        a.setFileName("HEEHEE.txt");
        a.setFileType("text/plain");
        a.setDate(new Date());
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = {"SUBMITTER"})
    public void testGetFileUpload() throws Exception {
        mock.perform(get("/upload"))
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
                .andExpect(status().isOk());
    }

    @Test
    public void testGetFileById() throws Exception {
        mock.perform(get("/getfile/1"))
                .andExpect(status().isFound());
    }

    @Test
    public void deleteFile() throws Exception {
        mock.perform(delete("/upload/1")
                        .with(csrf()))
                .andExpect(status().isFound());
    }
}
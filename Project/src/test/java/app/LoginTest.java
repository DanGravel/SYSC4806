package app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {
    @Autowired
    private MockMvc mockMvc;

    /**
     * Tests login
     * @throws Exception
     */
    @Test
    @WithMockUser(username="user", password = "password", roles={"SUBMITTER"})
    public void login() throws Exception {

        mockMvc.perform(formLogin().user("user").password("password"))
                .andExpect(status().isFound());
    }

    /**
     * Tests login page get request
     * @throws Exception
     */
    @Test
    public void loginPage() throws Exception {
        mockMvc.perform(get("/login")).andExpect(status().isOk());
    }

    /**
     * Tests login with user that is not registered
     * @throws Exception
     */
    @Test
    public void loginFail() throws Exception {
        mockMvc.perform(formLogin().user("notreal").password("pass")).andExpect(status().is3xxRedirection());
    }
}
package edu.carroll.cs389application.web.controller;

import edu.carroll.cs389application.service.UserService;
import edu.carroll.cs389application.web.form.LoginForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(LoginController.class)
public class IndexControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService loginService;

    @Test
    public void indexTest() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));

    }

    @Test
    public void loginGet() throws Exception {
        mockMvc.perform(get("/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("loginForm"));

    }

    @Test
    public void loginFailed() throws Exception {
        mockMvc.perform(get("/loginfailed"))
                .andExpect(status().isOk())
                .andExpect(view().name("loginfailed"));

    }

    @Test
    public void loginSuccess() throws Exception {
        mockMvc.perform(get("/togar?username=test"))
                .andExpect(status().isOk())
                .andExpect(view().name("togar"))
                .andExpect(model().attribute("username", "test"));

    }

    @Test
    public void loginPost() throws Exception {
        LoginForm loginForm = new LoginForm();
        loginForm.setUsername("testUser");
        when(loginService.validateUsername(loginForm)).thenReturn(true);

        mockMvc.perform(post("/index")
                        .flashAttr("loginForm", loginForm))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/togar?Username=testUser"));
    }
}


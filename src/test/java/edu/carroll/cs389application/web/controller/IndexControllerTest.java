package edu.carroll.cs389application.web.controller;

import edu.carroll.cs389application.service.UserService;
import edu.carroll.cs389application.web.form.LoginForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for the {@link LoginController} class.
 */
@WebMvcTest(LoginController.class)
public class IndexControllerTest {

    /**
     * The {@link MockMvc} instance used to perform the tests.
     */
    @Autowired
    private MockMvc mockMvc;
    /**
     * A mock instance of the {@link UserService} interface.
     */
    @MockBean
    private UserService loginService;

    /**
     * Test for the index endpoint.
     *
     * @throws Exception if an error occurs while performing the test.
     */
    @Test
    public void indexTest() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));
    }

    /**
     * Test for the login GET endpoint.
     *
     * @throws Exception if an error occurs while performing the test.
     */
    @Test
    public void loginGet() throws Exception {
        mockMvc.perform(get("/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("loginForm"));
    }

    /**
     * Test for the login failed endpoint.
     *
     * @throws Exception if an error occurs while performing the test.
     */
    @Test
    public void loginFailed() throws Exception {
        mockMvc.perform(get("/loginfailed"))
                .andExpect(status().isOk())
                .andExpect(view().name("loginfailed"));
    }

    /**
     * Test for the login POST endpoint.
     *
     * @throws Exception if an error occurs while performing the test.
     */
    @Test
    public void loginPost() throws Exception {
        LoginForm loginForm = new LoginForm();
        loginForm.setUsername("testUser");
        when(loginService.validateUsername(loginForm)).thenReturn(true);

        // Create mock request and session objects
        MockHttpServletRequest request = mock(MockHttpServletRequest.class);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("request", request);

        MockHttpServletRequestBuilder requestBuilder = post("/index")
                .flashAttr("loginForm", loginForm)
                .session(session);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/togar"))
                .andExpect(request().sessionAttribute("username", "testUser"));
    }
}
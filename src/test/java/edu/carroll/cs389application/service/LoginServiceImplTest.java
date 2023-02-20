package edu.carroll.cs389application.service;

import edu.carroll.cs389application.web.form.LoginForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
public class LoginServiceImplTest {
    private static final String username = "testuser";

    @Autowired
    private LoginService loginService;

    @BeforeEach
    public void doBeforeTest() {
        assertNotNull("loginService must be injected", loginService);

        //Make sure that "testuser" is in database before our validateExistingUserSuccessfulTest
        final LoginForm testForm = new LoginForm(username);
        loginService.validateUsername(testForm);
    }

    @Test
    public void validatedExistingUserSuccessfulTest() {
        final LoginForm testForm = new LoginForm(username);
        assertTrue("validateExistingUserSuccessfulTest: should succeed using a valid username", loginService.validateUsername(testForm));
    }

    @Test
    public void validateNewUserSuccessfulTest() {
        final String newUserTest = "newUser";
        final LoginForm testForm = new LoginForm(newUserTest);
        assertTrue("ValidateCreationNewUserSuccessful: should succeed creating a new user", loginService.validateUsername(testForm));
    }
}


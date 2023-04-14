package edu.carroll.cs389application.service;

import edu.carroll.cs389application.web.form.LoginForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertTrue;


/**
 * This class contains unit tests for the LoginServiceImpl class.
 */
@SpringBootTest
public class LoginServiceImplTest {

    private static final String username = "testuser";

    @Autowired
    private UserService loginService;

    /**
     * Ensures that the loginService is injected.
     * Also creates a test user in the database.
     */
    @BeforeEach
    public void doBeforeTest() {
        assertNotNull("loginService must be injected", loginService);

        //Make sure that "testuser" is in database before our validateExistingUserSuccessfulTest
        final LoginForm testForm = new LoginForm(username);
        loginService.validateUsername(testForm);
    }

    /**
     * Tests that a valid existing user is validated successfully.
     */
    @Test
    public void validatedExistingUserSuccessfulTest() {
        final LoginForm testForm = new LoginForm(username);
        assertTrue("validateExistingUserSuccessfulTest: should succeed using a valid username", loginService.validateUsername(testForm));
    }

    /**
     * Tests that a new user can be successfully created.
     */
    @Test
    public void validateNewUserSuccessfulTest() {
        final String newUserTest = "testuser";
        final LoginForm testForm = new LoginForm(newUserTest);
        assertTrue("ValidateCreationNewUserSuccessful: should succeed creating a new user", loginService.validateUsername(testForm));
    }
}
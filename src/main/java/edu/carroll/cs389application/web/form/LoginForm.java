package edu.carroll.cs389application.web.form;

import edu.carroll.cs389application.service.SanizationService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * The LoginForm class represents a form used for user login functionality.
 * It contains validation rules for the username field.
 */
public class LoginForm {

    /**
     * The username field of the LoginForm.
     * It must not be null and must be between 8 and 30 characters in length.
     */
    @NotNull(message = "Username cannot be null")
    @Size(min = 8, message = "Username must be at least 9 characters long")
    @Size(max = 30, message = "Username must be smaller than 30 characters long")
    private String username;

    /**
     * Creates a new instance of the LoginForm class.
     */
    public LoginForm() {
    }

    /**
     * Creates a new instance of the LoginForm class with a given username.
     * The username is sanitized before being assigned to the field.
     *
     * @param username the username to be assigned to the username field
     */
    public LoginForm(String username) {
        this.username = SanizationService.sanitize(username);
    }

    /**
     * Returns the value of the username field.
     *
     * @return the value of the username field
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username field.
     *
     * @param username the value to be assigned to the username field
     */
    public void setUsername(String username) {
        this.username = SanizationService.sanitize(username);
    }
}

package edu.carroll.cs389application.web.form;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 *
 */
public class LoginForm {
    /**
     *
     */
    @NotNull
    @Size(min = 8, max = 30, message = "Username must be at least 9 characters long")
    private String username;

    /**
     *
     */
    public LoginForm() {
    }

    /**
     * @param username
     */
    public LoginForm(String username) {
        this.username = username;
    }

    /**
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }
}

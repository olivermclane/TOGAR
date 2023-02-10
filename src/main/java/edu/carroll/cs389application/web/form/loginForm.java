package edu.carroll.cs389application.web.form;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

/**
 *
 */
public class loginForm {
    /**
     *
     */
    @NotNull
    @Size(min = 8, max = 30, message = "Username must be at least 9 characters long")
    private String username;

    /**
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     */
    public loginForm() {
    }

    /**
     *
     * @param username
     */
    public loginForm(String username) {
        this.username = username;
    }
}

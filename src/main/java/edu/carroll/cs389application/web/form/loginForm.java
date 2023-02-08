package edu.carroll.cs389application.web.form;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

public class loginForm {
    @NotNull
    @Size(min = 9, message = "Username must be at least 9 characters long")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public loginForm() {
    }

    public loginForm(String username) {
        this.username = username;
    }
}

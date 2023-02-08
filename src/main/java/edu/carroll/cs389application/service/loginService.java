package edu.carroll.cs389application.service;

import edu.carroll.cs389application.web.form.loginForm;
public interface loginService {
    boolean validateUsername(loginForm form);
}

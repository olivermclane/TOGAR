package edu.carroll.cs389application.service;

import edu.carroll.cs389application.web.form.LoginForm;

/**
 *
 */
public interface LoginService {
    /**
     * @param form
     * @return
     */
    boolean validateUsername(LoginForm form);
}

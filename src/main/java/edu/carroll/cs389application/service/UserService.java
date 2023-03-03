package edu.carroll.cs389application.service;

import edu.carroll.cs389application.jpa.model.Login;
import edu.carroll.cs389application.web.form.LoginForm;

/**
 *
 */
public interface UserService {
    /**
     * @param form
     * @return
     */
    boolean validateUsername(LoginForm form);
    Login loginFromUsername(String userName);
}

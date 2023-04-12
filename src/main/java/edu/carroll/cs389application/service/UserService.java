/**
* Provides basic ammenties to work with the user and provide userinfo to the application.
**/
package edu.carroll.cs389application.service;

import edu.carroll.cs389application.jpa.model.Login;
import edu.carroll.cs389application.web.form.LoginForm;

/**
 * This interface provides methods to interact with the User Service.
 */
public interface UserService {

    /**
     * Validates the given username.
     *
     * @param form The login form containing the username.
     * @return True if the user exists, false otherwise.
     */
    boolean validateUsername(LoginForm form);

    /**
     * Retrieves the login information for the given username.
     *
     * @param userName The username to retrieve the login information for.
     * @return The Login object for the given username.
     */
    Login loginFromUsername(String userName);
}

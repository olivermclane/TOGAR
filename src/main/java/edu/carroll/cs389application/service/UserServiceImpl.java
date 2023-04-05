package edu.carroll.cs389application.service;

import edu.carroll.cs389application.jpa.model.Login;
import edu.carroll.cs389application.jpa.repo.LoginRepo;
import edu.carroll.cs389application.web.form.LoginForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of UserService interface for user login and registration.
 */
@Service
public class UserServiceImpl implements UserService {
    /**
     * This is a basic logging library that we use to keep track of logs for application
     */
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * Repository interface for user entities. Provides CRUD functionality and
     * additional methods for finding Login entities by username.
     */
    private final LoginRepo loginR;

    /**
     * Constructor that initializes LoginRepo instance.
     *
     * @param loginRe LoginRepo instance to be used for database access.
     */
    public UserServiceImpl(LoginRepo loginRe) {
        this.loginR = loginRe;
    }

    /**
     * Validates whether a user with the given username already exists in the database.
     * If no user with the given username is found, a new user is created.
     * If multiple users are found, an error is logged and false is returned.
     *
     * @param LoginForm Data from the login page including the username to be validated.
     * @return True if the user exists or was successfully created, false if multiple users were found.
     */
    @Override
    public boolean validateUsername(LoginForm LoginForm) {
        log.info("User '{}' has attempted login", LoginForm.getUsername());
        // This will grab out list of users
        List<Login> users = loginR.findByUsernameIgnoreCase(LoginForm.getUsername());

        //No users were found as such new user must be created.
        if (users.size() == 0) {
            Login newUser = new Login();
            newUser.setUsername(LoginForm.getUsername());
            loginR.save(newUser);
            log.info("Success: No user found: creating user {}", LoginForm.getUsername());
            return true;
        }
        //Multiple users found which shouldn't occur but must handle.
        if (users.size() > 1) {
            log.error("Error: found several {} users, someone broke DB", users.size());
            return false;
        }

        //Found existing username and successfully logged in
        log.info("Success: successful login, single user found {}", LoginForm.getUsername());
        return true;

    }

    /**
     * Finds a user with the given username.
     *
     * @param userName Username to search for.
     * @return The Login object corresponding to the given username.
     */
    @Override
    public Login loginFromUsername(String userName) {
        return loginR.findByUsernameIgnoreCase(userName).get(0);
    }
}
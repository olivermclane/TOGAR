package edu.carroll.cs389application.service;

import edu.carroll.cs389application.jpa.model.Login;
import edu.carroll.cs389application.jpa.repo.LoginRepo;
import edu.carroll.cs389application.web.form.LoginForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final LoginRepo loginR;

    /**
     * @param loginRe
     */
    public UserServiceImpl(LoginRepo loginRe) {
        this.loginR = loginRe;
    }

    /**
     * @param LoginForm the data from our login page such as the username
     * @return true if the user exist, false elsewise
     */
    @Override
    public boolean validateUsername(LoginForm LoginForm) {
        log.info("User '{} has attempted login", LoginForm.getUsername());
        // grab our list
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

        //Found existing username, no password needed in my app
        log.info("Success: successful login, single user found {}", LoginForm.getUsername());
        return true;

    }

    @Override
    public Login loginFromUsername(String userName) {
        return loginR.findByUsernameIgnoreCase(userName).get(0);
    }
}

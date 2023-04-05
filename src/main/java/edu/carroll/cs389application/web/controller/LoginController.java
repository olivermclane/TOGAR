package edu.carroll.cs389application.web.controller;

import edu.carroll.cs389application.service.UserService;
import edu.carroll.cs389application.web.form.LoginForm;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * This class is the controller for the login functionality of the application.
 * It handles GET and POST requests for the login page and authentication of users.
 */
@Controller
public class LoginController {
    /**
     * This is an instance of the UserService used to added users to our database and load user data.
     */
    private final UserService loginService;

    /**
     * Constructor for LoginController that injects the UserService dependency.
     *
     * @param loginService The UserService object to be used for authentication.
     */
    public LoginController(UserService loginService) {
        this.loginService = loginService;
    }

    /**
     * GET request handler for the application root path ("/").
     * Redirects to the login page.
     *
     * @return A String representing the name of the view to redirect to.
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/index";
    }

    /**
     * GET request handler for the login page ("/index").
     * Adds a new LoginForm object to the Model and returns the view name.
     *
     * @param model The Model object to be used for adding attributes.
     * @return A String representing the name of the view.
     */
    @GetMapping("/index")
    public String loginGet(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "index";
    }

    /**
     * GET request handler for the login failed page ("/loginfailed").
     *
     * @return A String representing the name of the view.
     */
    @GetMapping("/loginfailed")
    public String loginFailed() {
        return "loginfailed";
    }

    /**
     * POST request handler for the login page ("/index").
     * Authenticates the user and redirects them to their unique logged-in page.
     *
     * @param logForm The LoginForm object containing the user's credentials.
     * @param result  The BindingResult object containing any errors encountered during validation.
     * @param attrs   The RedirectAttributes object used for redirecting with flash attributes.
     * @param session The HttpSession object used for managing user sessions.
     * @return A String representing the name of the view to redirect to.
     */
    @PostMapping("/index")
    public String loginPost(@Valid @ModelAttribute LoginForm logForm, BindingResult result, RedirectAttributes attrs, HttpSession session) {
        if (result.hasErrors()) {
            return "index";
        }

        // Validate the user's username
        if (!loginService.validateUsername(logForm)) {
            result.addError(new ObjectError("globalError", "Username has issues with more than one instance"));
            return "index";
        }

        // Set the logged-in user's username as a session attribute
        session.setAttribute("username", logForm.getUsername());

        // Redirect to the user's unique logged-in page
        return "redirect:/togar";
    }
}


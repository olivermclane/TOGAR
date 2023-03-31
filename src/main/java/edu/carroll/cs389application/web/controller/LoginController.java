package edu.carroll.cs389application.web.controller;

import edu.carroll.cs389application.service.SanizationService;
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

import java.util.regex.Pattern;


/**
 *
 */
@Controller
public class LoginController {
    private final UserService loginService;

    /**
     * @param loginService
     */
    public LoginController(UserService loginService) {
        this.loginService = loginService;
    }

    /**
     * @return
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/index";
    }

    /**
     * @param model
     * @return
     */
    @GetMapping("/index")
    public String loginGet(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "index";
    }

    /**
     * @return
     */
    @GetMapping("/loginfailed")
    public String loginFailed() {
        return "loginfailed";
    }


    /**
     * @param logForm
     * @param result
     * @param attrs
     * @return
     */
    @PostMapping("/index")
public String loginPost(@Valid @ModelAttribute LoginForm logForm, BindingResult result, RedirectAttributes attrs, HttpSession session) {
    if (result.hasErrors()) {
        return "index";
    }

    //Here we don't need to worry about passwords the validateUser will
    // fail only if multiple instance of a name are found
    if (!loginService.validateUsername(logForm)) {
        result.addError(new ObjectError("globalError", "Username has issues with more than one instance"));
        return "index";
    }

    //should close session on browser close
    session.setMaxInactiveInterval(-1);

    session.setAttribute("username", logForm.getUsername());

    //this will handle redirecting to the users to there "unique" loggedin page.
    return "redirect:/togar";
}
}


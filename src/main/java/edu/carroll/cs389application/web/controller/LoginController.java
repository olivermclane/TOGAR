package edu.carroll.cs389application.web.controller;

import edu.carroll.cs389application.service.loginService;
import edu.carroll.cs389application.web.form.loginForm;
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
 *
 */
@Controller
public class LoginController {
    private final loginService loginService;

    /**
     *
     * @param loginService
     */
    public LoginController(loginService loginService)    {
        this.loginService = loginService;
    }

    /**
     *
     * @return
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/index";
    }

    /**
     *
     * @param model
     * @return
     */
    @GetMapping("/index")
    public String loginGet(Model model)     {
        model.addAttribute("loginForm", new loginForm());
        return "index";
    }

    /**
     *
     * @return
     */
    @GetMapping("/loginfailed")
    public String loginFailed()     {
       return "loginfailed";
    }

    /**
     *
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/togar")
    public String loginSuccess(String username, Model model)    {
        model.addAttribute("username", username);
        return "togar";
    }

    /**
     *
     * @param logForm
     * @param result
     * @param attrs
     * @return
     */
    @PostMapping("/index")
    public String loginPost(@Valid @ModelAttribute loginForm logForm, BindingResult result, RedirectAttributes attrs){
        if (result.hasErrors()){
            return "index";
        }
        //Here we don't need to worry about passwords the validateUser will
        // fail only if multiple instance of a name are found
        if(!loginService.validateUsername(logForm)){
            result.addError(new ObjectError("globalError", "Username has issues with more than one instance"));
            return "index";
        }

        //this will handle redirecting to the users to there "unique" loggedin page.
        attrs.addAttribute("Username", logForm.getUsername());
        return "redirect:/togar";
    }

}


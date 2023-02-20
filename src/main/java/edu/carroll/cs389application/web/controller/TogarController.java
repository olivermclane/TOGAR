package edu.carroll.cs389application.web.controller;

import edu.carroll.cs389application.web.form.TogarImageForm;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

public class TogarController {
    @PostMapping("/togar")
    public String handleFileUpload(@ModelAttribute("fileForm"), TogarImageForm fileForm){
        if()

    }
}
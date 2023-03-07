package edu.carroll.cs389application.web.controller;

import edu.carroll.cs389application.service.ImageServiceImpl;
import edu.carroll.cs389application.service.UserService;
import edu.carroll.cs389application.service.ImageService;
import edu.carroll.cs389application.web.form.ImageForm;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Controller
public class ImageController {
    private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);
    private final ImageService imageService;
    private final UserService userService;

    public ImageController(ImageService imageService, UserService userService) {
        this.imageService = imageService;
        this.userService = userService;
    }


    @PostMapping("/upload")
    public String handleFileUpload(@ModelAttribute("fileForm") ImageForm fileForm, @RequestParam("imageFile") MultipartFile file, BindingResult result, Model model, HttpSession session) throws IOException {
        String username = (String) session.getAttribute("username");

        if(result.hasErrors()){
            return "togar";
        }

        log.info("Started upload {}", username);

        //This may not be need after view logs.
        fileForm.setImageFile(file);
        imageService.saveImage(fileForm, userService.loginFromUsername(username));
        return "redirect:/togar?Username=" + username;
    }

    @GetMapping("/togar")
    public String imageGallery(Model model, @RequestParam("Username") String username) throws IOException {
        List<String> imageLocations = imageService.pullImages(userService.loginFromUsername(username));

        List<byte[]> imageDataList = new ArrayList<>();
        for (String imageLocation : imageLocations) {
            Path imagePath = Paths.get(imageLocation);
            try {
                byte[] imageData = Files.readAllBytes(imagePath);
                imageDataList.add(imageData);
                log.info("Image location: {}", imageLocation);
            } catch (IOException e) {
                log.error("There was a error converting to bytes of the image");
            }
        }

        model.addAttribute("images", imageDataList);

        return "togar";
    }
}
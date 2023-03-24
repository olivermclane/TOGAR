package edu.carroll.cs389application.web.controller;

import edu.carroll.cs389application.service.ImageService;
import edu.carroll.cs389application.service.ImageServiceImpl;
import edu.carroll.cs389application.service.UserService;
import edu.carroll.cs389application.web.form.ImageForm;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
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
        passImages(model, username);

        if (result.hasErrors()) {
            return "togar";
        }

        if (!imageService.validateFile(file).equals("validfile")) {
            ImageServiceImpl.ErrorCode fileError = imageService.validateFile(file);
            log.debug(fileError.toString());
            model.addAttribute("fileError", fileError);
            return "togar";
        }
        log.info("Started upload {}", username);

        fileForm.setImageFile(file);
        imageService.saveImage(fileForm, userService.loginFromUsername(username));
        return "redirect:/togar";
    }


    @GetMapping("/togar")
    public String imageGallery(Model model, HttpSession session) throws IOException {
        if (session == null) {
            return "redirect:/index";
        }
        if (session.getAttribute("username") == null) {
            return "redirect:/index";
        }

        String username = (String) session.getAttribute("username");
        passImages(model, username);

        return "togar";
    }

    private void passImages(Model model, String username) throws IOException {
        List<Pair<InputStream, String>> imageStreams = imageService.pullImages(userService.loginFromUsername(username));

        List<Pair<String, String>> images = new ArrayList<>();
        for (Pair<InputStream, String> pair : imageStreams) {
            String contentType = pair.getSecond();
            if (contentType.equalsIgnoreCase("image/jpeg") || contentType.equalsIgnoreCase("image/png")) {
                InputStream inputStream = pair.getFirst();
                byte[] byteArray = IOUtils.toByteArray(inputStream);
                String imageBase64 = Base64.getEncoder().encodeToString(byteArray);
                String imageSrc = "data:" + contentType + ";base64," + imageBase64;
                images.add(Pair.of(imageSrc, contentType));
                log.info("Image content type: {}", contentType);
            }
        }

        model.addAttribute("images", images);
    }


}

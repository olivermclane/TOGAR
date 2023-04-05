package edu.carroll.cs389application.web.controller;

import edu.carroll.cs389application.service.ImageServiceImpl.ErrorCode;
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

/**
 * The ImageController class handles all HTTP requests related to images, including uploading, retrieving, and displaying
 * images. It interacts with the ImageService and UserService to perform the necessary operations, such as saving and
 * retrieving images from the database.
 */
@Controller
public class ImageController {
    /**
     * This is a basic logging platform that we use throughout the application for logging errors,info, and warnings.
     */
    private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    /**
     * This instance of the ImageService will handle laoding images and saving images.
     */
    private final ImageService imageService;

    /**
     * This instance of the UserService will help navigate active users and help save images.
     */
    private final UserService userService;

    /**
     * Constructor for the ImageController class.
     *
     * @param imageService The ImageService used to perform operations related to images.
     * @param userService  The UserService used to perform operations related to users.
     */
    public ImageController(ImageService imageService, UserService userService) {
        this.imageService = imageService;
        this.userService = userService;
    }

    /**
     * Handles file upload requests.
     *
     * @param fileForm the form containing information about the image file
     * @param file     the image file itself
     * @param result   the result of the validation of the image file
     * @param model    the model used to store data to be displayed by the view
     * @param session  the user's session
     * @return the view to display
     * */
    @PostMapping("/upload")
    public String handleFileUpload(@ModelAttribute("fileForm") ImageForm fileForm, @RequestParam("imageFile") MultipartFile file, BindingResult result, Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        try {
            passImages(model, username);

            if (result.hasErrors()) {
                return "togar";
            }

            if (!imageService.validateFile(file).equals(ErrorCode.VALID_FILE)) {
                ErrorCode fileError = imageService.validateFile(file);
                log.debug(fileError.toString());
                model.addAttribute("fileError", fileError);
                return "togar";
            }
            log.info("Started upload {}", username);

            fileForm.setImageFile(file);
            imageService.saveImage(fileForm, userService.loginFromUsername(username));
            return "redirect:/togar";

        } catch (IOException e) {
            log.error("User caused a IOException review logs for {}", username);
            return "redirect:/togar";
        }

    }

    /**
     * Displays the user's image gallery.
     *
     * @param model   the model used to store data to be displayed by the view
     * @param session the user's session
     * @return the view to display
     */
    @GetMapping("/togar")
    public String imageGallery(Model model, HttpSession session) {
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

    /**
     * Displays the user's image gallery.
     *
     * @param session the user's session
     * @return the view to display
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/index";
    }

    /**
     * Helper method to pass the images to the view.
     * Retrieves the list of image streams for the specified user, converts each image to a base64-encoded string,
     * and adds it to a list of image pairs containing the image source and content type.
     * Finally, adds the list of image pairs to the model attribute "images".
     *
     * @param model    the model to add the image pairs to
     * @param username the username of the user to retrieve images for
     */
    private void passImages(Model model, String username) {
        try {
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
        } catch (IOException e) {
            log.error("User cause IOException preview logs for {}", username);
        }


    }


}

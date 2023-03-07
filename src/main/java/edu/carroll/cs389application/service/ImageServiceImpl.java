package edu.carroll.cs389application.service;

import edu.carroll.cs389application.jpa.model.Login;
import edu.carroll.cs389application.jpa.model.UserImage;
import edu.carroll.cs389application.jpa.repo.ImagesRepo;
import edu.carroll.cs389application.web.form.ImageForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
    private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);
    private final ImagesRepo imageRepo;


    /**
     *
     */
    public ImageServiceImpl(ImagesRepo imageR) {
        this.imageRepo = imageR;
    }

    /*
     *
     */
    @Override
    public void saveImage(ImageForm imageForm, Login user) throws IOException {
        MultipartFile imageFile = imageForm.getImageFile();
        String uploadDirectory = "./images/";

        //Seting up the properties for a UserImage object
        String imageName = imageFile.getOriginalFilename();
        String imageLocation = uploadDirectory + user.getId() + "/";
        String extension = StringUtils.getFilenameExtension(imageName);
        long imageSize = imageFile.getSize();

        //Still learning image processing libaries
        //one thought here might work needs testing
        BufferedImage bImage = ImageIO.read(imageFile.getInputStream());
        int imageHeight = bImage.getHeight();
        int imageWidth = bImage.getWidth();

        //creating userImage
        UserImage uImage = new UserImage(imageName, extension, imageLocation, imageSize, imageHeight, imageWidth, user);
        uImage.setImageFile(imageFile);

        //Saving UserImage object to DB and to the location on the local DB


        try{
            saveImageFile(uImage, imageFile);
            imageRepo.save(uImage);
            log.info("Success: Image meta data created in user_image");
        }catch(IOException e){
            //Create out if statements for different types of IOExceptions and log them
            if (e instanceof FileAlreadyExistsException) {
                log.error("A file of that name already exists.");
                throw new RuntimeException("A file of that name already exists.");
            }
            log.error("Problem saving file follow exception");
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void saveImageFile(UserImage userImage, MultipartFile file) throws IOException {
           String imageLocation = userImage.getImageLocation();
           Path root = Paths.get(imageLocation);
           Files.createDirectories(root);
           root = root.resolve(file.getOriginalFilename());
           Files.copy(file.getInputStream(), root);
           log.info("Image has been saved to directory image");

    }

    //there are two ways to go about this, we could either pass a list of locations for the images and have the controller go
    // through and loaded the images which could be bad practice or have this return a list of imageLocations;
    @Override
    public List<String> pullImages(Login user) throws IOException {
        List<UserImage> images = imageRepo.findByUser(user);
        log.info("User ID: {}",user.getId());
        log.info("Images list size: {}", images.size());
        List<String> imageLocations = new ArrayList<>();

        for (int i = 0; i < images.size(); i++) {
            imageLocations.add(images.get(i).getImageLocation() + images.get(i).getImageName());
            log.info("Images pulled: {} ", imageLocations.get(i));
        }

        return  imageLocations;
    }
}
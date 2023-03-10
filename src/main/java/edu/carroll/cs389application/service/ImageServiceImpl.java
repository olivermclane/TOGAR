package edu.carroll.cs389application.service;

import edu.carroll.cs389application.jpa.model.Login;
import edu.carroll.cs389application.jpa.model.UserImage;
import edu.carroll.cs389application.jpa.repo.ImagesRepo;
import edu.carroll.cs389application.web.form.ImageForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

        //Image processing libarary
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
            }
            log.error("Problem saving file follow exception");
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
    public List<Pair<InputStream, String>> pullImages(Login user) throws IOException {
        List<UserImage> images = imageRepo.findByUser(user);
        log.info("User ID: {}", user.getId());
        log.info("Images list size: {}", images.size());
        List<Pair<InputStream, String>> imageStreams = new ArrayList<>();

        for (UserImage image : images) {
            String imageLocation = image.getImageLocation() + image.getImageName();
            log.info("Image pulled: {} ", imageLocation);
            String extension = image.getImageName().substring(image.getImageName().lastIndexOf(".") + 1);
            if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("png")) {
                InputStream inputStream = new FileInputStream(imageLocation);
                String contentType = extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg") ? "image/jpeg" : "image/png";
                imageStreams.add(Pair.of(inputStream, contentType));
            }
        }

        return imageStreams;
    }

    @Override
    public String validateFile(MultipartFile file){
        boolean VirusBoolean = false;
        if (file == null) {
            return "Invalid File: File is null";
        }
        if (file.isEmpty()){
            return "Invalid File: Content of file is empty, or you didn't upload a file";
        }
        if (file.getSize() > 10 * 1024 * 1024){
            return "Invalid File Size: Validate image size is less than 10MB";
        }
        if(!file.getContentType().startsWith("image/")){
            return "Invalid File Type: Validate image types are PNG and JPEG";
        }
        return "validfile";
    }

}
package edu.carroll.cs389application.service;

import edu.carroll.cs389application.jpa.model.Login;
import edu.carroll.cs389application.jpa.model.UserImage;
import edu.carroll.cs389application.jpa.repo.ImagesRepo;
import edu.carroll.cs389application.web.form.ImageForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ImageServiceImplTest {
    private static final String username = "testuser";
    @Autowired
    private  ImageService imageService;
    @Autowired
    private ImagesRepo imagesRepo;

    private Login user;
    private UserImage image1, image2;

    @BeforeEach
    void setUp() {
        user = new Login(username);

        image1 = new UserImage("image1.jpg", "jpg", "./images/1/", 100L, 100, 100, user);
        image2 = new UserImage("image2.png", "png", "./images/1/", 200L, 200, 200, user);

        imagesRepo.save(image1);
        imagesRepo.save(image2);
    }

    @AfterEach
    void cleanUp() {
        imagesRepo.deleteAll();
    }

    @Test
    public void testDuplicateSaveImage() throws IOException {

    }

    @Test
    public void testInvalidFileExtSaveImage() throws IOException{

    }

    @Test
    public void testImageTooLargeSaveImage() throws IOException{

    }

    @Test
    public void testNullImage() throws IOException {

    }

    @Test
    public void testValidSaveImage() throws IOException {
        Login user = new Login(username);

        // Create a test file
        byte[] content = "test image content".getBytes();
        MockMultipartFile file = new MockMultipartFile("test.jpg", content);

        ImageForm imageForm = new ImageForm(file);
        imageForm.setImageFile(file);

        assertDoesNotThrow(() -> {
            imageService.saveImage(imageForm, user);
        });

        // Check that the image metadata has been saved in the database -- check with nate that we can use the images repo at this layer
        List<UserImage> images = imagesRepo.findByUser(user);
        assertTrue(!images.isEmpty());

        // Check that the image file has been saved in the correct location
        UserImage image = images.get(0);
        Path imagePath = Path.of(image.getImageLocation(), image.getImageName());
        assertTrue(Files.exists(imagePath));

        // Clean up
        imagesRepo.deleteAll();
        File directory = new File("./images/" + user.getId());
        if (directory.exists()) {
            for (File fileToDelete : directory.listFiles()) {
                fileToDelete.delete();
            }
            directory.delete();
        }
    }

    @Test
    void testValidPullImages() throws IOException {
        List<String> imageLocations = imageService.pullImages(user);
        assertEquals(2, imageLocations.size());
        assertTrue(imageLocations.contains(image1.getImageLocation() + image1.getImageName()));
        assertTrue(imageLocations.contains(image2.getImageLocation() + image2.getImageName()));
    }
}

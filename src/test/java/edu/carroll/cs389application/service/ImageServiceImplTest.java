package edu.carroll.cs389application.service;

import edu.carroll.cs389application.jpa.model.Login;
import edu.carroll.cs389application.jpa.model.UserImage;
import edu.carroll.cs389application.service.ImageServiceImpl.ErrorCode;
import edu.carroll.cs389application.web.form.ImageForm;
import edu.carroll.cs389application.web.form.LoginForm;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for the ImageService implementation.
 * <p>
 * Combined pullimages and saveimage into one because they test all the functionality together.
 */
@SpringBootTest
public class ImageServiceImplTest {

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    private final String testuser = "testuser";

    private final LoginForm testform = new LoginForm(testuser);

    /**
     * Set up the mock objects before each test case.
     *
     * @throws IOException if there is an error in the setup process.
     */
    @BeforeEach
    public void setup() throws IOException {
        MockitoAnnotations.openMocks(this);
        if (userService.validateUsername(testform)) {
            //Create and validate our user in the DB.
        }
    }

    /**
     * This method will clean up the database after writing images.
     *
     * @throws IOException will throw if images failed to clean
     */
    @AfterEach
    public void cleanup() throws IOException {
        Login user = userService.loginFromUsername(testuser);
        imageService.deleteUserImages(user);
    }

    /**
     * Test saving a valid image file.
     *
     * @throws IOException if there is an error in the process of saving the image.
     */
    @Test
    public void testSavePullImageOneValidFile() throws IOException {
        Login user = userService.loginFromUsername(testuser);
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_BINARY);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] fileBytes = baos.toByteArray();
        MultipartFile file1 = new MockMultipartFile("test-image1.jpg", "image1.jpg", "image/jpeg", fileBytes);
        ImageForm form1 = new ImageForm(file1);
        imageService.saveImage(form1, user);
        List<Pair<InputStream, String>> images = imageService.pullImages(user);
        List<UserImage> userimages = imageService.loadUserImagesbyUserID(user);
        for (Pair<InputStream, String> pair : images) {
            byte[] pairbytes = pair.getFirst().readAllBytes();
            assertArrayEquals(pairbytes, fileBytes);
        }
        String contentType1 = userimages.get(0).getExtension().equalsIgnoreCase("jpg") || userimages.get(0).getExtension().equalsIgnoreCase("jpeg") ? "image/jpeg" : "image/png";

        assertEquals(userimages.get(0).getImageName(), file1.getOriginalFilename());
        assertTrue(contentType1.equals(file1.getContentType()));
    }

    /**
     * This method will test if you test a pull with zero images/null.
     *
     * @throws IOException Catch if the image pull fails or the image save fails
     */
    @Test
    public void testSavePullImageNoFile() throws IOException {
        Login user = userService.loginFromUsername(testuser);
        //No Imagefile in form
        ImageForm form1 = new ImageForm();
        imageService.saveImage(form1, user);
        List<Pair<InputStream, String>> images = imageService.pullImages(user);
        List<UserImage> userimages = imageService.loadUserImagesbyUserID(user);
        assertTrue(images.isEmpty());
        assertTrue(userimages.isEmpty());

    }

    /**
     * Test saving and pulling images for two valid files.
     *
     * @throws IOException Throws if the save or pull fails.
     */
    @Test
    public void testSavePullImageTwoValidFile() throws IOException {
        Login user = userService.loginFromUsername(testuser);
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_BINARY);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] fileBytes = baos.toByteArray();
        MultipartFile file1 = new MockMultipartFile("test-image1.png", "image1.png", "image/png", fileBytes);
        MultipartFile file2 = new MockMultipartFile("test-image2.png", "image2.png", "image/png", fileBytes);
        ImageForm form1 = new ImageForm(file1);
        ImageForm form2 = new ImageForm(file2);
        imageService.saveImage(form1, user);
        imageService.saveImage(form2, user);
        List<Pair<InputStream, String>> images = imageService.pullImages(user);
        List<UserImage> userimages = imageService.loadUserImagesbyUserID(user);
        for (Pair<InputStream, String> pair : images) {
            byte[] pairbytes = pair.getFirst().readAllBytes();
            assertArrayEquals(pairbytes, fileBytes);
        }
        String contentType1 = userimages.get(0).getExtension().equalsIgnoreCase("jpg") || userimages.get(0).getExtension().equalsIgnoreCase("jpeg") ? "image/jpeg" : "image/png";
        String contentType2 = userimages.get(1).getExtension().equalsIgnoreCase("jpg") || userimages.get(1).getExtension().equalsIgnoreCase("jpeg") ? "image/jpeg" : "image/png";

        assertEquals(userimages.get(0).getImageName(), file1.getOriginalFilename());
        assertTrue(contentType1.equals(file1.getContentType()));
        assertEquals(userimages.get(1).getImageName(), file2.getOriginalFilename());
        assertTrue(contentType2.equals(file2.getContentType()));
    }

    /**
     * Test the saving and pulling of two identical images.
     *
     * @throws IOException Throws if error occurs while saving or pulling the images.
     */
    @Test
    public void testSavePullImageIdenticalValidFile() throws IOException {
        Login user = userService.loginFromUsername(testuser);
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_BINARY);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] fileBytes = baos.toByteArray();
        MultipartFile file1 = new MockMultipartFile("test-image1.png", "image1.png", "image/png", fileBytes);
        ImageForm form1 = new ImageForm(file1);
        imageService.saveImage(form1, user);
        imageService.saveImage(form1, user);
        List<Pair<InputStream, String>> images = imageService.pullImages(user);
        List<UserImage> userimages = imageService.loadUserImagesbyUserID(user);
        for (Pair<InputStream, String> pair : images) {
            byte[] pairbytes = pair.getFirst().readAllBytes();
            assertArrayEquals(pairbytes, fileBytes);
        }
        String contentType1 = userimages.get(0).getExtension().equalsIgnoreCase("jpg") || userimages.get(0).getExtension().equalsIgnoreCase("jpeg") ? "image/jpeg" : "image/png";
        String contentType2 = userimages.get(1).getExtension().equalsIgnoreCase("jpg") || userimages.get(1).getExtension().equalsIgnoreCase("jpeg") ? "image/jpeg" : "image/png";

        assertEquals(userimages.get(0).getImageName(), file1.getOriginalFilename());
        assertTrue(contentType1.equals(file1.getContentType()));
        assertEquals(userimages.get(1).getImageName(), file1.getOriginalFilename());
        assertTrue(contentType2.equals(file1.getContentType()));
    }

    /**
     * Tests saving and pulling of two different file types (png and jpg).
     *
     * @throws IOException Throws if fails to pull or save.
     */
    @Test
    public void testSavePullImageDifferentValidFile() throws IOException {
        Login user = userService.loginFromUsername(testuser);
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_BINARY);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] fileBytes = baos.toByteArray();
        MultipartFile file1 = new MockMultipartFile("test-image1.jpg", "image1.jpg", "image/jpeg", fileBytes);
        MultipartFile file2 = new MockMultipartFile("test-image2.png", "image2.png", "image/png", fileBytes);
        ImageForm form1 = new ImageForm(file1);
        ImageForm form2 = new ImageForm(file2);
        imageService.saveImage(form1, user);
        imageService.saveImage(form2, user);
        List<Pair<InputStream, String>> images = imageService.pullImages(user);
        List<UserImage> userimages = imageService.loadUserImagesbyUserID(user);
        for (Pair<InputStream, String> pair : images) {
            byte[] pairbytes = pair.getFirst().readAllBytes();
            assertArrayEquals(pairbytes, fileBytes);
        }
        String contentType1 = userimages.get(0).getExtension().equalsIgnoreCase("jpg") || userimages.get(0).getExtension().equalsIgnoreCase("jpeg") ? "image/jpeg" : "image/png";
        String contentType2 = userimages.get(1).getExtension().equalsIgnoreCase("jpg") || userimages.get(1).getExtension().equalsIgnoreCase("jpeg") ? "image/jpeg" : "image/png";

        assertEquals(userimages.get(0).getImageName(), file1.getOriginalFilename());
        assertTrue(contentType1.equals(file1.getContentType()));
        assertEquals(userimages.get(1).getImageName(), file2.getOriginalFilename());
        assertTrue(contentType2.equals(file2.getContentType()));
    }


    /**
     * Test validating a valid file.
     *
     * @throws IOException if there is an error in the process of validating the file.
     */
    @Test
    public void testValidateFileValid() throws IOException {
        byte[] fileBytes = new byte[]{0x00};
        MockMultipartFile file = new MockMultipartFile("test-image.jpg", "test-image.jpg", "image/jpeg", fileBytes);
        ErrorCode result = imageService.validateFile(file);
        assertEquals(ErrorCode.VALID_FILE, result);
    }

    /**
     * Test validating a null file.
     */
    @Test
    public void testValidateFileNullFile() {
        ErrorCode result = imageService.validateFile(null);
        assertEquals(ErrorCode.INVALID_FILE_ISNULL, result);
    }

    /**
     * Test validating a empty file.
     */
    @Test
    void testValidateFileEmptyFile() {
        MockMultipartFile file = new MockMultipartFile("test-image.jpg", new byte[0]);
        ErrorCode result = imageService.validateFile(file);
        assertEquals(ErrorCode.INVALID_FILE_ISNULL, result);
    }

    /**
     * Test validating a large file over 10MB.
     */
    @Test
    public void testValidateFileLargeFile() {
        byte[] data = new byte[1024 * 1024 * 11];
        MockMultipartFile file = new MockMultipartFile("test-image.jpg", "image.jpg", "image/jpg", data);
        ErrorCode result = imageService.validateFile(file);
        assertEquals(ErrorCode.INVALID_FILE_SIZE, result);
    }

    /**
     * Test validating a invalid file type.
     */
    @Test
    public void testValidateFileInvalidType() {
        MockMultipartFile file = new MockMultipartFile("test-image.txt", "test.txt", "text/plain", new byte[1024]);
        ErrorCode result = imageService.validateFile(file);
        assertEquals(ErrorCode.INVALID_FILE_TYPE, result);
    }

    /**
     * Testing Null Content Type within a file.
     */
    @Test
    public void testValidateFileNoContentType() {
        MockMultipartFile file = new MockMultipartFile("test-image.txt", "test.txt", null, new byte[1024]);
        ErrorCode result = imageService.validateFile(file);
        assertEquals(ErrorCode.INVALID_FILE_ISNULL, result);
    }

    /**
     * Tests a file with no data in it.
     */
    @Test
    public void testValidateFileTestNoImageData() {
        MockMultipartFile file = new MockMultipartFile("test-image.txt", "test.txt", "text/plain", (byte[]) null);
        ErrorCode result = imageService.validateFile(file);
        assertEquals(ErrorCode.INVALID_FILE_EMPTY, result);
    }

}
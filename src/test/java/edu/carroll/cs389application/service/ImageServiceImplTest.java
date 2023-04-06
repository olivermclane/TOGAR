package edu.carroll.cs389application.service;

import edu.carroll.cs389application.jpa.model.Login;
import edu.carroll.cs389application.service.ImageServiceImpl.ErrorCode;
import edu.carroll.cs389application.web.form.ImageForm;
import edu.carroll.cs389application.web.form.LoginForm;
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

import static org.junit.Assert.assertEquals;


/**
 *
 */
@SpringBootTest
public class ImageServiceImplTest {

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    /**
     * @throws IOException
     */
    @BeforeEach
    public void setup() throws IOException {
        MockitoAnnotations.openMocks(this);
        if (userService.validateUsername(new LoginForm("testuser"))) {
            //Create and validate our user in the DB.
        }

    }

    /**
     *
     */
    @Test
    public void testSaveImageValidFile() throws IOException {
        Login user = userService.loginFromUsername("testuser");
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_BINARY);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] fileBytes = baos.toByteArray();
        MockMultipartFile file = new MockMultipartFile("test-image.png", "image.png", "image/png", fileBytes);
        ImageForm form = new ImageForm(file);
        Assertions.assertDoesNotThrow(() -> imageService.saveImage(form, user));
    }

    /**
     * @throws IOException
     */
    @Test
    public void testPullImages() throws IOException {
        Login user = userService.loginFromUsername("testuser");
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_BINARY);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] fileBytes = baos.toByteArray();
        MultipartFile file1 = new MockMultipartFile("test-image1.png", "image1.png", "image/png", fileBytes);
        ImageForm form = new ImageForm(file1);
        imageService.saveImage(form, user);
        List<Pair<InputStream, String>> images = imageService.pullImages(user);
        Assertions.assertNotNull(images);
    }

    /**
     * @throws IOException
     */
    @Test
    public void testValidateFileValid() throws IOException {
        byte[] fileBytes = new byte[]{0x00};
        MockMultipartFile file = new MockMultipartFile("test-image.jpg", "test-image.jpg", "image/jpeg", fileBytes);
        ErrorCode result = imageService.validateFile(file);
        assertEquals(ErrorCode.VALID_FILE, result);
    }

    /**
     *
     */
    @Test
    public void testValidateFileNullFile() {
        ErrorCode result = imageService.validateFile(null);
        assertEquals(ErrorCode.INVALID_FILE_ISNULL, result);
    }

    /**
     *
     */
    @Test
    void testValidateFileEmptyFile() {
        MockMultipartFile file = new MockMultipartFile("test-image.jpg", new byte[0]);
        ErrorCode result = imageService.validateFile(file);
        assertEquals(ErrorCode.INVALID_FILE_EMPTY, result);
    }

    /**
     *
     */
    @Test
    public void testValidateFileLargeFile() {
        byte[] data = new byte[1024 * 1024 * 11];
        MockMultipartFile file = new MockMultipartFile("test-image.jpg", "image.jpg", "image/jpg", data);

        ErrorCode result = imageService.validateFile(file);
        assertEquals(ErrorCode.INVALID_FILE_SIZE, result);
    }

    /**
     *
     */
    @Test
    public void testValidateFileInvalidType() {
        MockMultipartFile file = new MockMultipartFile("test-image.txt", "test.txt", "text/plain", new byte[1024]);
        ErrorCode result = imageService.validateFile(file);
        assertEquals(ErrorCode.INVALID_FILE_TYPE, result);
    }
}
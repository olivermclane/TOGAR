package edu.carroll.cs389application.service;

import edu.carroll.cs389application.service.ImageServiceImpl.ErrorCode;
import edu.carroll.cs389application.jpa.model.Login;
import edu.carroll.cs389application.web.form.ImageForm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.mock.web.MockMultipartFile;


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

    /**
     *
     * @throws IOException
     */
    @BeforeEach
    public void setup() throws IOException {
        MockitoAnnotations.openMocks(this);


        //Need to add images so that pullImages works

    }

    /**
     *
     */
    @Test
    public void testSaveImageValidFile() {
        byte[] fileBytes = new byte[]{0x00, 0x01};
        MockMultipartFile file = new MockMultipartFile("test.png", fileBytes);
        Login user = new Login("testuser");
        ImageForm form = new ImageForm(file);
        Assertions.assertDoesNotThrow(() -> imageService.saveImage(form, user));
    }

    /**
     *
     * @throws IOException
     */
    @Test
    public void testPullImages() throws IOException {
        Login user = new Login("testuser");
        List<Pair<InputStream, String>> images = imageService.pullImages(user);
        Assertions.assertNotNull(images);
    }

    /**
     *
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
        ImageServiceImpl.ErrorCode result = imageService.validateFile(file);
        assertEquals(ErrorCode.INVALID_FILE_EMPTY, result);
    }

    /**
     *
     */
    @Test
    public void testValidateFileLargeFile() {
        byte[] data = new byte[1024 * 1024 * 11];
        MockMultipartFile file = new MockMultipartFile("test-image.jpg", data);
        ImageServiceImpl.ErrorCode result = imageService.validateFile(file);
        assertEquals(ErrorCode.INVALID_FILE_SIZE, result);
    }

    /**
     *
     */
    @Test
    public void testValidateFileInvalidType() {
        MockMultipartFile file = new MockMultipartFile("test-image.txt", "test.txt", "text/plain", new byte[1024]);
        ImageServiceImpl.ErrorCode result = imageService.validateFile(file);
        assertEquals(ErrorCode.INVALID_FILE_TYPE, result);
    }
}
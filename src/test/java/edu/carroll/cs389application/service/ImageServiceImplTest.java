package edu.carroll.cs389application.service;

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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;


@SpringBootTest
public class ImageServiceImplTest {

    @Autowired
    private ImageService imageService;

    @BeforeEach
    public void init() throws IOException {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveImageValidFile() {
        byte[] fileBytes = new byte[]{0x00};
        MockMultipartFile file = new MockMultipartFile("test.png", fileBytes);
        Login user = new Login();
        ImageForm form = new ImageForm(file);
        form.setImageFile(file);
        Assertions.assertDoesNotThrow(() -> imageService.saveImage(form, user));
    }

    @Test
    public void testPullImages() throws IOException {
        Login user = new Login();
        List<Pair<InputStream, String>> images = imageService.pullImages(user);
        Assertions.assertNotNull(images);
    }

    @Test
    public void testValidateFileValid() throws IOException {
        MockMultipartFile file = new MockMultipartFile("test-image.jpg", "test-image.jpg", "image/jpeg", Files.readAllBytes(Paths.get("./src/test/resources/test-image.jpg")));
        ImageServiceImpl.ErrorCode result = imageService.validateFile(file);
        assertEquals(ImageService.ErrorCode.VALID_FILE, result);
    }

    @Test
    public void testValidateFileNullFile() {
        ImageServiceImpl.ErrorCode result = imageService.validateFile(null);
        assertEquals(ImageService.ErrorCode.INVALID_FILE_ISNULL, result);
    }

    @Test
    void testValidateFileEmptyFile() {
        MockMultipartFile file = new MockMultipartFile("test-image.jpg", new byte[0]);
        ImageServiceImpl.ErrorCode result = imageService.validateFile(file);
        assertEquals(ImageServiceImpl.ErrorCode.INVALID_FILE_EMPTY, result);
    }

    @Test
    public void testValidateFileLargeFile() {
        byte[] data = new byte[1024 * 1024 * 11];
        MockMultipartFile file = new MockMultipartFile("test-image.jpg", data);
        ImageServiceImpl.ErrorCode result = imageService.validateFile(file);
        assertEquals(ImageServiceImpl.ErrorCode.INVALID_FILE_SIZE, result);
    }

    @Test
    public void testValidateFileInvalidType() {
        MockMultipartFile file = new MockMultipartFile("test-image.txt", "test.txt", "text/plain", new byte[1024]);
        ImageServiceImpl.ErrorCode result = imageService.validateFile(file);
        assertEquals(ImageServiceImpl.ErrorCode.INVALID_FILE_TYPE, result);
    }
}
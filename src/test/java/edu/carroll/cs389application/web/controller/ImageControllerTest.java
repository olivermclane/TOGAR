package edu.carroll.cs389application.web.controller;

import edu.carroll.cs389application.service.ImageService;
import edu.carroll.cs389application.service.ImageServiceImpl.ErrorCode;
import edu.carroll.cs389application.service.UserService;
import edu.carroll.cs389application.web.form.ImageForm;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This class contains unit tests for the ImageController class.
 */
public class ImageControllerTest {

    private ImageService imageServiceMock;
    private UserService userServiceMock;
    private ImageController controller;
    private Model modelMock;
    private HttpSession sessionMock;

    /**
     * Sets up the test environment by creating mocks and the ImageController instance.
     */
    @BeforeEach
    public void setUp() {
        imageServiceMock = mock(ImageService.class);
        userServiceMock = mock(UserService.class);
        modelMock = mock(Model.class);
        sessionMock = mock(HttpSession.class);
        controller = new ImageController(imageServiceMock, userServiceMock);
    }

    /**
     * Tests the imageGallery method when there is no session.
     */
    @Test
    public void testDisplayImagesWithoutSession() {
        when(sessionMock.getAttribute("username")).thenReturn(null);
        MultipartFile fileMock = mock(MultipartFile.class);
        BindingResult bindingResultMock = mock(BindingResult.class);

        String viewName = controller.imageGallery(modelMock, sessionMock);

        assertThat(viewName).isEqualTo("redirect:/index");
    }


    /**
     * Tests the imageGallery method when there is no username in the session.
     */
    @Test
    public void testDisplayImagesWithoutUsername() {
        when(sessionMock.getAttribute("username")).thenReturn("testuser");
        MultipartFile fileMock = mock(MultipartFile.class);
        BindingResult bindingResultMock = mock(BindingResult.class);

        String viewName = controller.imageGallery(modelMock, sessionMock);

        assertThat(viewName).isEqualTo("togar");
    }


    /**
     * Tests the imageGallery method when there is no username in the session.
     */
    @Test
    public void testHandleFileUploadWithBindingError() {
        BindingResult bindingResultMock = mock(BindingResult.class);
        when(bindingResultMock.hasErrors()).thenReturn(true);
        MultipartFile fileMock = mock(MultipartFile.class);

        String viewName = controller.handleFileUpload(new ImageForm(fileMock), fileMock, bindingResultMock, modelMock, sessionMock);

        assertThat(viewName).isEqualTo("togar");
    }

    /**
     * Tests the handleFileUpload method when the file is invalid.
     */
    @Test
    public void testHandleFileUploadWithInvalidFile() {
        MockMultipartFile fileMock = new MockMultipartFile("imageFile", "test.jpg", "image/jpeg", "not an image".getBytes());
        when(imageServiceMock.validateFile(fileMock)).thenReturn(ErrorCode.INVALID_FILE_TYPE);
        MultipartFile multipartFile = fileMock;
        BindingResult bindingResultMock = mock(BindingResult.class);
        String viewName = controller.handleFileUpload(new ImageForm(multipartFile), multipartFile, bindingResultMock, modelMock, sessionMock);
        assertThat(viewName).isEqualTo("togar");
    }

    /**
     * Tests the handleFileUpload method for a valid file.
     */
    @Test
    public void testHandleFileUploadWithValidFile() {
        MockMultipartFile fileMock = new MockMultipartFile("imageFile", "test.jpg", "image/jpeg", "test".getBytes());
        when(imageServiceMock.validateFile(fileMock)).thenReturn(ErrorCode.VALID_FILE);
        assertThat(imageServiceMock.validateFile(fileMock)).isEqualTo(ErrorCode.VALID_FILE);
        MultipartFile multipartFile = fileMock;
        BindingResult bindingResultMock = mock(BindingResult.class);
        String viewName = controller.handleFileUpload(new ImageForm(multipartFile), multipartFile, bindingResultMock, modelMock, sessionMock);
        assertThat(viewName).isEqualTo("redirect:/togar");
    }

}
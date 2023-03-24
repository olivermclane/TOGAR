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

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ImageControllerTest {

    private ImageService imageServiceMock;
    private UserService userServiceMock;
    private ImageController controller;
    private Model modelMock;
    private HttpSession sessionMock;

    @BeforeEach
    public void setUp() {
        imageServiceMock = mock(ImageService.class);
        userServiceMock = mock(UserService.class);
        modelMock = mock(Model.class);
        sessionMock = mock(HttpSession.class);
        controller = new ImageController(imageServiceMock, userServiceMock);
    }

    @Test
    public void testDisplayImagesWithoutSession() throws IOException {
        when(sessionMock.getAttribute("username")).thenReturn(null);
        MultipartFile fileMock = mock(MultipartFile.class);
        BindingResult bindingResultMock = mock(BindingResult.class);

        String viewName = controller.imageGallery(modelMock, sessionMock);

        assertThat(viewName).isEqualTo("redirect:/index");
    }

    @Test
    public void testDisplayImagesWithoutUsername() throws IOException {
        when(sessionMock.getAttribute("username")).thenReturn("testuser");
        MultipartFile fileMock = mock(MultipartFile.class);
        BindingResult bindingResultMock = mock(BindingResult.class);

        String viewName = controller.imageGallery(modelMock, sessionMock);

        assertThat(viewName).isEqualTo("togar");
    }

    @Test
    public void testHandleFileUploadWithBindingError() throws IOException {
        BindingResult bindingResultMock = mock(BindingResult.class);
        when(bindingResultMock.hasErrors()).thenReturn(true);
        MultipartFile fileMock = mock(MultipartFile.class);

        String viewName = controller.handleFileUpload(new ImageForm(fileMock), fileMock, bindingResultMock, modelMock, sessionMock);

        assertThat(viewName).isEqualTo("togar");
    }

    @Test
    public void testHandleFileUploadWithInvalidFile() throws IOException {
        MockMultipartFile fileMock = new MockMultipartFile("imageFile", "test.jpg", "image/jpeg", "not an image".getBytes());
        when(imageServiceMock.validateFile(fileMock)).thenReturn(ErrorCode.INVALID_FILE_TYPE);
        MultipartFile multipartFile = fileMock;
        BindingResult bindingResultMock = mock(BindingResult.class);


        String viewName = controller.handleFileUpload(new ImageForm(multipartFile), multipartFile, bindingResultMock, modelMock, sessionMock);

        assertThat(viewName).isEqualTo("togar");
    }

    @Test
    public void testHandleFileUploadWithValidFile() throws IOException {
        MockMultipartFile fileMock = new MockMultipartFile("imageFile", "test.jpg", "image/jpeg", "test".getBytes());
        when(imageServiceMock.validateFile(fileMock)).thenReturn(ErrorCode.VALID_FILE);
        assertThat(imageServiceMock.validateFile(fileMock)).isEqualTo(ErrorCode.VALID_FILE);
        MultipartFile multipartFile = fileMock;
        BindingResult bindingResultMock = mock(BindingResult.class);

        String viewName = controller.handleFileUpload(new ImageForm(multipartFile), multipartFile, bindingResultMock, modelMock, sessionMock);

        assertThat(viewName).isEqualTo("togar");
    }

}
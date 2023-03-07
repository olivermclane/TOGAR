package edu.carroll.cs389application.web.controller;

import edu.carroll.cs389application.jpa.model.Login;
import edu.carroll.cs389application.service.ImageService;
import edu.carroll.cs389application.service.UserService;
import edu.carroll.cs389application.web.form.ImageForm;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(ImageController.class)
public class ImageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @MockBean
    private UserService userService;

    @Test
    public void testHandleFileUpload() throws Exception {
        MockMultipartFile file = new MockMultipartFile("imageFile", "test.png", "image/png", "test".getBytes());
        ImageForm form = new ImageForm(file);


        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file)
                        .flashAttr("fileForm", form)
                        .sessionAttr("username", "testuser"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/togar?Username=testuser"))
                .andExpect(MockMvcResultMatchers.flash().attributeCount(0));

        // assert that the image has been saved in the database -- Need nates help
        //My thoguh

        // assert that the image file has been saved in the correct location
        String expectedPath = "./images/ + " + userService.loginFromUsername("testuser").getId()+"/test.png";
        Path imagePath = Paths.get(expectedPath);
        assertTrue(Files.exists(imagePath));
        assertArrayEquals("test".getBytes(), Files.readAllBytes(imagePath));
    }

    @Test
    public void testHandleFileUploadWithInvalidInput() throws Exception {
        MockMultipartFile file = new MockMultipartFile("imageFile", "test.png", "image/png", new byte[0]);
        ImageForm form = new ImageForm(file);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file)
                        .flashAttr("fileForm", form)
                        .sessionAttr("username", "testuser"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("togar"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("fileForm", "imageFile"));
    }

    @Test
    public void testImageGallery() throws Exception {
        List<String> imageLocations = Arrays.asList("./images/" + userService.loginFromUsername("testuser").getId()+
                "/image1.jpg", "./images/" + + userService.loginFromUsername("testuser").getId()+ "/image2.jpg");

        Mockito.when(imageService.pullImages(Mockito.any(Login.class))).thenReturn(imageLocations);

        mockMvc.perform(MockMvcRequestBuilders.get("/togar")
                        .param("Username", "testuser"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("togar"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("images"))
                .andExpect(MockMvcResultMatchers.model().attribute("images", Matchers.hasSize(2)));
    }
}

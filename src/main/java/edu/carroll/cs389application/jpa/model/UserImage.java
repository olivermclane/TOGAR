package edu.carroll.cs389application.jpa.model;

import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;


@Entity
@Table(name = "userImage")
public class UserImage {
    @Column(name = "image_name", nullable = false)
    private String imageName;
    @Column(name = "extension", nullable = false)
    private String extension;
    @Column(name = "location", nullable = false)
    private String imageLocation;
    @Column(name = "image_size", nullable = false)
    private long imageSize;
    @Column(name = "width", nullable = false)
    private int imageWidth;
    @Column(name = "height", nullable = false)
    private int imageHeight;
    @Transient
    private MultipartFile imageFile;
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "login_id")
    private Login user;

    public UserImage() {
        // default constructor required by JPA
    }

    public UserImage(String imageName, String extension, String imageLocation, long imageSize, int imageHeight, int imageWidth, Login login) {
        this.imageName = imageName;
        this.extension = extension;
        this.imageLocation = imageLocation;
        this.imageSize = imageSize;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.user = login;
    }

    public Integer getId() {
        return id;
    }

    public String getImageName() {
        return imageName;
    }

    public String getExtension() {
        return extension;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public long getImageSize() {
        return imageSize;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    @Override
    public String toString() {
        return "Image{" + "id=" + id + ", filename='" + imageName + '\'' + ", imageExtension='" + extension + '\'' + ", fileSize=" + imageSize + ", width=" + imageHeight + ", height=" + imageWidth + '}';
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}

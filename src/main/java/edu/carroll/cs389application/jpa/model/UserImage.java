package edu.carroll.cs389application.jpa.model;

import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;


@Entity
@Table(name = "userImage")
public class UserImage {
    @Column(name = "imagename", nullable = false)
    private final String imageName;
    @Column(name = "extension", nullable = false)
    private final String extension;
    @Column(name = "location", nullable = false)
    private final String imageLocation;
    @Column(name = "imagesize", nullable = false)
    private final long imageSize;
    @Column(name = "width", nullable = false)
    private final int imageWidth;
    @Column(name = "height", nullable = false)
    private final int imageHeight;
    @Transient
    private MultipartFile imageFile;
    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    private Login user;

    public UserImage(String imageName, String extension, String imageLocation, long imageSize, int imageHeight, int imageWidth) {
        this.imageName = imageName;
        this.extension = extension;
        this.imageLocation = imageLocation;
        this.imageSize = imageSize;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
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

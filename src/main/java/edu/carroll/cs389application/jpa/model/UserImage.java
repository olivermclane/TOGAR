/**
 * The edu.carroll.cs389application.jpa.model package contains the classes used to represent the data model
 * for the application. This package includes the Login and UserImage classes.
 */
package edu.carroll.cs389application.jpa.model;

import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * The UserImage class represents an image uploaded by a user in the application.
 * It contains information about the image, including the file name, extension, location, size,
 * width, and height. The UserImage class is associated with a Login instance, which represents the user who uploaded
 * the image.
 */
@Entity
@Table(name = "user_images")
public class UserImage {

    /**
     * The name of the image file.
     */
    @Column(name = "image_name", nullable = false)
    private String imageName;
    /**
     * The file extension of the image file.
     */
    @Column(name = "extension", nullable = false)
    private String extension;
    /**
     * The location of the image file.
     */
    @Column(name = "location", nullable = false)
    private String imageLocation;
    /**
     * The size of the image file.
     */
    @Column(name = "image_size", nullable = false)
    private long imageSize;
    /**
     * The width of the image file.
     */
    @Column(name = "width", nullable = false)
    private int imageWidth;
    /**
     * The height of the image file.
     */
    @Column(name = "height", nullable = false)
    private int imageHeight;
    /**
     * The MultipartFile instance of the image file.
     */
    @Transient
    private MultipartFile imageFile;
    /**
     * The id of the UserImage instance.
     */
    @Id
    @GeneratedValue
    private Integer id;
    /**
     * The Login instance representing the user who uploaded the image.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "login_id")
    private Login user;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private Date createdAt;

    @Column(name = "updatedAt", nullable = false)
    private Date updatedAt;

    // Getters and setters for other fields

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }


    /**
     * Creates a new UserImage instance with default values.
     */
    public UserImage() {
// default constructor required by JPA
    }

    /**
     * Creates a new UserImage instance with the specified values.
     *
     * @param imageName     the name of the image file
     * @param extension     the file extension of the image file
     * @param imageLocation the location of the image file
     * @param imageSize     the size of the image file
     * @param imageHeight   the height of the image file
     * @param imageWidth    the width of the image file
     * @param login         the Login instance representing the user who uploaded the image
     */
    public UserImage(String imageName, String extension, String imageLocation, long imageSize, int imageHeight, int imageWidth, Login login) {
        this.imageName = imageName;
        this.extension = extension;
        this.imageLocation = imageLocation;
        this.imageSize = imageSize;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.user = login;
    }

    /**
     * Returns the id of the UserImage instance.
     *
     * @return the id of the UserImage instance
     */
    public Integer getId() {
        return id;
    }

    /**
     * Returns the name of the image file.
     *
     * @return the name of the image file
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * Returns the file extension of the image file.
     *
     * @return the file extension of the image file
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Returns the location of the image file.
     *
     * @return the location of the image file
     */
    public String getImageLocation() {
        return imageLocation;
    }

    /**
     * Returns the size of the image file in bytes.
     *
     * @return the size of the image file in bytes
     */
    public long getImageSize() {
        return imageSize;
    }

    /**
     * Returns the width of the image in pixels.
     *
     * @return the width of the image in pixels
     */
    public int getImageWidth() {
        return imageWidth;
    }

    /**
     * Returns the height of the image in pixels.
     *
     * @return the height of the image in pixels
     */
    public int getImageHeight() {
        return imageHeight;
    }

    /**
     * Returns a string representation of the Image object.
     *
     * @return a string representation of the Image object
     */
    @Override
    public String toString() {
        return "Image{" + "id=" + id + ", filename='" + imageName + '\'' + ", imageExtension='" + extension + '\'' + ", fileSize=" + imageSize + ", width=" + imageHeight + ", height=" + imageWidth + '}';
    }

    /**
     * Returns the MultipartFile object representing the image file.
     *
     * @return the MultipartFile object representing the image file
     */
    public MultipartFile getImageFile() {
        return imageFile;
    }

    /**
     * Sets the MultipartFile object representing the image file.
     *
     * @param imageFile the MultipartFile object representing the image file
     */
    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    /**
     * Returns the Login object associated with the Image.
     *
     * @return the Login object associated with the Image
     */
    public Login getUser() {
        return user;
    }

}

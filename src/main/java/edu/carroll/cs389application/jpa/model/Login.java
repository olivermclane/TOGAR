/**
 * The edu.carroll.cs389application.jpa.model package contains the Login class which is an entity class
 * used to represent login data in the database. It has a unique ID, a username which is stored in the
 * "username" column in the database, and a list of UserImage objects associated with it.
 */
package edu.carroll.cs389application.jpa.model;

import jakarta.persistence.*;
import java.util.Date;


import java.util.ArrayList;
import java.util.List;

/**
 * The Login class represents a user login data.
 * It is mapped to the "logindata" table in the database.
 */
@Entity
@Table(name = "logindata")
public class Login {

    private static final long serialVersionUID = 1L;
    private static final String EOL = System.lineSeparator();
    private static final String TAB = "\t";

    /**
     * A unique ID used to identify the row in the database.
     */
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Integer login_id;

    /**
     * The username associated with the login. It is stored in the "username" column
     * in the database and should be unique.
     */
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * A list of UserImage objects associated with the Login object.
     */
    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    private List<UserImage> userImages;

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
     * A default constructor required by JPA.
     */
    public Login() {
    }

    /**
     * Constructor for a new Login object with a specified username.
     *
     * @param username The username associated with the login.
     */
    public Login(String username) {
        this.username = username;
        this.userImages = new ArrayList<>();
    }

    /**
     * Returns the ID of the row in the database associated with the Login object.
     *
     * @return The ID of the row in the database.
     */
    public Integer getLogin_id() {
        return login_id;
    }

    /**
     * Sets the ID of the row in the database associated with the Login object.
     *
     * @param id The ID of the row in the database.
     */
    public void setLogin_id(Integer id) {
        this.login_id = id;
    }

    /**
     * Returns the username associated with the Login object.
     *
     * @return The username associated with the Login object.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username associated with the Login object.
     *
     * @param username The new username to be associated with the Login object.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns a string representation of the Login object.
     *
     * @return A string representation of the Login object.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Login @ ").append(super.toString()).append("[").append(System.lineSeparator());
        builder.append("\t").append("username=").append(username).append(System.lineSeparator());
        builder.append("]").append(System.lineSeparator());
        return builder.toString();
    }
}

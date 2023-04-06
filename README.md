# TOGAR
The application allows users to upload images and view their image gallery. To upload an image, the user must select the image file and if the image file fails validation, the method redirects the user to the image gallery view with an error message. If the image file passes validation, the method saves the image to the postgres database and redirects the user to the image gallery view. The user's image gallery is populated by retrieving the list of image streams for the specified user using the ImageService. Each image is then converted to a base64-encoded string and added to a list of image pairs containing the image source and content type. The list of image pairs is then added to the model attribute "images" and displayed in the view. In all my application is a basic image uploading and viewing webapp that takes multipart files in and displays them on our front-end.

## Application Setup
  
  ### Step-By-Step Guide for Database
  1.	Install Docker on your computer if you haven't already.
  2.	Clone the project from GitHub.
  3.	Open a terminal or command prompt and navigate to the root directory of the project.
  4.	Build the Docker image by running the following command: **docker build -t togardatabase -f docker/dockerfile.yaml .**
  5.	Start a Docker container from the image with the following command: **docker run --name togarpostgres -p 5432:5432 -d togardatabase**
  6.	Verify that the container is running by running the following command: **docker ps**
  7.		
  ### Step-By-Step Guide for Running Application
  1. Navigate to the root directory of the project.
  2. In the terminal prompt run: **./gradlew bootrun** 
  3. Open up your prefered browers and navigate to **http://localhost:8080/**
  4. Enjoy the application!

## Application Breakdown
  ### Viewing images at directory view
  1. Each user is assigned a Unique Idenfication Number
  2. In the file-tree, we can see the directory **/images**, double click it!
  3. In the directory, you will see each user you have created by the associated UID.
  4. In each directory (UID), you will see the images they have uploaded.
  
  ### Running application tests
  1. Navigate to the root directoyr of the project.
  2. In a terminal prompt run: **./gradlew test**
  3. The terminal window will run threw all the tests located in our testing folder.
  4. Afterwards, it will tell you whether the test pass or failed, marked by a **Build Faild or Build Successful**.

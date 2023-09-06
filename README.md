# TOGAR

The application allows users to upload images and view their image gallery. To upload an image, the user must select the
image file and if the image file fails validation, the method redirects the user to the image gallery view with an error
message. If the image file passes validation, the method saves the image to the postgres database and redirects the user
to the image gallery view. The user's image gallery is populated by retrieving the list of image streams for the
specified user using the ImageService. Each image is then converted to a base64-encoded string and added to a list of
image pairs containing the image source and content type. The list of image pairs is then added to the model attribute "
images" and displayed in the view. In all my application is a basic image uploading and viewing webapp that takes
multipart files in and displays them on our front-end.

## Application Setup

### Step-By-Step Guide for Database

1. Install Docker on your computer if you haven't already.

       Docker for Windows: https://docs.docker.com/docker-for-windows/install/
      
       Docker for Mac: https://docs.docker.com/docker-for-mac/install/
      
       Docker for Ubuntu: https://docs.docker.com/engine/install/ubuntu/
      
       Docker for Debian: https://docs.docker.com/engine/install/debian/
      
       Docker for CentOS: https://docs.docker.com/engine/install/centos/

2. Clone the project from GitHub:

       git clone https://github.com/olivermclane/TOGAR

3. Navigate to the root directory of the project (Should already be there if you cloned it).
4. Build the Docker image by running the following command: **docker build -t togardatabase -f docker/dockerfile.yaml
   .**
5. Start a Docker container from the image with the following command: **docker run --name togarpostgres -p 5432:5432 -d
   togardatabase**
6. Verify that the container is running by running the following command: **docker ps**
7. You've created the database and can move on to running the application.

### Step-By-Step Guide for Running Application

1. Navigate to the root directory of the project.
2. In the terminal prompt run: **./gradlew bootrun**
3. Open up your preferred browser and navigate to **http://localhost:8080/**
4. Enjoy the application!

### Few Notes
1. The current configuration has the java version set to 20 if you aren't running the latest java make sure you update your version in the build.gradle file.

## Application Breakdown

### Viewing images at directory view

1. Each user is assigned a Unique ID Number
2. In the file-tree of the project, we can see the directory **/images**, double click it!
3. In the images directory, you will see each user has been created by the associated UID.
4. In each directory (UID), you will see the images they have uploaded.

### Running application tests

1. Navigate to the root directory of the project.
2. In a terminal prompt run: **./gradlew test**
3. The terminal window will run threw all the tests located in our testing folder.
4. Afterwards, it will tell you whether the test pass or failed, marked by a **Build Faild or Build Successful**.

### Viewing Documentation

1. Navigate to the Docs folder
2. Open the **index.html** in your preferred browser.
3. View/Search the java documentation.

### Viewing Database 

1. Open a terminal prompt.
2. Type **docker ps** and grab the container_id.
3. Type **docker exec -it {container_id} bash**
4. Type **psql -U postgres -d TogarData -h localhost**
5. Dig around the database:

       a. Type \dt to view avaliable tables.
       
       b. Select * from user_image; to view images saved by all users.
   
       c. Select * from logindata; to view user instances


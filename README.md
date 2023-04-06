# TOGAR
Togar is an exerimental web app that allows you to login via username and upload and delete certian images from within a Database. All of this is displayed via HTML and CSS with the images currently stored on (MariaDB). 

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
  4. In each directory(UID), you will see the images they have uploaded.
  
  ### Running application tests
  1. Navigate to the root directoyr of the project.
  2. In a terminal prompt run: **./gradlew test**
  3. The terminal window will run threw all the tests located in our testing folder.
  4. Afterwards, it will tell you whether the test pass or failed, marked by a **<span style="color:red;">Build Failded<span>** or  **<span style="color:green;">Build Successful<span>**


# TOGAR
Togar is an exerimental web app that allows you to login via username and upload and delete certian images from within a Database. All of this is displayed via HTML and CSS with the images currently stored on (MariaDB). 

Application Setup
Step-By-Step Guide for Database
1.	Install Docker on your computer if you haven't already.
2.	Clone the project from GitHub.
3.	Open a terminal or command prompt and navigate to the root directory of the project.
4.	Build the Docker image by running the following command: docker build -t togardatabase -f docker/dockerfile.yaml .
5.	Start a Docker container from the image with the following command: docker run --name togarpostgres -p 5432:5432 -d togardatabase
6.	Verify that the container is running by running the following command: docker ps

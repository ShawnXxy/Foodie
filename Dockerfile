FROM tomcat:9
 
# Copy files from local disk to image
COPY Foodie.war /usr/local/tomcat/webapps/
 

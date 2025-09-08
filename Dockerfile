# Use Tomcat 10 base image with JDK 17
FROM tomcat:10.1-jdk17

# Remove default ROOT app
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy your WAR into Tomcat
COPY build/electricity_db.war /usr/local/tomcat/webapps/ROOT.war

# Expose port 8080 (Railway will map it automatically)
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]

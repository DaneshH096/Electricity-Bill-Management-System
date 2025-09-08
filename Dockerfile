# Use Tomcat 10 with JDK 17
FROM tomcat:10.1-jdk17

# Remove default ROOT app
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy your WAR file into Tomcat and rename as ROOT.war
COPY build/ElectricityBill.war /usr/local/tomcat/webapps/ROOT.war

# Expose port 8080 (Railway maps it automatically)
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]

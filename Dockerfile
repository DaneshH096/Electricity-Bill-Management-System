# Use Tomcat 10 with JDK 17
FROM tomcat:10.1-jdk17

# Remove default apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR from Maven target folder and rename to ROOT.war
COPY build/ElectricityBill.war /usr/local/tomcat/webapps/ROOT.war

# EXPOSE is purely informational in Railway, but good practice
EXPOSE 8080

# 🔥 Fix: Run the sed command at STARTUP, not during BUILD
CMD ["sh", "-c", "sed -i \"s/8080/$PORT/g\" /usr/local/tomcat/conf/server.xml && catalina.sh run"]

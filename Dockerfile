# Use Tomcat 10 with JDK 17
FROM tomcat:10.1-jdk17

# Railway provides PORT dynamically
ENV PORT=8080

# Remove default apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR from Maven target folder and rename to ROOT.war
COPY build/ElectricityBill.war /usr/local/tomcat/webapps/ROOT.war

# 🔥 Make Tomcat listen on Railway's PORT
RUN sed -i 's/port="8080"/port="${PORT}"/' /usr/local/tomcat/conf/server.xml

# Expose dynamic port
EXPOSE ${PORT}

# Start Tomcat
CMD ["catalina.sh", "run"]

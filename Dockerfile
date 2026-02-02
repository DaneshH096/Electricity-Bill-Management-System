FROM tomcat:10.1-jdk17

# Railway injects PORT dynamically
ENV PORT=8080

# Remove default apps (clean)
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy your WAR as ROOT
COPY target/ROOT.war /usr/local/tomcat/webapps/ROOT.war

# 🔥 Replace Tomcat connector port with Railway PORT
RUN sed -i 's/port="8080"/port="${PORT}"/' /usr/local/tomcat/conf/server.xml

# Expose the dynamic port
EXPOSE ${PORT}

# Start Tomcat
CMD ["catalina.sh", "run"]

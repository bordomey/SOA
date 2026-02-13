# Use Tomcat 9 with OpenJDK 17
FROM tomcat:9.0-jdk17

# Set environment variables
ENV CATALINA_OPTS="-Xms512m -Xmx1024m -server -Djava.awt.headless=true"

# Remove default webapps
RUN rm -rf $CATALINA_HOME/webapps/*

# Copy the WAR file
COPY ./target/movie-service.war $CATALINA_HOME/webapps/ROOT.war

# Expose ports
EXPOSE 8080 844

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/ || exit 1

# Start Tomcat
CMD ["catalina.sh", "run"]
FROM payara/micro:5.2022.2-jdk11

# Copy the WAR file to the Payara micro directory
COPY target/oscar-service.war $DEPLOY_DIR

# Expose HTTPS port
EXPOSE 8282

# Set SSL configuration
ENV JAVA_OPTS="-Dserver.ssl.enabled=true -Dserver.ssl.key-store=/opt/payara/config/keystore.jks -Dserver.ssl.key-store-password=changeit -Dserver.ssl.key-password=changeit -Dserver.ssl.key-alias=oscarservice"

# Copy keystore
COPY src/main/resources/keystore.jks /opt/payara/config/

# Run Payara Micro with the deployed application
CMD ["--deploymentDir", "/opt/payara/deployments", "--sslPort", "8282", "--contextroot", "oscar-service"]
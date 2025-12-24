FROM payara/server-full:5.2022.2

WORKDIR /opt/payara

# Скопировать EAR и keystore
COPY oscar-ear/target/oscar-ear-1.0.0.ear /opt/payara/appserver/glassfish/domains/domain1/autodeploy/oscar-ear.ear
COPY keystore.jks /opt/payara/appserver/glassfish/domains/domain1/config/keystore.jks

# Открыть порты: HTTP, HTTPS, IIOP
EXPOSE 8080 8181 3700


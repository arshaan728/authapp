FROM openjdk:26-ea-21-jdk-slim

WORKDIR /authapp
COPY /target/authapp-0.0.1-SNAPSHOT.war login.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar" ,"login.war"]
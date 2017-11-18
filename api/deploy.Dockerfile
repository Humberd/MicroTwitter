# this Dockerfile is responsible for hosting api
FROM openjdk:8-jdk-alpine
COPY target/micro-twitter-api*.jar micro-twitter-api.jar
ENV JAVA_OPTS="-Dspring.profiles.active=production"

EXPOSE 8080

ENTRYPOINT exec java $JAVA_OPTS -jar micro-twitter-api.jar
# this Dockerfile is responsible for hosting api
FROM openjdk:8-jdk-alpine
VOLUME /tmp
RUN pwd
RUN ls tmp
RUN ls api
COPY api/target/microtwitter*.jar microtwitter.jar
ENV JAVA_OPTS="-Dspring.profiles.active=production"

EXPOSE 8080

ENTRYPOINT exec java $JAVA_OPTS -jar microtwitter.jar
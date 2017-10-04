FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ui/target/microtwitter*.jar microtwitter.jar
ENV JAVA_OPTS=""

EXPOSE 8080

ENTRYPOINT exec java $JAVA_OPTS -jar microtwitter.jar
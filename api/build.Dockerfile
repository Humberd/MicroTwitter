FROM maven:3.5.2-alpine

RUN mvn clean install -DskipTests=true

#VOLUME /media
#
#WORKDIR /mvn-app
#
#COPY pom.xml .
#RUN mvn dependency:resolve
#
#COPY . .
#RUN mvn install -DskipTests=true
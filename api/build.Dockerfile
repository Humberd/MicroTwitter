FROM maven:3.5.2-alpine

WORKDIR /mvn-app

COPY pom.xml .
RUN mvn dependency:resolve

COPY . .
RUN mvn install -DskipTests=true
FROM maven:3.5.2-alpine

VOLUME "~/.m2"

WORKDIR /mvn-app

COPY . .

RUN mvn clean install -DskipTests=true

RUN ls /mvn-app && ls /mvn-app/target

# API

## Requirements:

 * Maven
 * Docker

## Installation

### Database

Database is hosted in a Docker Container (Virtual Machine), which means it is completely isolated from host OS.

To install a docker container with `postgres` run this:
```shell
docker run --name micro-twitter-postgres -d -e POSTGRES_PASSWORD=admin123 -p "5432:5432" postgres:10-alpine
```

And we need a separate database just for testing purposes
```shell
docker run --name micro-twitter-postgres-testing -d -e POSTGRES_PASSWORD=admin123 -p "5435:5432" postgres:10-alpine
```

## Run

### Database

You need to make sure your database is up and running:

**Keep in mind** that each time you shut down your OS, your docker container with `postgres` shuts down. You need to start it up again.

Run the following command to start your `postgres` docker container:
```shell
docker start micro-twitter-postgres
```

And respectively to the testing db
```shell
docker start micro-twitter-postgres-testing
```

### Server

Run the following command inside `/api` directory to install all of the dependencies and start a development server:

```shell
mvn spring-boot:run
```

## Tests

Run the following commant inside `/api` directory to run all the tests:

```shell
mvn test
```

### Or just use [IntelliJ IDEA](https://www.jetbrains.com/idea/)

# Helpful links (dont remove)

https://medium.com/@crsandeep/creating-and-testing-a-kotlin-restful-web-services-using-spring-boot-1a11aeda279e
http://javasampleapproach.com/spring-framework/spring-data/kotlin-springjpa-hibernate-one-many-relationship
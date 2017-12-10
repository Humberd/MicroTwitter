# MicroTwitter

The social network website highly influenced by Twitter. For educational purposes only.

## Links

| Service | URI |
|---|---|
| UI | https://micro-twitter.quazarus.com |
| Api | https://api.micro-twitter.quazarus.com |
| Api Swagger | https://api.micro-twitter.quazarus.com/swagger-ui.html |

## Features
 * Separation between Authenticated and Anonymous user.
 * Login/SignUp
 * Creating Tweets (short messages).
 * Deleting Tweets.
 * Viewing Tweets of a specific user.
 * Searching users by `username` of `fullName`
 * Following users (user x that follows user y is able to see user's y tweets on his wall)
 * Unfollowing users
 * Editing own profile
 * Viewing profiles
 * Viewing followers (users that follow given user)
 * Viewing following (users that given user follow)
 * Viewing Tweets that user like
 * Liking Tweets
 * Unliking Tweets
 * Commenting a Tweet
 * Viewing comments of a Tweet
 * Changing a profile color, so that other users that come to profile would see everything in a color the profile owner chose.
 * Sharing a Tweet by a unique url.

## Technical details

Backend and UI are built, tested and deployed on git push to master branch.

To authorize users there was used a JWT, which should be present in an HTTP request header with a structure of: `Authorization: Bearer <JWT>`

### Backend

Backend project is located under `./api`.

It exposes a standard REST API written in Kotlin using a Spring Boot framework.
Data is stored in PostgreSQL database that is accessed using Hibernate ORM framework.
There are also about 70 e2e tests targetting all of the REST API endpoints.

The REST API Swagger can be accessed here: [https://api.micro-twitter.quazarus.com/swagger-ui.html](https://api.micro-twitter.quazarus.com/swagger-ui.html)

#### Technological stack
 * Kotlin:1.2
 * Spring Boot:2.0.0-M4
 * Hibernate:5.2.11.Final
 * PostgreSQL:10

### Frontend

Frontend project is located under `./ui`.

It is a Web Application written in Angular framework using Typescript with a help of RxJS library.
Its core design is highly influenced by Twitter, but common components like buttons, dialogs, tabs, etc.
were created using Material Design theme thanks to the Angular Material library.

#### Technological stack
 * Typescript:2.6.1
 * Angular:5.1.0
 * RxJS:5.5.5

### DevOps

Jenkins was used to automate build and deployment.
In a `Jenkinsfile` located in `./` there are defined 3 main stages: Build, Test and Deploy.

Docker was used to contenerize and isolate Backend, Frontend and Database from eachother.
In a `production.deploy.docker-compose.yml` located in `./` there are defined production
container definitions for Backend, Frontend and Database.

Backend and Frontend are secured by SSL thanks to [Let's Encrypt](https://letsencrypt.org/).

#### Technological stack
 * Docker:17.09.1-ce
 * Docker Compose:1.16.1
 * Jenkins:2.86

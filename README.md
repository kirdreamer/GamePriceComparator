# Game Price Comparator

RESTful backend service responsible for comparing prices of searched games on the main gaming platforms like Steam, Epic Game Store and GOG. For authenticated users there is also an opportunity to add a game to favorites and receive an email notification if this game is sold on one or more platforms at a discount.

## Technologies
- Spring Frameworks such as SpringBoot and SpringSecurity
- SpringBootMail for sending messages to users
- Mapstruct for automatic generation of class implementation for data-mapping
- JWT for generating JWT tokens by an authenticated user
- Jackson for processing JSON responses from gaming platforms

## Tests
Tests are written using JUnit, Testcontainers and Mockito.

## Database
PostgreSQL

## CI/CD
This project also has a CI/CD pipeline using Github Actions technology. After passing Build-Test process, project image pushes to Dockerhub.

[Code of the pipeline](https://github.com/kirdreamer/GamePriceComparator/blob/main/.github/workflows/build-and-test-app.yaml)

## Build-And-Test Requirements
To build this project, the following steps must be completed:

1. To use environment variables in application.properties file you need to use .env.template as a template
    1. Write your URL and credentials to connect database for PostgresDB. By default, url is "localhost" and port is "5432".
    2. Add your gmail in MAIL_PROVIDER_USERNAME and App-password from google in MAIL_PROVIDER_PASSWORD: Google-Account -> Security -> 2-Factor Authentication -> App Passwords. To use a different mail provider, you must change the host in spring.mail.host
    3. Add secret key for jwt. To generate this key you can use such sites like [www.browserling.com](https://www.browserling.com/tools/random-hex)
2. Use the following command to build and test project with maven from the root of the project:
``` bash
> maven clean package
```
3. Use the following command to build and start the application with docker-compose from the root of the project:
``` bash
> docker-compose -f docker-compose.yaml up
```

## Future development of the project
* Cloud deployment
* Using Kubernetes
* Monitoring

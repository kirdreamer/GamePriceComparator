# Game Price Comparator

RESTful backend service responsible for comparing prices of searched games on the main gaming platforms like Steam, Epic Game Store and GOG. For authenticated users there is also an opportunity to add a game to favorites and receive an email notification if this game is sold on one or more platforms at a discount.

## Frontend

[→ Frontend link](https://github.com/derReiskanzler/fe-angular-game-price-comparator)

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

## Cross-Origin Resource Sharing (CORS)

This project uses CORS for the safety communication between Frontend and Backend. To be able to use Postman locally you need to use in .env next value: `FRONTEND_URL=http://localhost`

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
# Or
> docker compose -f docker-compose.yaml up
```
4. To build docker image and start the container you need to use following commands:
``` bash
> docker build -t game-price-comparator-app .
> docker run --env-file .env -p 8080:8080 game-price-comparator-app
```

## Kubernetes

This project is supported for Kubernetes(K8s). 

### Important guides
- [Basics of K8s](https://kubernetes.io/docs/tutorials/kubernetes-basics/).
- [Basics of Kubectl](https://kubernetes.io/docs/reference/kubectl/).
- [Basics of minikube](https://kubernetes.io/docs/tutorials/hello-minikube/).


### How to start service with Kubernetes

0. In folder `kubernetes/` can be found next yaml-files:
   1. [postgres.yaml](kubernetes%2Fpostgres.yaml) - contains PersistentVolumeClaim, Deployment and Service for postgres. 
   2. [postgres-config.yaml](kubernetes%2Fpostgres-config.yaml) - contains Configuration for postgres
   3. [postgres-secret.yaml.template](kubernetes%2Fpostgres-secret.yaml.template) - Secrets template for postgres. Requires updating credentials.
   4. [backend.yaml](kubernetes%2Fbackend.yaml) - contains Deployment and Service (LoadBalancer) for backend service
   5. [ingress.yaml](kubernetes%2Fingress.yaml) - contains Ingress for backend service


1. [Download](https://minikube.sigs.k8s.io/docs/start/?arch=%2Fmacos%2Farm64%2Fstable%2Fbinary+download) minikube and start using next command:
``` bash
> minikube start \
    --cpus=2 --memory=3920m \
    --container-runtime=cri-o \
    --driver=docker \
    --addons=ingress
```

2. Create Namespace for the current project by using command 
``` bash
> kubectl create namespace backend
```
Change current context by using next command:
``` bash
> kubectl config set-context --current --namespace=backend
```
3. Apply Configurations and Secrets using next commands:
``` bash
> kubectl apply -n backend --filename ./postgres-config.yaml
# After creating yaml-file with your credentials
> kubectl apply -n backend --filename ./postgres-secret.yaml
# To be able to use secrets from .env you need to create them
> kubectl create secret generic game-price-comparator-secret -n backend --from-env-file=.env
```

4. Start Postgres and Backend using next commands:

``` bash
> kubectl apply -n backend --filename ./postgres.yaml
> kubectl apply -n backend --filename ./backend.yaml
```

5. To access the service, you need to get the ip from minikube using the `minikube ip` command and put it in `ingress.yaml` instead of `<minikube-ip>`. After that use next command:
``` bash
> kubectl apply -n backend --filename ./ingress.yaml
```

6. For MacOS with m1, m2 and m3 processors [Guide](https://kubernetes.io/docs/tasks/access-application-cluster/ingress-minikube/#create-an-ingress):
``` bash
# Terminal 1
> minikube tunnel
# Terminal 2
> minikube service game-price-comparator-service --url
# Terminal 3 
> curl http://127.0.0.1:53999/api/v1/auth/register -d '{
    "email":"Example@examp.le",
    "nickname":"exAmple",
    "password":"example"
}'
```

7. To stop service and delete everything use next command:
``` bash
> kubectl delete --all backend
# Or
> kubectl delete --all ingresses -n backend
> kubectl delete --all services -n backend
> kubectl delete --all deployments -n backend
> kubectl delete --all secrets -n backend
> kubectl delete --all configmaps -n backend
```


## Future development of the project
* Cloud deployment
* Monitoring

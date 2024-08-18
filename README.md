# ProductsService

It is an API that performs CRUD operations on a database and deploy it on a Kubernetes cluster.

## Technologies Used
This project utilizes the following technologies:
* Spring Boot 3.3.2
* Spring Data JPA
* PostgreSQL 15
* Maven 3.3.2
* Java 21
* JUnit 5
* OpenAPI 3
* Docker
* Kubernetes
* [KinD](https://kind.sigs.k8s.io/) ->  kind is a tool for running local Kubernetes clusters using Docker container “nodes”.
* [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl-macos/#install-with-homebrew-on-macos) -> The Kubernetes command-line tool, kubectl, allows you to run commands against Kubernetes clusters.
* [REST Assured](https://rest-assured.io/) and [Testcontainers](https://testcontainers.com/) (for Spring integration tests using a container)

## Project Structure
Project has number of files/folders, below is the explanation for each of them:

1. **src** -> source code of the ProductService.
2. **k8s** -> kubernetes configuration manifest files.
3. **kind** -> kubernetes cluster files to create and destroy cluster locally.
4. **postman** -> contains postman collection json file to test endpoint via Postman.
5. **DockerFile** -> to create a docker image of ProductAPI.
6. **docker-compose.yml** -> compose file to create docker container of database.
7. **docker-compose-app.yml** -> compose file to create docker container of application.
8. **run.sh** -> file containing different handy commands to start/stop application and dependency services.

## Swagger Documentation
Swagger docs will be available at : http://localhost:8080/swagger-ui/index.html
You can also download Json & Yaml file from http://localhost:8080/v3/api-docs and http://localhost:8080/v3/api-docs.yaml respectively.

## How to Run the Project in Local

1. Clone the repository:
   ```sh
   git clone https://github.com/hitendra1908/freelancer-service.git

2. To run the application - Navigate to the root directory and execute below command:
   ```sh
   sh run.sh start

3. To stop the application:
   ```sh
   sh run.sh stop

## How to Access API in Local

Access API using http://localhost:8080/api/products

## Test Endpoints via Postman
Postman collection is available at "postman" folder to test endpoints via Postman. It has 2 configurations one to test endpoints on local setup and second for kubernetes cluster.

## How to Run the Project for Development or to Debug

During development, we need database to run the application, for that we can run database in a container and then can run the application normally.

1. To start only dependent services(DB container):
   ```sh
   sh run.sh start_infra

2. Build the application:
   ```sh
   sh run.sh build_api
   
3. Run the Spring Boot application:
   ```sh
   mvn spring-boot:run

4. To stop the dependent services:
   ```sh
   sh run.sh stop_infra


## How to Run the Project on Kubernetes

### Prerequisite:
* [Docker](https://www.docker.com/) -> [install docker](https://docs.docker.com/get-docker/)
* [KinD](https://kind.sigs.k8s.io/) ->  to create Kubernetes clusters locally.
* [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl-macos/#install-with-homebrew-on-macos) -> The Kubernetes command-line tool, kubectl, allows you to run commands against Kubernetes clusters.
* [lens](https://k8slens.dev/) (Optional) -> good to have tool to get insights of the KinD cluster

#### Note:
kind does not require kubectl, but you will not be able to perform some of the commands which are used below.

1. Build the application:
   ```sh
   sh run.sh build_api
   
2. Make Docker image: command ends with dot(.) & do not create latest tag, as we can't load latest tag in kind cluster, because if tag is latest, it always tries to fetch image from repository.
   ```sh
   docker build -t products-app:1.0.0 .
   
3. Navigate to kind folder:
   ```sh
   cd kind
   
4. Create kubernetes cluster locally using kind, for that execute below command:
   ```sh
   sh create-cluster.sh
   
5. Load local docker image created in step 2, inside the k8s cluster which we just created:
   ```sh
   kind load docker-image products-app:1.0.0 -n products-k8s
   
6. Navigate to k8s folder and then apply all the k8s manifest files by below command (ends with dot), it will run all the file in the current folder
   ```sh
   kubectl apply -f .

7. Check status of cluster:
   ```sh
   kubectl get all
   
8. To check the logs of running pod (pod name can be found from the output of above command)
   ```sh
   kubectl logs {pod name}
   
9. To scale up number of pods:
   ```sh
   kubectl scale deployment products-api-deployment --replicas=2
   
10. To delete all manifest:
    ```sh
    kubectl delete -f .
   
11. To remove the KinD cluster, move to kind folder and then execute below command:
    ```sh
    sh destroy-cluster.sh

## How to Access API on Kubernetes

Access API using NodePort http://localhost:18080/api/products

## Kubernetes Useful commands

### Pods

```shell
kubectl get pods
kubectl get all
kubectl logs pods {podname}
kubectl describe pods {podname}
kubectl delete pods {podname}
kubectl apply -f 3-products-api.yaml  --> to run the pod

```

### Deployments

```shell
kubectl apply -f deployment-api.yaml --> to start deployment
kubectl delete -f deployment-api.yaml
```
### Horizontal scaling of Pod

```shell
kubectl scale deployment products-api-deployment --replicas=2
```
### Services

* **ClusterIP**:  Exposes the Service on a cluster-internal IP. Only reachable from within the cluster.
* **NodePort**: Exposes the Service on each node's IP address at a static port. Accessible from outside the cluster.

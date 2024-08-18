
------- With DockerFile_____
docker build -t products-app:1.0.0 .
docker run -p 8080:8080 products-app

----
docker compose up -> to run project
docker compose up -d -> to build and run docker in detached mode
docker compose stop -> to stop the docker container
docker compose rm -> to remove docker container

## How to run?

```shell
$ git clone https://github.com/hitendra1908/productapi.git
$ ./run.sh start
$ ./run.sh stop

$ ./run.sh start_infra
$ ./run.sh stop_infra
```

* To start only dependent services

```shell
$ ./run.sh start_infra
$ ./run.sh stop_infra
```
-------------------
1. KinD -> https://kind.sigs.k8s.io/
2. kubectl -> https://kubernetes.io/docs/tasks/tools/install-kubectl-macos/#install-with-homebrew-on-macos
3. lens tool -> https://k8slens.dev/  --> need lens id, so can skip this


4. load local docker image--> kind load docker-image products-app:1.0.0 -n products-k8s
kubectl run products-api --image=products-app:1.0.0 --port=8080

kubectl get pods
kubectl get all

kubectl logs pods products-api

kubectl delete pods products-api

kubectl apply -f 3-products-api.yaml  --> to run the pod

kubectl apply -f deployment-api.yaml --> to start deployment, then we don't need to run pod
kubectl delete -f deployment-api.yaml

kubectl scale deployment products-api --replicas=2  ---> to scale up pods

kubectl apply -f . -->  (with dot) is to run all files in current folder
kubectl get all

-------to run with Kubernetes-----
1. make build
2. make docker image --> docker build -t products-app:1.0.0 .(dont forget dot(.)) & do not create latest tag, as we can'nt load latest tag in kind cluster, because if tag is latest, it always try to fetch image from repository 
3. /kind folder run create cluster 
4. add load local docker image whihc we created in step 2 inside the cluster which we created in step 3 by -> kind load docker-image products-app:1.0.0 -n products-k8s
5. apply all the kubernetes file by -> kubectl apply -f . -->  (with dot) is to run all files in current folder
6. check status by -> kubectl get all
7. check logs by -> kubectl logs {pod name}
8. hit -> http://localhost:18080/api/products
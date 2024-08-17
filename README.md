
------- With DockerFile_____
docker build -t products-app .
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

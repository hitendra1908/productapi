#!/bin/bash

declare dc_infra=docker-compose.yml
declare dc_app=docker-compose-app.yml

#build spring-boot API
function build_api() {
    ./mvnw clean package -DskipTests
}

#Starting the dependencies needed for the API for eg, DB
function start_infra() {
    echo "Starting infra docker containers...."
    docker compose -f ${dc_infra} up -d
    docker compose -f ${dc_infra} logs -f
}

#Stopping the dependencies of the API for eg, DB
function stop_infra() {
    echo "Stopping infra docker containers...."
    docker compose -f ${dc_infra} stop
    docker compose -f ${dc_infra} rm -f
}

#Staring/Running the API with its dependencies
function start() {
    build_api
    echo "Starting all docker containers...."
    docker compose -f ${dc_infra} -f ${dc_app} up --build -d
    docker compose -f ${dc_infra} -f ${dc_app} logs -f
}

#Stopping the API with its dependencies
function stop() {
    echo "Stopping all docker containers...."
    docker compose -f ${dc_infra} -f ${dc_app} stop
    docker compose -f ${dc_infra} -f ${dc_app} rm -f
}

#Function to restart the API
function restart() {
    stop
    sleep 3
    start
}

action="start"

if [[ "$#" != "0"  ]]
then
    action=$@
fi

eval ${action}
#!/bin/sh

echo "Initializing Kubernetes cluster..."

kind create cluster --config kind-config.yml

echo "\n-----------------------------------------------------\n"

echo "Successfully Created Kubernetes cluster..."
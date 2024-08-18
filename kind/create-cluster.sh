#!/bin/sh

echo "Initializing Kubernetes cluster..."

kind create cluster --config kind-config.yml

echo "Successfully Created Kubernetes cluster..."
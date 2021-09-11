#!/bin/bash

app_name=$1
image=$2
namespace=$3

deploy=`oc get deployment $app_name`
if [[ "$?" -eq 0 ]]; then
    oc set image deployment/$app_name $app_name=$image
    oc rollout restart deployment/$app_name
else
    oc new-app $image --name $app_name -n $namespace
fi

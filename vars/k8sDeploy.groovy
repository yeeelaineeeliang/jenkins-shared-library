def call(Map config = [:]) {
    def service   = config.service    ?: error("k8sDeploy: 'service' is required")
    def namespace = config.namespace  ?: 'dev'
    def image     = config.image      ?: env.DOCKER_IMAGE ?: error("k8sDeploy: 'image' is required")

    echo "KUBERNETES DEPLOY: ${service} - namespace=${namespace} image=${image}"

    sh """
        sed -i "s|IMAGE_PLACEHOLDER|${image}|g" k8s/deployment.yaml

        kubectl apply -f k8s/configmap.yaml      -n ${namespace}
        kubectl apply -f k8s/secret.yaml         -n ${namespace}
        kubectl apply -f k8s/deployment.yaml     -n ${namespace}
        kubectl apply -f k8s/service-clusterip.yaml -n ${namespace}
        kubectl apply -f k8s/service-nodeport.yaml  -n ${namespace}

        kubectl rollout status deployment/${service} -n ${namespace} --timeout=60s

        kubectl get pods -n ${namespace} -l app=${service}
    """

    echo "Deployment complete: ${service} in ${namespace}"
}
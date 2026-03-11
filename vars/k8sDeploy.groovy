def call(Map config) {
    def namespace  = config.namespace
    def service    = config.service
    def image      = config.image
    def environment = namespace 

    sh """
        sed "s|IMAGE_PLACEHOLDER|${image}|g" k8s/base/deployment.yaml | \
            kubectl apply -f - -n ${namespace} --context=ecommerce-${environment}
        kubectl apply -f k8s/${environment}/ -n ${namespace} --context=ecommerce-${environment}
        kubectl rollout status deployment/${service} -n ${namespace} \
            --context=ecommerce-${environment} --timeout=300s
    """
}
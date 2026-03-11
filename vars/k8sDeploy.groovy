def call(String namespace, String environment, String imageName) {
    echo "Deploying to namespace: ${namespace}, environment: ${environment}"
    
    sh """
        sed -i "s|IMAGE_PLACEHOLDER|${imageName}|g" k8s/base/deployment.yaml
        
        kubectl apply -f k8s/base/ -n ${namespace} --context=ecommerce-${environment}
        
        kubectl apply -f k8s/${environment}/ -n ${namespace} --context=ecommerce-${environment}
        
        kubectl rollout status deployment/\$(basename \$(pwd)) -n ${namespace} --context=ecommerce-${environment} --timeout=120s
        
        sed -i "s|${imageName}|IMAGE_PLACEHOLDER|g" k8s/base/deployment.yaml
    """
}
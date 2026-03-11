def call(Map config = [:]) {
    def namespace = config.namespace ?: 'monitoring'

    echo "MONITORING DEPLOY: Prometheus + Grafana in namespace=${namespace}"

    sh """
        kubectl get namespace ${namespace} --context=ecommerce-dev || \
            kubectl create namespace ${namespace} --context=ecommerce-dev

        helm repo add prometheus-community https://prometheus-community.github.io/helm-charts || true
        helm repo update

        helm upgrade --install prometheus prometheus-community/kube-prometheus-stack \
            --namespace ${namespace} \
            --kube-context ecommerce-dev \
            --set grafana.adminPassword=admin123 \
            --set prometheus.prometheusSpec.scrapeInterval=15s \
            --set grafana.resources.requests.memory=128Mi \
            --set grafana.resources.limits.memory=256Mi \
            --timeout=600s \
            --wait

        echo "Monitoring stack deployed"
        kubectl get pods -n ${namespace} --context=ecommerce-dev
    """
}
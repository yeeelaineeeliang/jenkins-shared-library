def call(Map config = [:]) {
    def namespace = config.namespace ?: 'monitoring'

    echo "MONITORING DEPLOY: Prometheus + Grafana in namespace=${namespace}"

    sh """
        kubectl get namespace ${namespace} || kubectl create namespace ${namespace}

        helm repo add prometheus-community https://prometheus-community.github.io/helm-charts || true
        helm repo update

        helm upgrade --install prometheus prometheus-community/kube-prometheus-stack \
            --namespace ${namespace} \
            --set grafana.adminPassword=admin123 \
            --set prometheus.prometheusSpec.scrapeInterval=15s \
            --wait --timeout=120s

        echo "Monitoring stack deployed"
        kubectl get pods -n ${namespace}
    """
}
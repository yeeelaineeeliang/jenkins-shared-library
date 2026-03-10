def call(Map config = [:]) {
    def service   = config.service   ?: 'unknown-service'
    def image     = config.image     ?: ''
    def severity  = config.severity  ?: 'HIGH,CRITICAL'

    echo "SECURITY SCAN STAGE: ${service}"

    // Trivy
    if (image) {
        echo "Running Trivy image scan on: ${image}"
        sh """
            trivy image \
                --exit-code 1 \
                --severity ${severity} \
                --no-progress \
                --format table \
                --output trivy-report-${service}.txt \
                ${image} || true

            echo "Trivy Scan Summary"
            cat trivy-report-${service}.txt
        """
        archiveArtifacts artifacts: "trivy-report-${service}.txt", allowEmptyArchive: true
    }

    // Checkov
    echo "Running Checkov IaC scan on terraform/ directory"
    sh """
        if [ -d "terraform" ]; then
            checkov -d terraform \
                --quiet \
                --compact \
                --output cli \
                --output-file checkov-report-${service}.txt \
                --soft-fail || true

            echo "Checkov Scan Summary"
            cat checkov-report-${service}.txt
        else
            echo "No terraform/ directory found — skipping Checkov scan"
        fi
    """
    archiveArtifacts artifacts: "checkov-report-${service}.txt", allowEmptyArchive: true

    echo "Security scan stage complete"
}
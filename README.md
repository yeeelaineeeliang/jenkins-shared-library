# Phase 3 Jenkins Shared Library — ecommerce-pipeline

This library is for reusable pipeline steps for all four microservices.

## Jenkinsfile 

```groovy
@Library('ecommerce-shared-lib') _

pipeline {
    stages {
        stage('Build')    { steps { buildStage(service: 'product-service') } }
        stage('Test')     { steps { testStage(service: 'product-service') } }
        stage('Security') { steps { securityScanStage(service: 'product-service', image: "${DOCKER_IMAGE}") } }
    }
}
```
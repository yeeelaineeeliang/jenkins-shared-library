def call(Map config = [:]) {
    def service = config.service ?: 'unknown-service'

    echo "TEST STAGE: ${service}"

    sh '''
        npm test --if-present -- --ci --forceExit --detectOpenHandles 2>&1 | tee test-results.txt
        echo "Test stage complete"
    '''

    junit allowEmptyResults: true, testResults: '**/test-results/*.xml'
}
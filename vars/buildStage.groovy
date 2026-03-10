def call(Map config = [:]) {
    def service = config.service ?: 'unknown-service'

    echo "BUILD STAGE: ${service}"

    sh '''
        echo "Node version: $(node --version)"
        echo "NPM version: $(npm --version)"
        npm install
        npm run lint --if-present
        echo "Build stage complete"
    '''
}
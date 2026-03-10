def call(Map config = [:]) {
    def service      = config.service   ?: error("dockerBuildPush: 'service' is required")
    def tag          = config.tag       ?: 'latest'
    def registry     = config.registry  ?: 'yeelaine'
    def fullImage    = "${registry}/ecommerce-${service}:${tag}"
    def latestImage  = "${registry}/ecommerce-${service}:latest"

    echo "DOCKER BUILD and PUSH: ${fullImage}"

    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials') {
        def img = docker.build(fullImage, "--no-cache .")

        echo "Pushing ${fullImage}"
        img.push(tag)

        echo "Pushing ${latestImage}"
        img.push('latest')
    }

    // export image name
    env.DOCKER_IMAGE = fullImage
    echo "Docker build and push complete. Image: ${fullImage}"
}
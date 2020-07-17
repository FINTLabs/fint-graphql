pipeline {
    agent { label 'docker' }
    parameters {
        string(name: 'MODEL_VERSION', description: 'Version of model to generate for')
    }
    stages {
        stage('Build') {
            steps {
                sh "docker build --tag ${GIT_COMMIT} --build-arg TAG_NAME=v${MODEL_VERSION} --build-arg VERSION=${MODEL_VERSION} ."
            }
        }
        stage('Publish') {
            when { branch 'master' }
            steps {
                withDockerRegistry([credentialsId: 'fintlabsacr.azurecr.io', url: 'https://fintlabsacr.azurecr.io']) {
                    sh "docker tag ${GIT_COMMIT} fintlabsacr.azurecr.io/graphql:${MODEL_VERSION}-${BUILD_NUMBER}"
                    sh "docker push fintlabsacr.azurecr.io/graphql:${MODEL_VERSION}-${BUILD_NUMBER}"
                }
            }
        }
        stage('Publish PR') {
            when { changeRequest() }
            steps {
                withDockerRegistry([credentialsId: 'fintlabsacr.azurecr.io', url: 'https://fintlabsacr.azurecr.io']) {
                    sh "docker tag ${GIT_COMMIT} fintlabsacr.azurecr.io/graphql:${BRANCH_NAME}.${BUILD_NUMBER}"
                    sh "docker push fintlabsacr.azurecr.io/graphql:${BRANCH_NAME}.${BUILD_NUMBER}"
                }
            }
        }
    }
}

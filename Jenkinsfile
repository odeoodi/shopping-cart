pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    environment {
        DOCKERHUB_CREDENTIALS_ID = 'docker_hub'
        DOCKERHUB_REPO = 'odeoodi/shopping_cart'
        DOCKER_IMAGE_TAG = 'latest'
    }



    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: ''
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Generate Report') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }

        stage('Publish Coverage Report') {
            steps {
                jacoco()
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
      export PATH="/usr/local/bin:/opt/homebrew/bin:$PATH"
      docker version
      docker build -t ${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG} .
    '''
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: DOCKERHUB_CREDENTIALS_ID,
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
        export PATH="/usr/local/bin:/opt/homebrew/bin:$PATH"
        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
        docker push ${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}
      '''
                }
            }



        }
    }}
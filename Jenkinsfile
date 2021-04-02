pipeline {
    agent {
        label 'spot'
    }
    environment {
        imageName = "lindynetech/calculator"
        registryCredential = 'dockerhub'
        dockerImage = ''
    }
    stages {
        stage('Compile') {
            steps {
                sh "./gradlew compileJava"      
            }
        }
        stage('Unit test') {
            steps {
              sh "./gradlew test" 
            }
        }
        stage("Code coverage") {
            steps {
                sh "./gradlew jacocoTestCoverageVerification"
            }
        } 
        stage("Static code analysis") {
            steps {
                sh "./gradlew checkstyleMain"
            }
        }
        stage('Package') {
            steps {
                sh './gradlew build'
            }
                          
        }
        stage('Docker build') {
            steps {
                sh 'docker build -t $imageName:${BUILD_TIMESTAMP} .'
            }
        }
        stage('Docker Login') {
            steps {
                 withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'docker-hub-credentials',
                    usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                    sh "docker login --username $USERNAME --password $PASSWORD"
                }
            }
        }
        stage('Docker push') {
            steps {
                sh 'docker push $imageName:${BUILD_TIMESTAMP}'            
            }
        }
        stage('Update Version') {
            steps {
                sh "sed -i 's/{{VERSION}}/${BUILD_TIMESTAMP}/g' calculator.yml"
            }
        }
        stage("Deploy to staging") {
            steps {
                sh "kubectl config use-context staging"
                sh "kubectl apply -f hazelcast.yaml"
                sh "kubectl apply -f calculator.yaml"
            }
        }
    }
}
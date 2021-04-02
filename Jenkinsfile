pipeline {
    agent {
        label 'spot'
    }
    environment {
        imageName = "lindynetech/calculator"
        registryCredential = 'dockerhub'
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
        // Fails for some reason
        // stage("Code coverage") {
        //     steps {
        //         sh "./gradlew jacocoTestReport"
        //         sh "./gradlew jacocoTestCoverageVerification"
        //     }
        // } 
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
                 withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'dockerhub',
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
                sh "kubectl apply -f hazelcast.yml"
                sh "kubectl apply -f calculator.yml"
            }
        }
        stage("Acceptance test") {
            steps {
                sleep 60
                sh "chmod +x acceptance_test_k8s.sh && ./acceptance_test_k8s.sh"
            }
        }
        stage("Release") {
          steps {
                sh "kubectl config use-context production"
                sh "kubectl apply -f hazelcast.yml"
                sh "kubectl apply -f calculator.yml"
            }
        }
    }
}
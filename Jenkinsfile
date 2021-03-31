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
                script {
                   dockerImage = docker.build imageName
                }
            }
        }
        stage('Docker push') {
            steps {
                script {
                    docker.withRegistry( '', registryCredential ) {
                        dockerImage.push()
                    }                
                }            
            }
        }
        stage("Deploy to staging") {
            steps {
                sh "docker run -d --rm -p 8765:8080 --name calculator $imageName"
            }
        }
        stage("Acceptance test Basic Curl") {
            steps {
                sleep 60
                sh "chmod +x acceptance_test.sh && ./acceptance_test.sh"
            }
        }
        stage("Acceptance test Cucumber") {
            steps {
                sleep 60
                sh "./gradlew acceptanceTest -Dcalculator.url=http://localhost:8765"
            }
        }
     
        post {
            always {
                sh "docker stop calculator"
                // sh "docker rmi $imageName"
            }
        }
    }
}
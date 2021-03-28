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
        // stage('Docker build') {
        //     steps {
        //         sh 'docker build -t lindynetech/calculator .'
        //     }
        // }
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
                // sh 'docker push $imageName'
            }
        }
        stage('Docker cleanup') {
            steps {
                sh "docker rmi $imageName"
            }
        } 
    }
}
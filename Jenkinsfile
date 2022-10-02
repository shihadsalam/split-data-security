pipeline {
    agent any

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "M3"
    }
    environment {
        DATE = new Date().format('yy.M')
        TAG = "${DATE}.${BUILD_NUMBER}"
    }
    stages {
        stage('Build') {
            steps {
                // Get some code from a GitHub repository
                git 'https://github.com/shihadsalam/split-data-security.git'

                // Run Maven on a Unix agent.
                sh "mvn -Dmaven.test.failure.ignore=true clean package"

                // To run Maven on a Windows agent, use
                // bat "mvn -Dmaven.test.failure.ignore=true clean package"
            }
            
            post {
                // If Maven was able to run the tests, even if some of the test
                // failed, record the test results and archive the jar file.
                success {
                    archiveArtifacts 'target/*.jar'
                }
            }
        }
        stage('Docker Build') {
            steps {
                script {
                    docker.build("shihadsalam/split-data-security:${TAG}", "./src/main/docker/")
                }
            }
        }
	    stage('Pushing Docker Image to Dockerhub') {
            steps {
                sh 'node --version'
                script {
                    docker.withRegistry('', 'dockerhub') {
                        docker.image("shihadsalam/split-data-security:${TAG}").push()
                        docker.image("shihadsalam/split-data-security:${TAG}").push("latest")
                    }
                }
            }
        }
    }
}

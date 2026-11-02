
pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    options {
        timestamps()
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out Snap2PDF source code'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Compiling Java application'
                bat 'mvn -B clean compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Running JUnit tests'
                bat 'mvn -B test'
            }
        }

        stage('Package') {
            steps {
                echo 'Generating executable JAR'
                bat 'mvn -B package'
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'target/snap2pdf-1.0-SNAPSHOT.jar',
                                 fingerprint: true
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true,
                  testResults: 'target/surefire-reports/*.xml'
        }

        success {
            echo 'Snap2PDF CI Pipeline completed successfully!'
        }

        failure {
            echo 'Snap2PDF CI Pipeline failed!'
        }
    }
}

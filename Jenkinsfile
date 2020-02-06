pipeline {
    agent any
    options { 
        disableConcurrentBuilds()
        timeout(time: 1, unit: 'HOURS') 
    }
    tools {
        maven 'Maven 3.6.2'
    }
        
    environment {
        SLACK_TOKEN = credentials('Slacktoken')
    }

    stages {
        stage('build') {
            steps {
                sh 'mvn compile'
            }
        }
        stage('test') {
            steps{
                copyArtifacts filter: 'gym/Linux/bin/**', fingerprintArtifacts: true, projectName: 'UnityPipeline/Develop'
                sh 'mvn test'
            }
        }
        stage('package') {
            steps{
                sh 'mvn package'
            }
        }
    }

    post {
        
        always {
            echo 'Stages complete..'
            junit 'target/surefire-reports/**/*.xml'
        }
        success {
            echo 'It ran succesfully' 
            slackSend (channel: '#jenkins-notifications', color: '#00FF00', message: "It ran succesfully! View results at: ${env.BUILD_URL} \n")
        }
        failure {
            echo 'There was an error'
            slackSend (channel: '#jenkins-notifications', color: '#FF0000', message: "It failed... View errors at: ${env.BUILD_URL}\n ")
        }
        
    }
}
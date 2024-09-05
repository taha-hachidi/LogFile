pipeline {
    agent {
        kubernetes {
            label 'jenkins-agent'
            yaml """
            apiVersion: v1
            kind: Pod
            spec:
              containers:
              - name: maven
                image: maven:3.8.4-jdk-11
                command:
                - cat
                tty: true
            """
        }
    }
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/taha-hachidi/LogFile'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Deploy') {
            steps {
                kubernetesDeploy(configs: 'kubernetes/*.yaml', kubeconfigId: '37')
            }
        }
    }
}

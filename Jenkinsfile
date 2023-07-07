pipeline {
  agent any
  tools { 
    maven 'jenkins-maven' 
    jdk 'Java 11' 
  }
  environment {
    DOCKER_TAG = sh(script: 'echo $BUILD_NUMBER', returnStdout: true).trim()
  }

  stages {
    stage('Checkout') {
      steps {
        checkout([
          $class: 'GitSCM', 
          branches: [[name: 'main']],
          userRemoteConfigs: [[url: 'git@github.com:ISeco/DevopsTestMerge.git']]
        ])
      }
    }
    stage('Clean Package') {
      steps {
        sh "mvn clean package"
      }
    }
    stage('Build Image') {
      steps {
        // Construir la imagen del Dockerfile con el tag semántico
        sh "docker build -t iseco/devopsmerge:${DOCKER_TAG} ."
      }
    } 
    stage('Push Image') {
      steps {
        // Iniciar sesión en Docker Hub
        withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKERHUB_PASSWORD', usernameVariable: 'DOCKERHUB_USERNAME')]) {
            sh "docker login -u $DOCKERHUB_USERNAME -p $DOCKERHUB_PASSWORD"
        }
        
        // Hacer push de la imagen con el tag semántico a Docker Hub
        sh "docker push iseco/devopsmerge:${DOCKER_TAG}"
      }
    }
    stage('Sonar Scanner') {
      steps {
        withSonarQubeEnv('SonarQube') { 
          sh 'mvn sonar:sonar -Dsonar.projectKey=GS -Dsonar.sources=src/main/java/com/kibernumacademy/miapp -Dsonar.tests=src/test/java/com/kibernumacademy/miapp -Dsonar.java.binaries=.'
        }
      }
    }
  }
}

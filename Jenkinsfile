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
    stage('Archive') {
      steps {
        archiveArtifacts artifacts: "**/target/*.jar", fingerprint: true
      }
    }    
    stage('Sonar Scanner') {
      steps {
        withSonarQubeEnv('SonarQube') { 
          sh 'mvn sonar:sonar -Dsonar.projectKey=GS -Dsonar.sources=src/main/java/com/kibernumacademy/devops -Dsonar.tests=src/test/java/com/kibernumacademy/devops -Dsonar.java.binaries=.'
        }
      }
    }
    stage('Quality Gate'){
      steps{
        timeout(time:1, unit:'HOURS'){
          waitForQualityGate abortPipeline:true
        }
      }
    }
    stage('Build Image') {
      steps {
        script {
          def gitCommitShort = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
          echo "Git SHA commit: ${gitCommitShort}"
          // Construir la imagen del Dockerfile con el SHA commit abreviado como tag
          sh "docker build -t iseco/devopsmerge:${gitCommitShort} ."
          sh "docker build -t iseco/devopsmerge:latest ."
        }
      }
    } 
    
    stage('Push Image') {
      steps {
        // Iniciar sesión en Docker Hub
        withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKERHUB_PASSWORD', usernameVariable: 'DOCKERHUB_USERNAME')]) {
          sh "docker login -u $DOCKERHUB_USERNAME -p '$DOCKERHUB_PASSWORD'"
        }
        // Obtener el SHA commit abreviado nuevamente en esta etapa para usarlo en el push
        script {
          def gitCommitShort = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim() 
          // Hacer push de la imagen con el SHA commit abreviado como tag a Docker Hub
          sh "docker push iseco/devopsmerge:${gitCommitShort}"
          sh "docker push iseco/devopsmerge:latest"
        }
      }
    }
  }
}

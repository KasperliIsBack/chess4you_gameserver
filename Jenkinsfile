pipeline {
  agent any
  stages {
    stage('gradle wrapper') {
      steps {
        sh '''cd gameserver
gradlew clean'''
      }
    }
  }
}
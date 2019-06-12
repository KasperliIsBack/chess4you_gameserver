pipeline {
  agent any
  stages {
    stage('gradle wrapper') {
      steps {
        sh './gameserver/gradlew wrapper'
      }
    }
    stage('gradle clean') {
      steps {
        sh './gameserver/gradlew clean'
      }
    }
  }
}
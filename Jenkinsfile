pipeline {
  agent any
  stages {
    stage('gradle clean') {
      steps {
        sh './gameserver/gradlew init'
      }
    }
  }
}
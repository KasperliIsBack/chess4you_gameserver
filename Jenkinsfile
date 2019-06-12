pipeline {
  agent any
  stages {
    stage('gradle clean') {
      steps {
        sh 'sh \'./gameserver/gradlew clean\''
      }
    }
  }
}
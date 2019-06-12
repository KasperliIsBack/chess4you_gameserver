pipeline {
  agent any
  stages {
    stage('open folder') {
      steps {
        dir(path: 'gamserver')
      }
    }
    stage('gradle clean') {
      steps {
        sh './gradlew clean'
      }
    }
    stage('gradle build') {
      steps {
        sh './gradlew build'
      }
    }
    stage('gradle code coverage') {
      steps {
        sh './gradlew jacocoTestReport'
      }
    }
    stage('gradle sonarqube') {
      steps {
        sh './gradlew sonarqube -Dsonar.projectKey=gameserver -Dsonar.organization=chess4you -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=d061ee7c6b7eaf4437ba17697abca4417d0f42ce -Dsonar.coverage.jacoco.xmlReportPaths=C:\\git\\chess4you_gameserver\\gameserver\\build\\code_coverage\\reports\\jacocoXml\\jacocoXml.xml'
      }
    }
  }
}
pipeline {
  agent any
  stages {
    stage('gradle clean') {
      steps {
        dir(path: 'gameserver') {
          sh './gradlew clean'
        }

      }
    }
    stage('gradle build') {
      steps {
        dir(path: 'gameserver') {
          sh './gradlew build -x test'
        }

      }
    }
    stage('gradle code coverage') {
      steps {
        dir(path: 'gameserver') {
          sh './gradlew jacocoTestReport'
        }

      }
    }
    stage('gradle sonarqube') {
      steps {
        dir(path: 'gameserver') {
          sh './gradlew sonarqube -x test -Dsonar.projectKey=gameserver -Dsonar.organization=chess4you -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=d061ee7c6b7eaf4437ba17697abca4417d0f42ce -Dsonar.coverage.jacoco.xmlReportPaths=C:\\git\\chess4you_gameserver\\gameserver\\build\\code_coverage\\reports\\jacocoXml\\jacocoXml.xml'
        }

      }
    }
    stage('docker stop') {
      steps {
        sh '[ -z $(docker ps -aqf "label=server=gameServer") ] || docker stop $(docker ps -aqf "label=server=gameServer")'
      }
    }
    stage('docker remove') {
      steps {
        sh '[ -z $(docker ps -aqf "label=server=gameServer") ] || docker rm $(docker ps -aqf "label=server=gameServer")'
      }
    }
    stage('gradle docker build') {
      steps {
        dir(path: 'lobbyserver') {
          sh './gradlew build docker -x test'
          sh 'docker ps -aqf "label=server=gameServer"'
        }

      }
    }
    stage('docker run') {
      steps {
        sh 'docker run -d -p 8082:8082 -t com.chess4you/gameserver'
      }
    }
  }
}
#!/usr/bin/env groovy

pipeline {
    agent any
    tools {
        maven 'Maven 3.5.2'
    }

    stages {
        // Mark the code checkout 'stage'....
        stage('Checkout') {
            steps {
                git url: 'https://github.com/billmeyer/MultiJavaSauce.git'
            }
        }

        stage('Build') {
            steps {
                sh "mvn compile"
            }
        }

        stage('Test') {
            steps {
                sauce('3c0dede2-4b25-439d-9765-cfcbcaea3925') {
                    sauceconnect(useGeneratedTunnelIdentifier: true, verboseLogging: true) {
                        sh "mvn test"
                    }
                }
            }

        }

        stage('Collect Results') {
            steps {
                step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
//                step([$class: 'SauceOnDemandTestPublisher'])
                saucePublisher()
            }
        }
    }
}
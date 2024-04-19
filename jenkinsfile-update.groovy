pipeline {

  agent { kubernetes { inheritFrom 'update-deployment' } }

  stages {
    stage('Build and push') {
      steps {
          script {
            sh "echo building image..."
            sh "echo pushing image..."
        }
      }
    }

    stage('Docker image analize') {
      steps {
          container('trivy') {
            script {
              withCredentials([file(credentialsId: 'templateHTMLTrivy', variable: 'template_html_trivy')]) {
                sh "cp ${template_html_trivy} html.tpl"
                sh "mkdir -p reports"
                sh "trivy image --timeout 10m --format template --template '@html.tpl' -o reports/minecraft.html itzg/minecraft-server:latest" 

                publishHTML target : [
                              allowMissing: true,
                              alwaysLinkToLastBuild: true,
                              keepAll: true,
                              reportDir: 'reports',
                              reportFiles: "minecraft.html",
                              reportName: 'TrivyScan',
                              reportTitles: 'Trivy Scan'
                          ]
                print(BUILD_URL + "TrivyScan")
              }
            }
        }
      }
    }

    stage('Notification') {
      steps {
          script {
            sh 'echo Notifying reports...'
        }
      }
    }

    stage('Updating yamls files and upgrade deployment') {
        steps {
            container('helm-kubectl') {
                script {
                    def NODE = "ip-10-1-224-115.ec2.internal"
                    sh 'echo Updating reports...'
                    sh "sed -i -e 's%\\\${VERSION}%${BUILD_NUMBER}%g' helm/minecraft/values.yaml"
                    sh "sed -i -e 's%\\\${NODE}%${NODE}%g' helm/minecraft/values.yaml"
                    sh "helm upgrade minecraft helm/minecraft --namespace minecraft"
                }
            }
        }
    }

    stage('Finish') {
      steps {
        print("This is the end")
      }
    }
  }
}

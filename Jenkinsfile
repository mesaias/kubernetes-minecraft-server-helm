pipeline {

  agent { kubernetes { inheritFrom 'minecraft' } }

  parameters {
    choice(
      name: 'options', 
      choices: ['Launch Minecraft server', 'Delete Minecraft server'], 
      description: 'SELECT AN OPTION TO CONTINUE'
      )
  }

  stages {
    stage('Checkout') {
      steps {
        sh 'echo passed'
        git branch: 'aws', url: 'https://github.com/mesaias/kubernetes-minecraft-server-helm.git'
      }
    }

    stage('Build and Test') {
      steps {
          container('helm-kubectl') {
            script {
              def helmListOutput = sh(script: "helm list -n minecraft -o json", returnStdout: true).trim()
              def NODE = "ip-10-1-224-115.ec2.internal"
              
              if (params.options == 'Launch Minecraft server') {
                 if (helmListOutput.contains("minecraft")) {
                        echo "A Minecraft server exists already, don't need create another one"
                    } else {
                        sh "sed -i -e 's%\\\${VERSION}%${BUILD_NUMBER}%g' helm/minecraft/values.yaml"
                        sh "sed -i -e 's%\\\${NODE}%${NODE}%g' helm/minecraft/values.yaml"
                        sh "helm install minecraft helm/minecraft --namespace minecraft"
                        //sh "helm upgrade minecraft helm/minecraft --namespace minecraft"
                    }

              } else {
                sh "helm uninstall minecraft --namespace minecraft"
              }
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

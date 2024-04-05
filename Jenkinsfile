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
        git branch: 'main', url: 'https://github.com/mesaias/minecraft-kubernetes-helm.git'
      }
    }

    stage('Build and Test') {
      steps {
          container('helm-kubectl') {
            script {
              def helmListOutput = sh(script: "helm list -n minecraft -o json", returnStdout: true).trim()

              if (params.options == 'Launch Minecraft server') {
                 if (helmListOutput.contains("minecraft")) {
                        echo "A Minecraft server exists already, don't need create another one"
                    } else {
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
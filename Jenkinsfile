pipeline {
    agent any

    stages {
        stage('pull') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: "*/${branch}"]],
                doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [],
                userRemoteConfigs: [[credentialsId: '9bc5f661-86ed-446c-b0b3-e41b364a39a2',
                url: 'http://git.5kcrm.com/zhangzhiwei/wk_crm_single.git']]])
                echo '拉取代码成功'
            }
        }
        stage('build') {
            steps {
                script {
                         def apps = "${project_name}".split(",")
                         for (int i = 0; i < apps.size(); ++i) {
                             sh label: '', script: "sh package.sh ${apps[i]}"
                             echo " ${apps[i]} 打包成功"
                         }
                    }
            }
        }
        stage('deploy') {
            steps {
                echo 'deploy'
                withEnv(['JENKINS_NODE_COOKIE=dontkillme']) {
                    script {
                            def apps = "${project_name}".split(",")
                            for (int i = 0; i < apps.size(); ++i) {
                                sh label: '', script: "sh deploy.sh stop ${apps[i]}"
                                sh label: '', script: "sh deploy.sh copy ${apps[i]}"
                                sh label: '', script: "sh deploy.sh start ${apps[i]}"
                                echo " ${apps[i]} 部署成功"
                            }
                        }
                   }
            }
        }

    }
}
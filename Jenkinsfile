pipeline {
    agent any

    environment {
        DOCKER_HUB_REPO = 'sonjiseokk/server'
        DOCKER_HUB_CREDENTIALS_ID = 'DOCKER_HUB_CREDENTIALS_ID'
        NETWORK_NAME = 'my-network'
        GITLAB_CREDENTIALS_ID = 'GITLAB_CREDENTIALS_ID' // GitLab 인증 정보 ID
        GITHUB_CREDENTIALS_ID = 'GITHUB_CREDENTIALS_ID'

        GITHUB_BACKEND_REPO_URL = "github.com/gonggibap/server.git"
        GITHUB_FRONTEND_REPO_URL = "github.com/gonggibap/client.git"

    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                configFileProvider([
                    configFile(fileId: '4f17cfba-7a7c-435a-9577-b2fc41d6d085', targetLocation: 'env.properties'),  // 첫 번째 파일
                    configFile(fileId: 'd879cd1e-dfd5-4f19-9a85-6800cf3926f7', targetLocation: 'application.yml')      // 두 번째 파일
                ]) {
                    script {
                        // config/env.properties 파일을 Docker 빌드에 포함시킵니다.
                        def app = docker.build("${DOCKER_HUB_REPO}:latest", ".")
                    }
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', "${DOCKER_HUB_CREDENTIALS_ID}") {
                        def app = docker.image("${DOCKER_HUB_REPO}:latest")
                        app.push()
                    }
                }
            }
        }

        stage('Deploy to Server') {
            steps {
                sshPublisher(publishers: [
                    sshPublisherDesc(
                        configName: 'ubuntu',
                        transfers: [
                            sshTransfer(
                                sourceFiles: '',
                                execCommand: """
                                    docker system prune -f
                                    docker pull ${DOCKER_HUB_REPO}:latest
                                    docker stop server || true
                                    docker rm server || true
                                    docker ps --filter "publish=8080" --format "{{.ID}}" | xargs -r docker stop
                                    docker ps --filter "publish=8080" --format "{{.ID}}" | xargs -r docker rm
                                    docker run -d --name server -p 8080:8080 ${DOCKER_HUB_REPO}:latest
                                """,
                                remoteDirectory: '/home/ubuntu',
                                removePrefix: ''
                            )
                        ],
                        usePromotionTimestamp: false,
                        useWorkspaceInPromotion: false,
                        verbose: true
                    )
                ])
            }
        }


        stage('Update GitLab Repository') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${GITLAB_CREDENTIALS_ID}", passwordVariable: 'GITLAB_PASSWORD', usernameVariable: 'GITLAB_USERNAME'),
                                 string(credentialsId: "${GITHUB_CREDENTIALS_ID}", variable: 'GITHUB_TOKEN')]) {
                    sh '''
                        git config --global user.email "thswltjr11@gmail.com"
                        git config --global user.name "sonjiseokk"

                        rm -rf S11P31C204

                        # Clone GitLab repository
                        git clone https://${GITLAB_USERNAME}:${GITLAB_PASSWORD}@lab.ssafy.com/s11-final/S11P31C204.git
                        cd S11P31C204

                        # Add backend subtree (to ensure it remains updated)
                        git subtree pull --prefix=server https://${GITHUB_TOKEN}@${GITHUB_BACKEND_REPO_URL} main

                        # Add frontend subtree
                        git subtree pull --prefix=client https://${GITHUB_TOKEN}@${GITHUB_FRONTEND_REPO_URL} main

                        # Set remote URL for GitLab
                        git remote set-url origin https://${GITLAB_USERNAME}:${GITLAB_PASSWORD}@lab.ssafy.com/s11-final/S11P31C204.git

                        # Ensure there are changes to commit and force push
                        git add .
                        git commit -m "Update subtrees" || true
                        git push --force origin main
                    '''
                }
            }
        }
    }

    post {
        success {
            script {
                def Author_ID = sh(script: "git show -s --pretty=%an", returnStdout: true).trim()
                def Author_Name = sh(script: "git show -s --pretty=%ae", returnStdout: true).trim()
                mattermostSend(color: 'good',
                message: "빌드 성공: ${env.JOB_NAME} #${env.BUILD_NUMBER} by ${Author_ID}(${Author_Name})\n(<${env.BUILD_URL}|Details>)",
                endpoint: 'https://meeting.ssafy.com/hooks/e61kngw88idn8gtrxuwafbxy3y',
                channel: 'GongGiBap-Jenkins')
            }
        }
        failure {
            script {
                withEnv(["LANG=en_US.UTF-8"]) {
                    def Author_ID = sh(script: "git show -s --pretty=%an", returnStdout: true).trim()
                    def Author_Name = sh(script: "git show -s --pretty=%ae", returnStdout: true).trim()
                    mattermostSend(color: 'danger',
                    message: "빌드 실패: ${env.JOB_NAME} #${env.BUILD_NUMBER} by ${Author_ID}(${Author_Name})\n(<${env.BUILD_URL}|Details>)",
                    endpoint: 'https://meeting.ssafy.com/hooks/e61kngw88idn8gtrxuwafbxy3y',
                    channel: 'GongGiBap-Jenkins')
                }
            }
        }
    }

}
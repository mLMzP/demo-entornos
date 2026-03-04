pipeline {
    agent any

    environment {
        APP_NAME        = 'demo-entornos'
        APP_PORT        = '8080'
        DOCKER_IMAGE    = "demo-entornos:${BUILD_NUMBER}"
        CONTAINER_NAME  = "demo-entornos-test-${BUILD_NUMBER}"
        POSTMAN_COLLECTION = 'src/test/postman/collection.json'
        POSTMAN_ENV        = 'src/test/postman/environment.json'
    }

    tools {
        maven 'Maven'   // nombre configurado en Jenkins → Global Tool Configuration
        jdk   'JDK17'   // nombre configurado en Jenkins → Global Tool Configuration
    }

    stages {

        // ─────────────────────────────────────────────
        stage('Checkout') {
        // ─────────────────────────────────────────────
            steps {
                echo '📥 Clonando repositorio...'
                checkout scm
            }
        }

        // ─────────────────────────────────────────────
        stage('Build') {
        // ─────────────────────────────────────────────
            steps {
                echo '🔨 Compilando proyecto con Maven...'
                sh 'mvn clean package -DskipTests'
            }
            post {
                success { echo '✅ Build exitoso.' }
                failure { echo '❌ Build fallido.' }
            }
        }

        // ─────────────────────────────────────────────
        stage('Unit Tests') {
        // ─────────────────────────────────────────────
            steps {
                echo '🧪 Ejecutando pruebas unitarias...'
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        // ─────────────────────────────────────────────
        stage('Docker Build') {
        // ─────────────────────────────────────────────
            steps {
                echo "🐳 Construyendo imagen Docker: ${DOCKER_IMAGE}"
                sh "docker build -t ${DOCKER_IMAGE} ."
            }
        }

        // ─────────────────────────────────────────────
        stage('Start App') {
        // ─────────────────────────────────────────────
            steps {
                echo '🚀 Levantando la aplicación para pruebas...'
                sh """
                    docker run -d \
                        --name ${CONTAINER_NAME} \
                        -p ${APP_PORT}:${APP_PORT} \
                        ${DOCKER_IMAGE}
                """
                // Esperar a que la app arranque (health check)
                sh """
                    echo 'Esperando a que la aplicación esté lista...'
                    for i in \$(seq 1 20); do
                        if curl -sf http://localhost:${APP_PORT}/actuator/health > /dev/null 2>&1; then
                            echo '✅ Aplicación lista.'
                            break
                        fi
                        echo "Intento \$i/20 - esperando..."
                        sleep 5
                    done
                """
            }
        }

        // ─────────────────────────────────────────────
        stage('Newman / Postman Tests') {
        // ─────────────────────────────────────────────
            steps {
                echo '📬 Ejecutando pruebas de API con Newman...'
                sh """
                    npx newman run ${POSTMAN_COLLECTION} \
                        --environment ${POSTMAN_ENV} \
                        --reporters cli,junit \
                        --reporter-junit-export target/newman/newman-report.xml \
                        --color on
                """
            }
            post {
                always {
                    junit allowEmptyResults: true,
                          testResults: 'target/newman/newman-report.xml'
                }
            }
        }

    } // end stages

    // ─────────────────────────────────────────────
    post {
    // ─────────────────────────────────────────────
        always {
            echo '🧹 Limpiando contenedor e imagen Docker...'
            sh """
                docker stop  ${CONTAINER_NAME} || true
                docker rm    ${CONTAINER_NAME} || true
                docker rmi   ${DOCKER_IMAGE}   || true
            """
        }
        success {
            echo '🎉 Pipeline completado con éxito.'
        }
        failure {
            echo '🔴 El pipeline ha fallado. Revisa los logs.'
        }
    }

}

pipeline {
    agent any
    
    // Define your variables
    environment {
        DOCKER_USER = 'kush2811'
        // Jenkins will securely inject these credentials later
        DOCKER_CREDS = credentials('dockerhub-credentials') 
    }

    stages {
        stage('Build Docker Images') {
            steps {
                echo 'Building Backend and Frontend Images...'
                sh 'docker build -t ${DOCKER_USER}/car-rental-backend:latest "./Car Rental System Backend"'
                sh 'docker build -t ${DOCKER_USER}/car-rental-frontend:latest "./Car Rental System Frontend"'
            }
        }
        
        stage('Push to Docker Hub') {
            steps {
                echo 'Logging into Docker Hub...'
                sh 'echo ${DOCKER_CREDS_PSW} | docker login -u ${DOCKER_CREDS_USR} --password-stdin'
                
                echo 'Pushing images to the cloud...'
                sh 'docker push ${DOCKER_USER}/car-rental-backend:latest'
                sh 'docker push ${DOCKER_USER}/car-rental-frontend:latest'
            }
        }
        
        stage('Deploy to Kubernetes') {
            steps {
                echo 'Applying Kubernetes Manifests...'
                sh 'kubectl apply -f k8s-deployment.yaml'
                
                echo 'Forcing Kubernetes to pull the new images...'
                sh 'kubectl rollout restart deployment/backend-deployment'
                sh 'kubectl rollout restart deployment/frontend-deployment'
            }
        }
    }
}

node {
    /**
     * Making sure, that there are only at most 2 artifacts stored on a server,
     * because We don't want to waste a storage on a server, do we?
     */
    properties([buildDiscarder(logRotator(
            artifactDaysToKeepStr: '',
            artifactNumToKeepStr: '2',
            daysToKeepStr: '',
            numToKeepStr: '')),
                disableConcurrentBuilds(),
                pipelineTriggers([githubPush()])])

    stage("Pre Cleanup") {
        deleteDir()
    }

    stage("Checkout") {
        git "https://github.com/Humberd/MicroTwitter.git"
    }

    stage("Build") {
        parallel(
                API: {
                    dir("api") {
                        withMaven(maven: "Maven") {
                            sh "mvn clean install -DskipTests=true"
                        }
                    }
                }
        )

    }

//    stage("Test") {
//        parallel(
//                API: {
//                    dir("api") {
//                        dockerComposeFile = "../production.api-testing.docker-compose.yml"
//
//                        sh "docker-compose -f ${dockerComposeFile} down --rmi all --remove-orphans"
//                        sh "docker-compose -f ${dockerComposeFile} up -d"
//
//                        try {
//                            withMaven(maven: "Maven") {
//                                sh "mvn test -DargLine='-Dspring.profiles.active=production'"
//                                sh "mvn jacoco:report"
//                            }
//                        } finally {
//                            sh "docker-compose -f ${dockerComposeFile} down --rmi all --remove-orphans"
//                        }
//                    }
//                }
//        )
//    }

    stage("Deploy") {
        dockerComposeFile = "production.deploy.docker-compose.yml"

//        sh "ls api -al"

        /**
         * Setting environment variables only for a docker container
         */
        withEnv([
                "COMMIT=${getCommit()}",
                "BUILD_NO=${getBuildNumber()}"
        ]) {
            sh "docker-compose -f ${dockerComposeFile} down --rmi all --remove-orphans"
            sh "docker-compose -f ${dockerComposeFile} up -d"
        }
    }

    stage("Post Cleanup") {
        deleteDir()
    }
}

def getCommit() {
    return sh(
            script: "git show -s",
            returnStdout: true
    ).trim()
}

def getBuildNumber() {
    return env.BUILD_NUMBER
}
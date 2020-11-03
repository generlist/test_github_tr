import groovy.json.JsonOutput


def appVersion() {
    def workspacePath = manager.build.getEnvVars()["WORKSPACE"]
    def versionNameCmd = ['/bin/sh', '-c', "cd  ${workspacePath} &&  ./gradlew -q printVersion"]

    versionNameCmd.execute().with {
        def output = new StringWriter()
        def error = new StringWriter()

        it.waitForProcessOutput(output, error)

        return output

    }

}

def slackNotificationChannel = 'android-app-release'

def notifySlack(text, channel, blocks) {


    def slackURL = 'https://hooks.slack.com/services/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'
    def jenkinsIcon = 'https://wiki.jenkins-ci.org/download/attachments/2916393/logo.png'
    def payload = JsonOutput.toJson([text: text, channel: channel, username: "Jenkins", icon_url: jenkinsIcon, blocks: blocks])


    def cmd = ['/bin/sh', '-c', "curl -X POST --data-urlencode \'payload=${payload}\' ${slackURL}"]

    manager.listener.logger.println("cmd=$cmd")

    cmd.execute().with {
        def output = new StringWriter()
        def error = new StringWriter()
        //wait for process ended and catch stderr and stdout.
        it.waitForProcessOutput(output, error)
        //check there is no error
        manager.listener.logger.println("error=$error")
        manager.listener.logger.println("output=$output")
        manager.listener.logger.println("code=${it.exitValue()}")

    }

}

def getGitAuthor = {
    def workspacePath = manager.build.getEnvVars()["WORKSPACE"]
    def cmd = ['/bin/sh', '-c', "cd  ${workspacePath} && git rev-parse HEAD"]
    cmd.execute().with {
        def output = new StringWriter()
        def error = new StringWriter()
        it.waitForProcessOutput(output, error)

        def authorCmd = ['/bin/sh', '-c', "cd  ${workspacePath} && git --no-pager show -s --format='%an' ${output}"]
        authorCmd.execute().with {
            def author = new StringWriter()
            def authorError = new StringWriter()
            it.waitForProcessOutput(author, authorError)
            manager.listener.logger.println "author: ${author}"
            return author
        }

    }

}
def getLastCommitMessage = {
    def workspacePath = manager.build.getEnvVars()["WORKSPACE"]
    def lastMessageCmd = ['/bin/sh', '-c', "cd  ${workspacePath} && git log -1 --pretty=%B"]
    lastMessageCmd.execute().with {
        def outPut = new StringWriter()
        def error = new StringWriter()
        it.waitForProcessOutput(outPut, error)
        manager.listener.logger.println "lastMessage: ${outPut}"
        return outPut
    }
}

def getJobName() {
    def jobName = manager.build.getEnvVars()["JOB_NAME"]
    // Strip the branch name out of the job name (ex: "Job Name/branch1" -> "Job Name")
    //jobName = jobName.getAt(0..(jobName.indexOf('/') - 1))"
    manager.listener.logger.println "jobName: ${jobName}"
    return jobName
}

def getJobBuildNumber(){
    def build = Thread.currentThread().executable
    def buildNumber = build.number
    manager.listener.logger.println "buildNumber: ${buildNumber}"
    return buildNumber
}

def getBranch() {
    def branch = manager.build.getEnvVars()["GIT_BRANCH"]
    manager.listener.logger.println "branch: ${branch}"
    return branch
}
def getBuildUrl() {
    def buildUrl = manager.build.getEnvVars()["BUILD_URL"]
    manager.listener.logger.println "buildUrl: ${buildUrl}"
    return buildUrl
}

def getBuildResult(){
    def result = manager.build.result
    manager.listener.logger.println "Build Result is: ${result}"
    return result
}
getBuildResult()
getJobName()
getJobBuildNumber()
getBranch()
getGitAuthor()
getBuildUrl()
getLastCommitMessage()
def successNotification = [
        ["type": "section", "text": ["type": "mrkdwn", "text": "안녕하세요. LineTV 개발 신현붕 입니다."]],
        ["type": "section", "text": ["type": "mrkdwn", "text": "LineTV Android ${appVersion()} 배포 공유 드립니다."]],
        ["type": "section", "text": ["type": "mrkdwn", "text": ":ghost: [주요 개발 내용] 은 <https://wiki.navercorp.com/display/videocell/LineTV${appVersion()} |주요 개발 내용> 를 참조 부탁 드립니다."]],
        ["type": "divider"], ["type": "section", "text": ["type": "mrkdwn", "text": "*<https://ndeploy.navercorp.com/app/292/android/real/latest|리얼 다운로드>*"]],
        ["type": "section", "text": ["type": "mrkdwn", "text": "*<https://ndeploy.navercorp.com/app/292/android/super-admin/latest|수퍼어드민 다운로드>*"]],
        ["type": "section", "text": ["type": "mrkdwn", "text": "*<https://ndeploy.navercorp.com/app/292/android/hms/latest|앱 갤러리용 다운로드>*"]]
]

def failNotification = [
        [title: ":cry_face: ${getJobName()}, build #${getJobBuildNumber()}", title_link: "${getBuildUrl()}", color: "danger", text: "${getBuildResult()}\n${getGitAuthor()}",
         "mrkdwn_in": ["fields"],fields: [[title: "Branch", value: "${getBranch()}", short: true], [title: "Last Commit", value: "${getLastCommitMessage()}", short: false]]
        ]
]
try {
//    if(getBuildResult() == 'SUCCESS'){
//        notifySlack("LineTV ${appVersion()} Real 배포 공유", slackNotificationChannel, successNotification)
//    }else{
//        notifySlack("LineTV ${appVersion()} Real 배포 공유", slackNotificationChannel, failNotification)
//    }
    notifySlack("LineTV ${appVersion()} Real 배포 공유", slackNotificationChannel, successNotification)

}  catch (hudson.AbortException ae) {
    manager.listener.logger.println "[Fail StackTrace]: ${ae}"
    notifySlack("LineTV ${appVersion()} Real 배포 공유", slackNotificationChannel, failNotification)
} catch (e) {
    manager.listener.logger.println "[Fail StackTrace]: ${e}"

}

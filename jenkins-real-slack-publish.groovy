
import groovy.json.JsonOutput

//앱 버전을 가져온다.
def appVersion() {
    try {
        def workspacePath = manager.build.getEnvVars()["WORKSPACE"] + "/LineTV_BUILD"
        def versionNameCmd = ['/bin/sh', '-c', "export ANDROID_HOME=/home1/irteam/android_home && cd ${workspacePath} && ./gradlew -q printVersion"]

        manager.listener.logger.println("workspacePath=$workspacePath")
        versionNameCmd.execute().with {
            def output = new StringWriter()
            def error = new StringWriter()

            it.waitForProcessOutput(output, error)
            manager.listener.logger.println("error() =${error}")
            manager.listener.logger.println("code=${it.exitValue()}")
            //값에 엔터가 들어가 있어서 제거
            return "$output".replaceAll("\n", "")

        }
    }catch(e){
        manager.listener.logger.println("eeeee() =${e}")
        //currentBuild.result = 'FAILURE'
        throw e
    }

}

def slackNotificationChannel = 'android-app-release'

def notifySlack(text, channel, blocks) {

    try {
        def slackURL = 'https://hooks.slack.com/services/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'
        def jenkinsIcon = 'https://wiki.jenkins-ci.org/download/attachments/2916393/logo.png'
        def payload = JsonOutput.toJson([text: text, channel: channel, username: "Jenkins", icon_url: jenkinsIcon, blocks: blocks])
        currentBuild.result = 'FAILURE'

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

    } catch (e) {
        manager.listener.logger.println("kkkkkk() =${e}")
        manager.currentBuild.result = 'FAILURE'
        throw e
    } finally {
       // currentBuild.result = 'FAILURE'
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
            return  "$author".replaceAll("\n","")
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
        return "$outPut".replaceAll("\n","")
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

def successNotification = [
        ["type": "section", "text": ["type": "mrkdwn", "text": ":ghost: 안녕하세요. LINE TV 개발 신현붕 입니다."]],
        ["type": "section", "text": ["type": "mrkdwn", "text": "LINE TV Android TV *${appVersion()}* 배포 공유 드립니다."]],
        ["type": "section", "text": ["type": "mrkdwn", "text": ":tv: 자세한 사항은 *<https://wiki.navercorp.com/display/videocell/AOS-TV-${appVersion()}-release|주요 개발 내용>* 를 참조 부탁 드립니다."]],
        ["type": "divider"], ["type": "section", "text": ["type": "mrkdwn", "text": "*<https://ndeploy.navercorp.com/app/941/android/real/latest|리얼 다운로드>*"]]
]

def failNotification = [

        ["type": "section", "text": ["type": "mrkdwn", "text": "안녕하세요. LINE TV 개발 신현붕 입니다."]],
        ["type": "section", "text": ["type": "mrkdwn", "text": "LINETV APP *${appVersion()}* 이 Build가 실패 하였습니다.:allo-crying: 재 배포 하도록 하겠습니다.\n 잠시만 기다려 주세요!"]],
        ["type": "divider"],
        ["type": "section", "text": ["type": "mrkdwn", "text": "*[Build 정보]*"]],
        ["type": "section", "text": ["type": "mrkdwn", "text": "Build Result : `${getBuildResult()}` \n JobName : `${getJobName()}` \n Build Number : `#${getJobBuildNumber()}`"]],
        ["type": "section", "text": ["type": "mrkdwn", "text": "\n *<${getBuildUrl()}|Build정보 바로 가기>*"]],
        ["type": "divider"],
        ["type": "section", "text": ["type": "mrkdwn", "text": "*[Repository 정보]*"]],
        ["type": "section", "text": ["type": "mrkdwn", "text": "Author : `${getGitAuthor()}` \n Branch : `${getBranch()}` \n Last Commit : `${getLastCommitMessage()}` \n"]],

]
try {
    def result= "${getBuildResult()}"
    if(result == "SUCCESS"){
        notifySlack("LineTV ${appVersion()} Real 배포 공유", slackNotificationChannel, successNotification)
    }else{
        notifySlack("LineTV ${appVersion()} Real 배포 공유", slackNotificationChannel, failNotification)
    }

}  catch (hudson.AbortException ae) {
    manager.listener.logger.println "[Fail StackTrace]: ${ae}"
    notifySlack("LineTV ${appVersion()} Real 배포 공유", slackNotificationChannel, failNotification)
} catch (e) {
    manager.listener.logger.println "[Fail StackTrace]: ${e}"
    notifySlack("LineTV ${appVersion()} Real 배포 공유", slackNotificationChannel, failNotification)

}


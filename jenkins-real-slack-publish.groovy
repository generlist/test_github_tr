
import groovy.json.JsonOutput
import groovy.transform.Field


@Field final String slackNotificationChannel = 'android-app-release'
@Field final String slackURL = 'https://hooks.slack.com/services/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'
@Field final String jenkinsIcon = 'https://wiki.jenkins-ci.org/download/attachments/2916393/logo.png'
@Field final String failTitle = 'LINE TV Real TV 빌드 실패'


@Field def failErrorNotification = [
        [
                fallback: "summary of the attachment.",
                color: "#2eb886",
                title: "Jenkins Build Error",
                text: "*<${getBuildUrl()}|빌드 바로 가기>*",
                "mrkdwn_in": ["text"],

        ]
]
@Field def getGitAuthor = {
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
@Field def getLastCommitMessage = {
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

def getJobNumbers(){
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
def getAppVersion() {
    try {
//        def workspacePath = manager.build.getEnvVars()["WORKSPACE"] + "/LineTV_BUILD"
//        def versionNameCmd = ['/bin/sh', '-c', "export ANDROID_HOME=/home1/irteam/android_home && cd ${workspacePath} && ./gradlew -q printVersion"]
        def workspacePath = manager.build.getEnvVars()["WORKSPACE"]
        def versionNameCmd = ['/bin/sh', '-c', "cd  ${workspacePath} &&  ./gradlew -q printVersion"]

        manager.listener.logger.println("workspacePath=$workspacePath")
        versionNameCmd.execute().with {
            def output = new StringWriter()
            def error = new StringWriter()

            it.waitForProcessOutput(output, error)
            manager.listener.logger.println("error() =${error}")
            manager.listener.logger.println("code=${it.exitValue()}")
            //값에 엔터가 들어가 있어서 제거
            if("${error}" =="0"){
                return "$output".replaceAll("\n", "")
            }else{
                throw new Exception()
            }

        }
    }catch(e){
        manager.listener.logger.println("getAppVersion() error = ${e}")
        failNotifySlack("${failTitle} getAppVersion() " ,slackNotificationChannel, failErrorNotification)
        Thread.getAllStackTraces().keySet().each() {
            t -> if (t.getName()=="${ Thread.currentThread().name}" ) {   t.interrupt();  }
        }
        throw e
    }

}
@Field def successNotification = [
        ["type": "section", "text": ["type": "mrkdwn", "text": ":ghost: 안녕하세요. LINE TV 개발 신현붕 입니다."]],
        ["type": "section", "text": ["type": "mrkdwn", "text": "LINE TV Android TV *${getAppVersion()}* 배포 공유 드립니다."]],
        ["type": "section", "text": ["type": "mrkdwn", "text": ":tv: 자세한 사항은 *<https://wiki.navercorp.com/display/videocell/AOS-TV-${getAppVersion()}-release|주요 개발 내용>* 를 참조 부탁 드립니다."]],
        ["type": "divider"], ["type": "section", "text": ["type": "mrkdwn", "text": "*<https://ndeploy.navercorp.com/app/941/android/real/latest|리얼 다운로드>*"]]
]

@Field def failNotification = [

        ["type": "section", "text": ["type": "mrkdwn", "text": "안녕하세요. LINE TV 개발 신현붕 입니다."]],
        ["type": "section", "text": ["type": "mrkdwn", "text": "LINETV APP *${getAppVersion()}* 이 Build가 실패 하였습니다.:allo-crying: 재 배포 하도록 하겠습니다.\n 잠시만 기다려 주세요!"]],
        ["type": "divider"],
        ["type": "section", "text": ["type": "mrkdwn", "text": "*[Build 정보]*"]],
        ["type": "section", "text": ["type": "mrkdwn", "text": "Build Result : `${getBuildResult()}` \n JobName : `${getJobName()}` \n Build Number : `#${getJobNumbers()}`"]],
        ["type": "section", "text": ["type": "mrkdwn", "text": "\n *<${getBuildUrl()}|Build정보 바로 가기>*"]],
        ["type": "divider"],
        ["type": "section", "text": ["type": "mrkdwn", "text": "*[Repository 정보]*"]],
        ["type": "section", "text": ["type": "mrkdwn", "text": "Author : `${getGitAuthor()}` \n Branch : `${getBranch()}` \n Last Commit : `${getLastCommitMessage()}` \n"]],

]


def failNotifySlack(text, channel, attachments) {
    try {

        def payload = JsonOutput.toJson([text: text, channel: channel, username: "Jenkins", icon_url: jenkinsIcon, attachments: attachments])

        def cmd = ['/bin/sh', '-c', "curl -X POST --data-urlencode \'payload=${payload}\' ${slackURL}"]

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
        manager.listener.logger.println("failNotifySlack() = ${e}")
        throw e
    } finally {}
}


def notifySlack(text, channel, blocks) {

    try {

        def payload = JsonOutput.toJson([text: text, channel: channel, username: "Jenkins", icon_url: jenkinsIcon, blocks: blocks])

        def cmd = ['/bin/sh', '-c', "curl -X POST --data-urlencode \'payload=${payload}\' ${slackURL}"]
        manager.listener.logger.println("cmd=$cmd")

        cmd.execute().with {
            def output = new StringWriter()
            def error = new StringWriter()
            //wait for process ended and catch stderr and stdout.
            it.waitForProcessOutput(output, error)
            //check there is no error
            manager.listener.logger.println("error= $error")
            manager.listener.logger.println("output= $output")
            manager.listener.logger.println("code= ${it.exitValue()}")

        }

    } catch (e) {
        manager.listener.logger.println("notifySlack() =${e}")
        failNotifySlack("${failTitle} notifySlack() ",slackNotificationChannel,failErrorNotification)
        Thread.getAllStackTraces().keySet().each() {
            t -> if (t.getName()=="${ Thread.currentThread().name}" ) {   t.interrupt();  }
        }
        throw e
    } finally {}
}



try {
    def result= "${getBuildResult()}"
    if(result == "SUCCESS"){
        notifySlack("LineTV ${getAppVersion()} Real 배포 공유", slackNotificationChannel, successNotification)
    }else{
        notifySlack("LineTV ${getAppVersion()} Real 배포 공유", slackNotificationChannel, failNotification)
    }

}  catch (hudson.AbortException ae) {
    manager.listener.logger.println "[Fail StackTrace]: ${ae}"
    failNotifySlack("${failTitle} ",slackNotificationChannel,failErrorNotification)
    Thread.getAllStackTraces().keySet().each() {
        t -> if (t.getName()=="${ Thread.currentThread().name}" ) {   t.interrupt();  }
    }

} catch (e) {
    manager.listener.logger.println "[Fail StackTrace]: ${e}"
    failNotifySlack("${failTitle} notifySlack()",slackNotificationChannel,failErrorNotification)
    Thread.getAllStackTraces().keySet().each() {
        t -> if (t.getName()=="${ Thread.currentThread().name}" ) {   t.interrupt();  }
    }

}



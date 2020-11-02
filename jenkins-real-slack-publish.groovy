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

notifySlack("LineTV ${appVersion()} Real 배포 공유", slackNotificationChannel, [
["type": "section", "text": ["type": "mrkdwn", "text": "안녕하세요. LineTV 개발 신현붕 입니다."]],
["type": "section", "text": ["type": "mrkdwn", "text": "LineTV Android ${appVersion()} 배포 공유 드립니다."]],
["type": "section", "text": ["type": "mrkdwn", "text": ":ghost: [주요 개발 내용] 은 <https://wiki.navercorp.com/display/videocell/LineTV${appVersion()} |주요 개발 내용> 를 참조 부탁 드립니다."]],
["type": "divider"], ["type": "section", "text": ["type": "mrkdwn", "text": "*<https://ndeploy.navercorp.com/app/292/android/real/latest|리얼 다운로드>*"]],
["type": "section", "text": ["type": "mrkdwn", "text": "*<https://ndeploy.navercorp.com/app/292/android/super-admin/latest|수퍼어드민 다운로드>*"]],
["type": "section", "text": ["type": "mrkdwn", "text": "*<https://ndeploy.navercorp.com/app/292/android/hms/latest|앱 갤러리용 다운로드>*"]]
])
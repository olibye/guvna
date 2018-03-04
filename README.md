[![Build Status](https://travis-ci.org/olibye/guvna.svg?branch=master)](https://travis-ci.org/olibye/guvna)

#State Machine library

I wanted to make a 10x10 state transition table in code to fit on my screen.

License: https://www.apache.org/licenses/LICENSE-2.0

#Release
----
mvn versions:set -DoldVersion=2.8.1-SNAPSHOT -DnewVersion=2.8.1 -DgroupId=org.jmock
find . -name pom.xml.versionsBackup -exec rm {} \;

eval $(gpg-agent --daemon --no-grab)
export GPG_AGENT_INFO
export GPG_TTY=$(tty)
mvn clean deploy -P release --settings settings.xml -Dgpg.keyname=XXXXXXXX

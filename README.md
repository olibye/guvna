[![Build Status](https://travis-ci.org/olibye/guvna.svg?branch=master)](https://travis-ci.org/olibye/guvna)

# State Machine library

I wanted to make a 10x10 state transition table in code to fit on my screen.

License: https://www.apache.org/licenses/LICENSE-2.0

## Release
----
```
eval $(gpg-agent --daemon --no-grab)
export GPG_AGENT_INFO
export GPG_TTY=$(tty)

mvn versions:set -DoldVersion=2.8.1-SNAPSHOT -DnewVersion=2.8.1 -DgroupId=com.positiverobot
find . -name pom.xml.versionsBackup -exec rm {} \;
```

install check
```
mvn clean install -P release -Dgpg.keyname=XXXXXXXX
```

deploy to sonatype repository
```
export SONATYPE_USERNAME=xx
export SONATYPE_PASSWORD=yy

mvn clean deploy -P release  --settings settings.xml -Dgpg.keyname=XXXXXXXX
```

### Check deployment
It will not appear immediately at https://mvnrepository.com/artifact/com.positiverobot/guvna
Instead check check https://oss.sonatype.org/#nexus-search;quick~guvna
Publishing to mvnrepository.com will happen eventually

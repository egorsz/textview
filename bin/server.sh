#!/usr/bin/env bash
export PROJECT_HOME=/home/yegor/code/java/bigdata/yproject
cd $PROJECT_HOME && mvn clean package -Dmaven.test.skip=true
java -cp $PROJECT_HOME/target/uber-yproject-1.0-SNAPSHOT.jar com.ytsebro.web.StartServer $PROJECT_HOME/bin/main.properties $PROJECT_HOME
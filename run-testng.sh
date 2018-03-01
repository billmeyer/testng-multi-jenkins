#!/bin/bash

CP=./target/test-classes
CP=$CP:./target/classes
CP=$CP:~/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar
CP=$CP:~/.m2/repository/org/testng/testng/6.13.1/testng-6.13.1.jar
CP=$CP:~/.m2/repository/com/beust/jcommander/1.72/jcommander-1.72.jar
CP=$CP:~/.m2/repository/org/seleniumhq/selenium/selenium-java/3.7.1/selenium-java-3.7.1.jar
CP=$CP:~/.m2/repository/org/seleniumhq/selenium/selenium-api/3.7.1/selenium-api-3.7.1.jar
CP=$CP:~/.m2/repository/org/seleniumhq/selenium/selenium-chrome-driver/3.7.1/selenium-chrome-driver-3.7.1.jar
CP=$CP:~/.m2/repository/org/seleniumhq/selenium/selenium-edge-driver/3.7.1/selenium-edge-driver-3.7.1.jar
CP=$CP:~/.m2/repository/org/seleniumhq/selenium/selenium-firefox-driver/3.7.1/selenium-firefox-driver-3.7.1.jar
CP=$CP:~/.m2/repository/org/seleniumhq/selenium/selenium-ie-driver/3.7.1/selenium-ie-driver-3.7.1.jar
CP=$CP:~/.m2/repository/org/seleniumhq/selenium/selenium-opera-driver/3.7.1/selenium-opera-driver-3.7.1.jar
CP=$CP:~/.m2/repository/org/seleniumhq/selenium/selenium-remote-driver/3.7.1/selenium-remote-driver-3.7.1.jar
CP=$CP:~/.m2/repository/org/seleniumhq/selenium/selenium-safari-driver/3.7.1/selenium-safari-driver-3.7.1.jar
CP=$CP:~/.m2/repository/org/seleniumhq/selenium/selenium-support/3.7.1/selenium-support-3.7.1.jar
CP=$CP:~/.m2/repository/net/bytebuddy/byte-buddy/1.7.5/byte-buddy-1.7.5.jar
CP=$CP:~/.m2/repository/org/apache/commons/commons-exec/1.3/commons-exec-1.3.jar
CP=$CP:~/.m2/repository/commons-codec/commons-codec/1.10/commons-codec-1.10.jar
CP=$CP:~/.m2/repository/commons-logging/commons-logging/1.2/commons-logging-1.2.jar
CP=$CP:~/.m2/repository/com/google/code/gson/gson/2.8.2/gson-2.8.2.jar
CP=$CP:~/.m2/repository/com/google/guava/guava/23.0/guava-23.0.jar
CP=$CP:~/.m2/repository/com/google/code/findbugs/jsr305/1.3.9/jsr305-1.3.9.jar
CP=$CP:~/.m2/repository/com/google/errorprone/error_prone_annotations/2.0.18/error_prone_annotations-2.0.18.jar
CP=$CP:~/.m2/repository/com/google/j2objc/j2objc-annotations/1.1/j2objc-annotations-1.1.jar
CP=$CP:~/.m2/repository/org/codehaus/mojo/animal-sniffer-annotations/1.14/animal-sniffer-annotations-1.14.jar
CP=$CP:~/.m2/repository/org/apache/httpcomponents/httpclient/4.5.3/httpclient-4.5.3.jar
CP=$CP:~/.m2/repository/org/apache/httpcomponents/httpcore/4.4.6/httpcore-4.4.6.jar
CP=$CP:~/.m2/repository/commons-lang/commons-lang/2.6/commons-lang-2.6.jar
CP=$CP:~/.m2/repository/com/saucelabs/sauce_testng/2.1.23/sauce_testng-2.1.23.jar
CP=$CP:~/.m2/repository/com/saucelabs/sauce_java_common/2.1.23/sauce_java_common-2.1.23.jar
CP=$CP:~/.m2/repository/com/googlecode/json-simple/json-simple/1.1.1/json-simple-1.1.1.jar
CP=$CP:~/.m2/repository/junit/junit/4.10/junit-4.10.jar
CP=$CP:~/.m2/repository/com/saucelabs/saucerest/1.0.32/saucerest-1.0.32.jar
CP=$CP:~/.m2/repository/org/json/json/20090211/json-20090211.jar

#echo "\$CP=$CP"
java -cp $CP org.testng.TestNG -threadcount 20 -parallel true -reporter org.testng.reporters.XMLReporter:generateTestResultAttributes=true,generateGroupsAttribute=true $* testng.xml


FROM registry.access.redhat.com/ubi8/openjdk-17-runtime:latest

COPY target/nationalparks-runner.jar /opt/nationalparks.jar

CMD java -jar /opt/nationalparks.jar

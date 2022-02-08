FROM maven:3.6.3-jdk-11 AS builder
WORKDIR /opt/app
COPY . .
RUN mvn clean package


FROM registry.access.redhat.com/ubi8/openjdk-11:latest
COPY --from=builder /opt/app/target/nationalparks.jar /opt
CMD java -jar /opt/nationalparks.jar
EXPOSE 8080

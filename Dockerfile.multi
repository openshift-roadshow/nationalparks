FROM gcr.io/cloud-builders/mvn:latest AS builder
WORKDIR /tmp
COPY . .
RUN mvn --no-transfer-progress verify package


FROM registry.access.redhat.com/ubi8/openjdk-11:latest
COPY --from=builder /tmp/target/nationalparks.jar /opt
CMD java -jar /opt/nationalparks.jar
EXPOSE 8080

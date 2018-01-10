FROM maven:3.5-jdk-9-slim
ADD ./ /data
WORKDIR /data
CMD mvn clean integration-test -Phit-refresh -Dport=8080
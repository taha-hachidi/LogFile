FROM maven:3.8.5-openjdk-17 as build
WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

RUN mkdir -p /app/logs

COPY --from=build /app/target/*.jar app.jar

COPY logs/FINEA_TRACE-2022-11-23-1.log /app/logs/

RUN apt-get update && apt-get install -y inotify-tools

COPY monitor_log.sh /app/monitor_log.sh

RUN chmod +x /app/monitor_log.sh

EXPOSE 8080

ENTRYPOINT ["/bin/sh", "-c", "/app/monitor_log.sh & exec java -jar app.jar"]

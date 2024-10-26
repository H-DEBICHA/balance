
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/balance-0.0.1-SNAPSHOT.jar balance.jar

EXPOSE 8080

CMD ["java", "-jar", "balance.jar"]
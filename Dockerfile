FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/CashPilot-0.0.1-SNAPSHOT.jar cashpilot-v1.0.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","cashpilot-v1.0"]
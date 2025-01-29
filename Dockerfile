FROM openjdk:22-jdk-slim
COPY build/libs/pasteHundler.jar /app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]
FROM eclipse-temurin:21

WORKDIR /app

COPY ./target/versions-1.0.0.jar versions-1.0.0.jar

EXPOSE 8080

CMD ["java", "-jar", "versions-1.0.0.jar"]
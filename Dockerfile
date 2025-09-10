FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

COPY src src

RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/ecs_cicd-0.0.1-SNAPSHOT.jar"]
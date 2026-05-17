FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

RUN mkdir -p /app/data /app/uploads

COPY --from=build /workspace/target/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod
ENV PORT=8080

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

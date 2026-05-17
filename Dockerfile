FROM maven:3.9.4-eclipse-temurin-21 AS build

#Build stage: Compile the application and package it into a JAR file
RUN addgroup --system app && adduser --system --group app
USER app
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

#Runtime stage: Use a lightweight JRE image to run the application
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
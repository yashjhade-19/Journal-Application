# Step 1: Use Maven + Java 11 to build the app
FROM maven:3.9.5-eclipse-temurin-11 AS build
WORKDIR /app
COPY src .
RUN mvn clean package -DskipTests

# Step 2: Use Java 11 JDK to run the app
FROM eclipse-temurin:11-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

# Step 1: Build the app using Maven and Java 11
FROM maven:3.9.5-eclipse-temurin-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Step 2: Run the app using Java 11 JDK
FROM eclipse-temurin:11-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

# --- Build Stage ----
FROM maven:3.8-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
# Download all project dependencies.
RUN mvn dependency:go-offline
# The cache for this layer is invalidated when code changes.
COPY src ./src
# This command runs only when the source code or pom.xml has changed.
RUN mvn package -DskipTests

# ---- Run Stage ----
FROM amazoncorretto:17-alpine
WORKDIR /app
# Copy the built JAR file from the build stage.
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
# ---- Build Stage: Build the layered JAR and extract the layers ----
FROM maven:3.8-amazoncorretto-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
# Build the layered JAR
RUN mvn package -DskipTests

# Use layertools to extract the application layers
RUN java -Djarmode=layertools -jar target/collectables.jar extract

# ---- Final Stage: Create the final image with optimized layers ----
FROM amazoncorretto:17-alpine

WORKDIR /app

# Copy the extracted layers from the builder stage
# Dependencies are copied first as they change infrequently
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./

# The entrypoint now uses the Spring Boot loader directly
# Needed to run the layered JAR
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
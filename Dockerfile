# Build stage
FROM maven:3.9.4-eclipse-temurin-21 AS builder

WORKDIR /workspace

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn -B -f pom.xml -DskipTests dependency:go-offline

# Copy source code
COPY src ./src

# Copy application-example.yaml to application.yaml
RUN cp src/main/resources/application-example.yaml src/main/resources/application.yaml

# Build jar
RUN mvn -B -DskipTests package

# Runtime stage
FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app

# Copy the jar file from the builder stage
ARG JAR_FILE=target/*.jar

# Copy the built jar file
COPY --from=builder /workspace/${JAR_FILE} app.jar

# Expose application port
EXPOSE 8080

# Set the entry point to run the jar file
ENTRYPOINT ["java","-jar","/app/app.jar"]
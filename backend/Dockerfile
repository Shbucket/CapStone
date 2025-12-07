# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copy all backend source code
COPY src ./src

# Make Maven wrapper executable
RUN chmod +x mvnw

# Build the project
RUN ./mvnw clean package -DskipTests

# Expose port Spring Boot runs on
EXPOSE 8080

# Run the Spring Boot app
CMD ["java", "-jar", "target/flashcardbe-0.0.1-SNAPSHOT.jar"]
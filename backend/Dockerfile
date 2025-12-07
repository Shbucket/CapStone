# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy the built JAR from your local target folder
COPY target/flashcardBE-0.0.1-SNAPSHOT.jar ./flashcardBE-0.0.1-SNAPSHOT.jar

# Expose port Spring Boot runs on
EXPOSE 8080

# Run the Spring Boot app
CMD ["java", "-jar", "flashcardBE-0.0.1-SNAPSHOT.jar"]

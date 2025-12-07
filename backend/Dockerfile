FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy Maven wrapper and pom
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copy backend source code
COPY src ./src

# Make Maven wrapper executable
RUN chmod +x mvnw

# Build the project
RUN ./mvnw clean package -DskipTests

# Expose port for local testing (optional, Render uses the env variable)
EXPOSE 8080

# Correct CMD using shell form to allow $PORT expansion
CMD sh -c "java -Dserver.port=\$PORT -jar target/flashcardBE-0.0.1-SNAPSHOT.jar"

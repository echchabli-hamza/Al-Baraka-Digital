FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy the JAR file from Maven build
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 1212

# Set JVM options
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
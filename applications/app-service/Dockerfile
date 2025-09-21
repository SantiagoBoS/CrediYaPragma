#JAVA 21
FROM eclipse-temurin:21-jre-jammy

# Directorio dentro del container
WORKDIR /app

# Copy the pre-built jar (built locally)
#COPY applications/app-service/build/libs/*.jar app.jar
COPY build/libs/CrediYa.jar app.jar

# Expose port
EXPOSE ${LOAN_PORT}

# Environment variables
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}
ENV DB_URL=${DB_URL}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV JWT_SECRET=${JWT_SECRET}
ENV JWT_EXPIRATION_MS=${JWT_EXPIRATION_MS}

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
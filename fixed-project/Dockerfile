# ============================================================
# Dockerfile — Online Course Platform
# Author: Bekzhanuly Nurmukhamed
# Multistage build: builder + runtime
# ============================================================

# ---- Stage 1: Builder ----
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /build

# Copy pom.xml first for dependency caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# Extract Spring Boot layers for optimized Docker image
RUN java -Djarmode=layertools -jar target/online-course-platform-1.0.0.jar extract --destination target/extracted

# ---- Stage 2: Runtime ----
FROM eclipse-temurin:17-jre-alpine AS runtime

# Security: run as non-root user
RUN addgroup -S courseapp && adduser -S courseapp -G courseapp

WORKDIR /app

# Create directories for uploads and logs with correct permissions
RUN mkdir -p /app/uploads /app/logs && \
    chown -R courseapp:courseapp /app

# Copy layers from builder (optimized for Docker cache)
COPY --from=builder --chown=courseapp:courseapp /build/target/extracted/dependencies/          ./
COPY --from=builder --chown=courseapp:courseapp /build/target/extracted/spring-boot-loader/    ./
COPY --from=builder --chown=courseapp:courseapp /build/target/extracted/snapshot-dependencies/ ./
COPY --from=builder --chown=courseapp:courseapp /build/target/extracted/application/           ./

USER courseapp

# Expose app port
EXPOSE 8080

# Health check — Spring Actuator
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget -qO- http://localhost:8080/api/actuator/health || exit 1

# JVM tuning for containers
ENV JAVA_OPTS="-Xms256m -Xmx512m \
               -XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:+UseG1GC \
               -XX:+OptimizeStringConcat \
               -Djava.security.egd=file:/dev/./urandom"

ENV FILE_UPLOAD_DIR=/app/uploads

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]

# Dockerfile
FROM eclipse-temurin:21-jdk-jammy

# Install nmap and dependencies
RUN apt-get update && \
    apt-get install -y nmap && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*
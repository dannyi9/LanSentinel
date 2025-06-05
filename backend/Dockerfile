FROM eclipse-temurin:21-jdk

RUN apt-get update && \
    apt-get install -y --no-install-recommends nmap && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

ARG JAR_NAME
COPY build/libs/${JAR_NAME} LanSentinel.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "LanSentinel.jar"]

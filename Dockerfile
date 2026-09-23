FROM eclipse-temurin:26.0.2_10-jdk-noble@sha256:abe71c9b7140affd9a80d6ed313c428e3393d4d18c235823b1745670d4702053 AS builder
WORKDIR /workspace
COPY . .
RUN ./gradlew --no-daemon build

FROM eclipse-temurin:26.0.2_10-jre-noble@sha256:0140636ef128fa014041fa12dae3a9de53b6a6c6f055271f4e6a7417ea34012c
WORKDIR /data
ENV JAVA_TOOL_OPTIONS="-XX:+ExitOnOutOfMemoryError --enable-native-access=ALL-UNNAMED"
COPY --from=builder --chown=10001:10001 /workspace/build/libs/fint-graphql.jar /data/fint-graphql.jar
USER 10001:10001
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/data/fint-graphql.jar"]

FROM eclipse-temurin:27_35-jdk-resolute@sha256:e665e17ae608e91002f8a6271842b61b9d436e8f23c2cab00ec4d2da9620ad43 AS builder
WORKDIR /workspace
COPY . .
RUN ./gradlew --no-daemon build

FROM eclipse-temurin:27_35-jre-resolute@sha256:492896faf0de16f8959951fcc97e84d6b1db7b1cbc327e732b4e740cd27409e9
WORKDIR /data
ENV JAVA_TOOL_OPTIONS="-XX:+ExitOnOutOfMemoryError --enable-native-access=ALL-UNNAMED"
COPY --from=builder --chown=10001:10001 /workspace/app/build/libs/fint-graphql.jar /data/fint-graphql.jar
USER 10001:10001
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/data/fint-graphql.jar"]

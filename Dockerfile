FROM eclipse-temurin:27_35-jdk-resolute@sha256:11fb95031e5720b9f6efc684658fde4f3d5a2318cced98602c882df0b328d22c AS builder
WORKDIR /workspace
COPY . .
RUN ./gradlew --no-daemon build

FROM eclipse-temurin:27_35-jre-resolute@sha256:7520762e9fac9c34ae0ef3a898ff0e9fc652b019860ab859728b17df41f2b497
WORKDIR /data
ENV JAVA_TOOL_OPTIONS="-XX:+ExitOnOutOfMemoryError --enable-native-access=ALL-UNNAMED"
COPY --from=builder --chown=10001:10001 /workspace/app/build/libs/fint-graphql.jar /data/fint-graphql.jar
USER 10001:10001
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/data/fint-graphql.jar"]

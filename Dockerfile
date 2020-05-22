FROM fint/graphql-cli:1.1.0 as generator
WORKDIR /
RUN ["/usr/bin/fint-graphql-cli", "--tag", "v3.5.0", "generate", "--exclude", "Fravar"]

FROM gradle:4.10.3-jdk8-alpine as builder
USER root
COPY . .
COPY --from=generator /graphql/schema/ src/main/resources/schema/
COPY --from=generator /graphql/model/ src/main/java/no/fint/graphql/model/
RUN gradle --no-daemon build

FROM gcr.io/distroless/java:8
ENV JAVA_TOOL_OPTIONS -XX:+ExitOnOutOfMemoryError
COPY --from=builder /home/gradle/build/deps/external/*.jar /data/
COPY --from=builder /home/gradle/build/deps/fint/*.jar /data/
COPY --from=builder /home/gradle/build/libs/fint-graphql-*.jar /data/fint-graphql.jar
CMD ["/data/fint-graphql.jar"]

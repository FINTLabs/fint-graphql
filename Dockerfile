FROM gradle:9.3.1-jdk25 AS builder
USER root
COPY . .
RUN gradle --no-daemon build

FROM gcr.io/distroless/java:25
ENV JAVA_TOOL_OPTIONS=-XX:+ExitOnOutOfMemoryError
COPY --from=builder /home/gradle/build/libs/fint-graphql*.jar /data/fint-graphql.jar
CMD ["/data/fint-graphql.jar"]

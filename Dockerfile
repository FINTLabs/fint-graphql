FROM gradle:6.9.4-jdk11 as builder
USER root
COPY . .
RUN gradle --no-daemon build

FROM gcr.io/distroless/java:11
ENV JAVA_TOOL_OPTIONS -XX:+ExitOnOutOfMemoryError
COPY --from=builder /home/gradle/build/libs/fint-graphql*.jar /data/fint-graphql.jar
CMD ["/data/fint-graphql.jar"]

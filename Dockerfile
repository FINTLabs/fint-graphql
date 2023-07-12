FROM ghcr.io/fintlabs/fint-graphql-cli:1.2.0 as generator
ARG TAG_NAME
WORKDIR /
RUN ["/usr/bin/fint-graphql-cli", "generate", "--exclude", "Fravar","generate", "--exclude", "Fravarstype", "--exclude", "OTUngdom", "--exclude-schema", "OTUngdom", "--exclude-schema", "OTStatus", "--exclude-schema", "OTEnhet"]

FROM gradle:7.6.2-jdk11-alpine as builder
ARG VERSION
USER root
COPY . .
COPY --from=generator /graphql/schema/ src/main/resources/schema/
COPY --from=generator /graphql/model/ src/main/java/no/fint/graphql/model/
COPY PersonService.txt src/main/java/no/fint/graphql/model/felles/person/PersonService.java
RUN gradle --no-daemon -Pversion=${VERSION} build

FROM gcr.io/distroless/java:11
ENV JAVA_TOOL_OPTIONS -XX:+ExitOnOutOfMemoryError
COPY --from=builder /home/gradle/build/deps/external/*.jar /data/
COPY --from=builder /home/gradle/build/deps/fint/*.jar /data/
COPY --from=builder /home/gradle/build/libs/fint-graphql-*.jar /data/fint-graphql.jar
CMD ["/data/fint-graphql.jar"]

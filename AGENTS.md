# Repository Guidelines

## Project Structure & Module Organization

This Gradle repository contains two Java modules:

- `app/`: Spring Boot GraphQL service. Handwritten Java lives in `src/main/java/no/fint/graphql`, configuration in `src/main/resources`, and Spock tests in `src/test/groovy`.
- `model-generator/`: build-only Java generator, with JUnit tests in `src/test/java` and reviewed fixtures in `src/test/resources/baseline`.

Generated Java and GraphQL schemas live under `app/build/generated/graphql/`. Change the generator rather than editing generated files. Keep the handwritten `PersonService` outside generation.

## Build, Test, and Development Commands

Use the Gradle wrapper. Java 27 is the target; Gradle can start on JDK 21+ and provision the toolchain.

- `./gradlew clean build`: generate sources, compile, and test both modules.
- `./gradlew :app:test`: run application tests.
- `./gradlew :model-generator:test`: run generator and Gradle TestKit tests.
- `./gradlew :app:test --tests '*GraphQLQueryTimeoutSpec'`: run a focused specification.
- `./gradlew :app:generateGraphql`: regenerate sources and schemas.
- `./gradlew :app:bootRun`: start locally; use `/graphql` and `/graphiql`.

The first build requires network access for dependencies and the pinned model. Cached builds support `--offline`.

## Coding Style & Naming Conventions

Follow surrounding Java and Groovy code: four-space indentation, Java semicolons, `UpperCamelCase` classes, `lowerCamelCase` members, and lowercase packages. Use descriptive service/configuration names and behavior-oriented Spock feature names. No dedicated formatter or lint plugin is configured.

## Testing Guidelines

Application tests use Spock, Reactor Test, and local MockWebServer instances; generator tests use JUnit Jupiter and Gradle TestKit. Name tests `*Spec.groovy` or `*Test.java`. Add regression coverage for changed behavior, especially authentication, timeouts, cache isolation, and generated schema compatibility. No numeric coverage threshold is configured. Review baseline changes explicitly; never refresh fixtures merely to silence failures.

## Commit & Pull Request Guidelines

Follow history with concise imperative subjects and prefixes such as `fix:`, `feat:`, `build:`, `ci:`, and `docs:`. PRs should explain the problem, resulting behavior, and validation commands/results; link related issues. Call out public schema, dependency, and configuration changes. Run `./gradlew clean build` before requesting review.

## Security & Configuration

Keep credentials out of source and logs. Configure local endpoints and the OAuth issuer through external properties. Preserve bearer authentication, role-based resource access, and query-scoped cache isolation.

## Extra Information
* The API this app queries is slow, and big data is queried. A user quering this API has an upper limit of 120 seconds due to our firewall. That is why timeouts are after 110 seconds.

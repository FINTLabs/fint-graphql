#!/usr/bin/env bash

docker run \
  -v "${PWD}/src/main/resources/schema:/src/graphql/schema" \
  -v "${PWD}/src/main/java/no/fint/graphql/model:/src/graphql/model" \
  fint/graphql-cli:latest \
  generate --exclude Fravar

cp PersonService.txt src/main/java/no/fint/graphql/model/felles/person/PersonService.java

echo Schema and models generated.  Please remember to clean up ${PWD}/src/main/{resources/schema,java/no/fint/graphql/model} before commit.

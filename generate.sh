#!/usr/bin/env bash

docker run \
  -v ${PWD}/src/main/resources/schema:/src/graphql/schema \
  -v ${PWD}/src/main/java/no/fint/graphql/model:/src/graphql/model \
  fint/graphql-cli:1.1.0 \
  generate --exclude Fravar

echo Schema and models generated.  Please remember to clean up ${PWD}/src/main/{resources/schema,java/no/fint/graphql/model} before commit.

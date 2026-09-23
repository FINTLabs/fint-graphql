#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"

# PersonService.txt is the custom implementation, kept in sync with the tested
# source by PersonServiceSpec. Never deliver the generator's single-source service.
test -r PersonService.txt

# Stage output for review: regeneration must not overwrite hand-maintained
# person merging, relationship error handling, or the published schema.
output_dir="$(mktemp -d "${TMPDIR:-/tmp}/fint-graphql-generation.XXXXXX")"
mkdir -p "$output_dir/schema" "$output_dir/model"
model_version="$(sed -n 's/^apiVersion=//p' gradle.properties)"
: "${model_version:?apiVersion must be set in gradle.properties}"
cli_image="${FINT_GRAPHQL_CLI_IMAGE:-fint-graphql-cli:2.0.0}"
printf 'Staging model %s in %s\n' "$model_version" "$output_dir"
docker run --rm \
  -v "$output_dir/schema:/src/graphql/schema" \
  -v "$output_dir/model:/src/graphql/model" \
  "$cli_image" \
  --tag "v${model_version}" \
  generate --exclude Fravar --exclude Fravarstype
person_service="$output_dir/model/model/person/PersonService.java"
if [[ ! -f "$person_service" ]]; then
  printf 'Expected generated PersonService at %s; refusing to stage an incompatible model layout.\n' "$person_service" >&2
  exit 1
fi
cp PersonService.txt "$person_service"
printf 'Generated Spring GraphQL candidates in %s\nReview and merge changes into src; keep existing custom service and resolver behavior.\n' "$output_dir"

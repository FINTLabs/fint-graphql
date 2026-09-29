# Generator migration reference data

These archives are independent, frozen references. They are test inputs, never
templates or inputs to production generation. `GenerationParityTest` compares
the complete file inventory, Java syntax trees (JDK compiler parser), and GraphQL
syntax trees (GraphQL Java). Comments and formatting are intentionally ignored;
string literals, statements, annotations, schema types, fields, arguments and
nullability are compared.

## Provenance

Captured 2026-09-29:

- Information model: `FINTLabs/fint-informasjonsmodell`, tag `v4.1.0`, file
  `FINT-informasjonsmodell.xml`, raw Windows-1252 XML, 3,091,516 bytes.
- Raw model SHA-256:
  `ff2e292de2ccb6fe9c334fe46ae1cb3b1eae2b919545345731b2dd3247b9b826`.
- `legacy.zip`: output from `fint-graphql-cli` 2.0.0, repository commit
  `6a28c91c022c86cc2a26a5f353d36d4428380492`, run with
  `--tag v4.1.0 generate --exclude Fravar --exclude Fravarstype`.
- `application.zip`: `src/main/java/no/fint/graphql/model` and
  `src/main/resources/schema` from `fint-graphql` commit
  `a3f524782900c732138330ab0a5723f0e8f80439`, before the module migration.
- Each archive contains the same 394 relative paths: `model/...` and `schema/...`.
- Archive SHA-256: `legacy.zip`
  `aa8cecfb4333e6fca3721a2cbbf8f77d4df8a619ca8f26653f258a07b2986d95`;
  `application.zip`
  `9614d926efb871e6d3e6b95410565b34f0ba1539bbf5de14d06783986d77ed3d`.

The archives have sorted entries and fixed ZIP timestamps (1980-01-01), and
contain the original file bytes. They can be inspected with `unzip -l` and
`unzip -p` without running Go or downloading the model again.

## Deliberate differences

There is exactly one functional difference between the historical references:
`model/model/person/PersonService.java`. The application merges data from
administration and education, preserves cached input objects and supports a
successful source when the other source fails. The Go output uses a single
endpoint. Production generation omits this service, and the maintained Java
implementation in `app` supplies it. The parity test originally verified the unmodified legacy generator output and
the complete application output including that file. The reviewed template
overlay below now describes the intentional implementation changes.

All other 393 files retain the same Java and GraphQL syntax. Java generation
improves error handling, ID-based reference resolution, XML security, deterministic
output replacement, cache integrity and build integration without changing the
public API.

## Future model changes

Keep `legacy.zip` as the historical migration reference. When upgrading
`apiVersion`, also update `modelSha256` and adapt the parity test so that the
historical parity case still uses the pinned 4.1.0 model through the verified
download/cache mechanism. Add a separately reviewed reference for the new model
and update the application's approved contract. Do not silently overwrite this
historical evidence or regenerate expected results as part of a normal test run.

For a new reference, review the full Java and SDL changes, record the model URL,
version, raw SHA-256, generator commit and any intentional contract changes, then
archive the reviewed files with sorted names and fixed ZIP timestamps. New
behavior requires regression tests in addition to updating reference data.

The port is derived from the MIT-licensed FINTLabs generator. Its original
copyright and permission notice are retained in `model-generator/LICENSE`.

## URI template implementation reference

`uri-templates.zip` is a separate, reviewed overlay captured 2026-09-29 from
this change's working tree, based on commit
`b5fe61957f5f651417bc036bc262988075273f32`. It uses the same pinned model and
SHA-256 above. The two historical archives are unchanged.

- Archive SHA-256: `770d227ac39bef4dc1cc42e8cbad35c5b8b9efce3c916ff6f47f7e5f2fd0219c`.
- 106 entries with sorted paths and fixed ZIP timestamps: `common/` contains
  103 generated services and `ResourceRoutes.java`; `legacy/` and `application/`
  each contain their respective Person service.
- All 104 generated service changes were checked against the historical source
  using an independent transformation: values move into `Map.of("value", value)`,
  URI paths use `{value}`, and response normalization is shared by both lookup
  forms. Existing Feide normalization is preserved.
- The handwritten Person service retains merging/error handling and switches
  both lookups to the template overload. The new catalog contains 228 collection
  and identifier templates, including both Person sources. Configured prefixes
  affect matching, while metric labels use canonical model paths.

`GenerationParityTest` overlays only these explicitly reviewed service/catalog
files. All other Java and GraphQL syntax still matches the historical references;
the resulting inventory is 395 files. New generator and application regression
tests cover behavior separately. Reference archives are never regenerated during
tests. Future changes must be reviewed before this overlay is updated.

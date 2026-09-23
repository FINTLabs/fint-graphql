#!/usr/bin/env python3
"""Convert generator 1.3.1 output to explicit Spring GraphQL mappings, idempotently."""
import argparse
import re
from pathlib import Path


# Match the optimized model imports: third-party/application imports first,
# java/javax separately, and wildcard imports for five or more FINT resources.
IMPORT = re.compile(r'^import\s+(static\s+)?([\w.*]+);[ \t]*\n?', re.M)
JAVA_NON_CODE = re.compile(r'"""[\s\S]*?"""|"(?:\\.|[^"\\])*"|\'(?:\\.|[^\'\\])*\'|//[^\n]*|/\*[\s\S]*?\*/')
MAPPING_PACKAGE = 'org.springframework.graphql.data.method.annotation.'
RESOURCE_PACKAGE = 'no.novari.fint.model.resource.'


def optimize_imports(text: str) -> str:
    """Normalize generator imports without changing declarations or method bodies.

    This is a lexical cleanup for the generator's simple Java classes, not a Java
    symbol resolver. Existing wildcards are retained; new ones are limited to
    FINT resource packages, as in the checked-in model classes.
    """
    imports = list(IMPORT.finditer(text))
    if not imports:
        return text
    code = JAVA_NON_CODE.sub(' ', IMPORT.sub('', text))
    identifiers = set(re.findall(r'\b\w+\b', code))
    package = re.search(r'^package\s+([\w.]+);', code, re.M)
    package = package[1] if package else None
    used = set()
    for match in imports:
        is_static, name = bool(match[1]), match[2]
        owner, simple_name = name.rsplit('.', 1)
        if simple_name == '*' or (simple_name in identifiers and (is_static or owner != package)):
            used.add((is_static, name))

    resources = {}
    owners_by_name = {}
    for is_static, name in used:
        owner, simple_name = name.rsplit('.', 1)
        if not is_static and simple_name != '*':
            owners_by_name.setdefault(simple_name, set()).add(owner)
            if name.startswith(RESOURCE_PACKAGE) and simple_name.endswith('Resource'):
                resources.setdefault(owner, set()).add(name)
    for owner, names in resources.items():
        # Retain explicit imports when a simple name occurs in multiple packages.
        if len(names) >= 5 and all(len(owners_by_name[name.rsplit('.', 1)[1]]) == 1 for name in names):
            used.difference_update((False, name) for name in names)
            used.add((False, owner + '.*'))

    groups = [[], [], [], []]
    for is_static, name in sorted(used):
        if not is_static and not name.endswith('.*') and (False, name.rsplit('.', 1)[0] + '.*') in used:
            # An explicit import disambiguates conflicting names even beside a wildcard.
            if len(owners_by_name.get(name.rsplit('.', 1)[1], ())) <= 1:
                continue
        group = 3 if is_static else 1 if name.startswith('javax.') else 2 if name.startswith('java.') else 0
        groups[group].append(f'import {"static " if is_static else ""}{name};')
    block = '\n\n'.join('\n'.join(group) for group in groups if group)
    prefix = text[:imports[0].start()].rstrip()
    # Retain any comments between imports rather than silently deleting them.
    between = IMPORT.sub('', text[imports[0].start():imports[-1].end()]).strip()
    suffix = text[imports[-1].end():].lstrip('\n')
    return '\n\n'.join(part for part in (prefix, between, block, suffix) if part)


def migrate(source: Path, schema: Path):
    # The old generator emits two empty, unreferenced object definitions.
    # Kickstart ignored them; GraphQL Java validates every supplied definition.
    for name in ('grepreferanse', 'vigoreferanse'):
        path = schema / 'model' / (name + '.graphqls')
        if path.exists() and re.fullmatch(r'\s*type\s+\w+\s*\{\s*\}\s*', path.read_text()):
            path.unlink()
    types = {}
    for path in sorted(schema.rglob('*.graphqls')):
        for name, body in re.findall(r'type\s+(\w+)\s*\{([^}]+)\}', path.read_text()):
            types[name] = set(re.findall(r'^\s*(\w+)\s*(?:\([^\n]*\))?\s*:', body, re.M))
    count = 0
    for path in sorted(source.rglob('*.java')):
        text = path.read_text()
        original = text
        query = 'implements GraphQLQueryResolver' in text
        match = re.search(r'implements GraphQLResolver<(\w+)Resource>', text)
        if not query and not match:
            # Services, Endpoints and already converted controllers use the same
            # import layout. Never rewrite their custom implementation logic.
            text = optimize_imports(text)
            if text != original:
                path.write_text(text)
            continue
        type_name = 'Query' if query else next(n for n in types if n.lower() == match[1].lower())
        text = re.sub(r'import (?:com.coxautodev.graphql.tools|graphql.kickstart.tools).GraphQL\w*Resolver;\n', '', text)
        text = text.replace('org.springframework.stereotype.Component', 'org.springframework.stereotype.Controller')
        text = text.replace('@Component(', '@Controller(')
        mapping_imports = set()
        text = re.sub(r' implements GraphQL(?:QueryResolver|Resolver<\w+>)', '', text)

        def annotate(m):
            declaration, method, args = m.groups()
            field = method.removeprefix('get')
            matches = [f for f in types[type_name] if f.lower() == field.lower()]
            if not matches and query:
                # Hidden queries still follow the current lowercase method naming,
                # but must not expose a new field absent from the public schema.
                return f'{declaration}{field.lower()}({args}) {{'
            if not matches:
                raise ValueError(f"{path}: no {type_name}.{field} in schema")
            field = matches[0]
            if query:
                method = field
                args = re.sub(r'\bString (\w+)', lambda a: f'@Argument("{a[1]}") String {a[1]}', args)
                annotation = f'@QueryMapping(name = "{field}")'
                mapping_imports.add('QueryMapping')
                if '@Argument(' in args:
                    mapping_imports.add('Argument')
            else:
                annotation = f'@SchemaMapping(typeName = "{type_name}", field = "{field}")'
                mapping_imports.add('SchemaMapping')
            return f'    {annotation}\n{declaration}{method}({args}) {{'

        text = re.sub(r'(    public CompletionStage<[^\n]+>\s+)(\w+)\((.*?)\) \{', annotate, text, flags=re.S)
        # Insert independently of DataFetchingEnvironment: a no-argument mapping
        # need not import it. Cleanup below sorts and deduplicates all imports.
        if mapping_imports:
            imports = ''.join(f'import {MAPPING_PACKAGE}{name};\n' for name in sorted(mapping_imports))
            first_import = IMPORT.search(text)
            if first_import:
                text = text[:first_import.start()] + imports + text[first_import.start():]
            else:
                package = re.search(r'^package\s+[\w.]+;\s*', text, re.M)
                position = package.end() if package else 0
                text = text[:position] + imports + '\n' + text[position:]
        text = optimize_imports(text)
        path.write_text(text)
        count += 1
    print(f'Converted {count} resolver files')


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--source', type=Path, default=Path('src/main/java/no/fint/graphql/model'))
    parser.add_argument('--schema', type=Path, default=Path('src/main/resources/schema'))
    args = parser.parse_args()
    migrate(args.source, args.schema)

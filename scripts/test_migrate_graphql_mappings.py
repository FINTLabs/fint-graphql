"""Regression fixtures for the current model layout and optimized Java imports.

All migrations in this suite target temporary directories, never checked-in Java.
"""
import importlib.util
import tempfile
import unittest
from pathlib import Path

spec = importlib.util.spec_from_file_location('migration', Path(__file__).with_name('migrate-graphql-mappings.py'))
migration = importlib.util.module_from_spec(spec)
spec.loader.exec_module(migration)


class MappingMigrationTest(unittest.TestCase):
    def setUp(self):
        directory = tempfile.TemporaryDirectory()
        self.addCleanup(directory.cleanup)
        self.root = Path(directory.name)
        self.schema = self.root / 'schema'
        self.source = self.root / 'model'
        self.schema.mkdir()
        self.source.mkdir()
        (self.schema / 'root.graphqls').write_text('''type Query {
    otungdom(systemId: String): OtUngdom
    ping: String
}
type OtUngdom {
    avlagtprove: AvlagtProve
}
type AvlagtProve {
    name: String
}
''')

    def write_source(self, relative_path, text):
        path = self.source / relative_path
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text(text)
        return path

    def assert_idempotent(self):
        before = {p: p.read_bytes() for p in self.root.rglob('*') if p.is_file()}
        migration.migrate(self.source, self.schema)
        after = {p: p.read_bytes() for p in self.root.rglob('*') if p.is_file()}
        self.assertEqual(before, after)

    def test_explicit_names_arguments_and_idempotence(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            schema = root / 'schema'
            source = root / 'model'
            schema.mkdir()
            source.mkdir()
            (schema / 'root.graphqls').write_text('type Query { otungdom(systemId: String): OtUngdom }\n'
                                               'type OtUngdom { avlagtprove: AvlagtProve }\n'
                                               'type AvlagtProve { name: String }')
            query = source / 'OtUngdomQueryResolver.java'
            query.write_text('''import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;
@Component("otQuery")
public class OtUngdomQueryResolver implements GraphQLQueryResolver {
    public CompletionStage<OtUngdomResource> getOtUngdom(String systemId, DataFetchingEnvironment dfe) {
        return service.get(systemId, dfe).toFuture();
    }
}''')
            resolver = source / 'OtUngdomResolver.java'
            resolver.write_text('''import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;
@Component("otResolver")
public class OtUngdomResolver implements GraphQLResolver<OtUngdomResource> {
    public CompletionStage<AvlagtProveResource> getAvlagtProve(OtUngdomResource source, DataFetchingEnvironment dfe) {
        return service.get(source, dfe).toFuture();
    }
}''')
            migration.migrate(source, schema)
            before = [query.read_text(), resolver.read_text()]
            self.assertIn('@Argument("systemId") String systemId', before[0])
            self.assertIn('@QueryMapping(name = "otungdom")', before[0])
            self.assertIn('public CompletionStage<OtUngdomResource> otungdom(', before[0])
            self.assertIn('@SchemaMapping(typeName = "OtUngdom", field = "avlagtprove")', before[1])
            self.assertIn('public CompletionStage<AvlagtProveResource> getAvlagtProve(', before[1])
            self.assertIn('return service.get(source, dfe).toFuture();', before[1])
            self.assertNotIn('coxautodev', ''.join(before))
            migration.migrate(source, schema)
            self.assertEqual(before, [query.read_text(), resolver.read_text()])

    def test_current_query_layout_and_exact_optimized_imports(self):
        query = self.write_source('model/otungdom/OtUngdomQueryResolver.java', '''
package no.fint.graphql.model.model.otungdom;

import java.util.List;
import java.util.concurrent.CompletionStage;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import reactor.core.publisher.Mono;
import no.novari.fint.model.resource.utdanning.ot.OtUngdomResource;
import org.apache.commons.lang3.StringUtils;
import lombok.extern.slf4j.Slf4j;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.otungdom.OtUngdomService;

@Component("modelOtUngdomQueryResolver")
@Slf4j
public class OtUngdomQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private OtUngdomService service;

    public CompletionStage<OtUngdomResource> otungdom(
            String systemId,
            DataFetchingEnvironment dfe) {
        log.info("New Query for OtUngdom");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getOtUngdomResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<OtUngdomResource>empty().toFuture();
    }
}
''')
        body = query.read_text().split('        log.info', 1)[1]
        migration.migrate(self.source, self.schema)
        text = query.read_text()
        self.assertIn('''import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.ot.OtUngdomResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelOtUngdomQueryResolver")''', text)
        self.assertIn('@QueryMapping(name = "otungdom")', text)
        self.assertIn('@Argument("systemId") String systemId', text)
        self.assertIn('public CompletionStage<OtUngdomResource> otungdom(', text)
        self.assertNotIn('import java.util.List;', text)
        self.assertNotIn('import no.fint.graphql.model.model.otungdom.OtUngdomService;', text)
        self.assertNotIn('kickstart', text)
        self.assertEqual(body, text.split('        log.info', 1)[1])
        self.assert_idempotent()

    def test_hidden_query_has_no_mapping_imports(self):
        query = self.write_source('model/funksjon/FunksjonQueryResolver.java', '''package example;

import graphql.schema.DataFetchingEnvironment;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletionStage;

@Component("modelFunksjonQueryResolver")
public class FunksjonQueryResolver implements GraphQLQueryResolver {
    public CompletionStage<FunksjonResource> getFunksjon(
            String systemId,
            DataFetchingEnvironment dfe) {
        return service.getFunksjonResourceById("systemid", systemId, dfe).toFuture();
    }
}
''')
        migration.migrate(self.source, self.schema)
        text = query.read_text()
        self.assertIn('@Controller("modelFunksjonQueryResolver")', text)
        self.assertNotIn('QueryMapping', text)
        self.assertNotIn('Argument', text)
        self.assertNotIn('GraphQLQueryResolver', text)
        self.assertIn('public CompletionStage<FunksjonResource> funksjon(', text)
        self.assertIn('String systemId,', text)
        self.assert_idempotent()

    def test_no_argument_query_does_not_need_data_fetching_environment_import(self):
        query = self.write_source('PingQueryResolver.java', '''package example;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletionStage;

@Component("pingQueryResolver")
public class PingQueryResolver implements GraphQLQueryResolver {
    public CompletionStage<String> ping() {
        return service.ping();
    }
}
''')
        migration.migrate(self.source, self.schema)
        text = query.read_text()
        self.assertIn('import org.springframework.graphql.data.method.annotation.QueryMapping;', text)
        self.assertIn('@QueryMapping(name = "ping")', text)
        self.assertNotIn('Argument', text)
        self.assertNotIn('DataFetchingEnvironment', text)
        self.assert_idempotent()

    def test_resource_wildcard_threshold_and_unused_import_removal(self):
        package = 'no.novari.fint.model.resource.administrasjon.kodeverk'
        resources = ['Ramme', 'Funksjon', 'Art', 'Anlegg', 'Ansvar']
        for count in (4, 5):
            with self.subTest(resource_count=count):
                imports = '\n'.join(f'import {package}.{name}Resource;' for name in resources)
                fields = '\n'.join(f'    private {name}Resource {name.lower()};' for name in resources[:count])
                original = f'''package example;

{imports}
import java.util.List;

public class Model {{
    // List and AnsvarResource in a comment must not count as usages.
    private String description = "List AnsvarResource";
{fields}
}}
'''
                text = migration.optimize_imports(original)
                if count == 5:
                    self.assertIn(f'import {package}.*;', text)
                    for name in resources:
                        self.assertNotIn(f'import {package}.{name}Resource;', text)
                else:
                    self.assertNotIn(f'import {package}.*;', text)
                    for name in resources[:count]:
                        self.assertIn(f'import {package}.{name}Resource;', text)
                    self.assertNotIn(f'import {package}.AnsvarResource;', text)
                self.assertNotIn('import java.util.List;', text)
                self.assertEqual(original.split('public class', 1)[1], text.split('public class', 1)[1])
                self.assertEqual(text, migration.optimize_imports(text))

    def test_existing_relationship_mapping_keeps_custom_code_and_used_imports(self):
        resolver = self.write_source('model/rolle/RolleResolver.java', '''package no.fint.graphql.model.model.rolle;

import java.util.stream.Collectors;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;
import java.util.Optional;
import java.util.List;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.beans.factory.annotation.Autowired;
import no.novari.fint.model.resource.administrasjon.fullmakt.RolleResource;
import no.novari.fint.model.resource.administrasjon.fullmakt.FullmaktResource;
import no.novari.fint.model.resource.Link;
import no.fint.graphql.model.model.fullmakt.FullmaktService;
import graphql.schema.DataFetchingEnvironment;

@Controller("modelRolleResolver")
public class RolleResolver {
    @Autowired
    private FullmaktService fullmaktService;

    @SchemaMapping(typeName = "Rolle", field = "fullmakt")
    public CompletionStage<List<FullmaktResource>> getFullmakt(RolleResource rolle, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(rolle.getFullmakt()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> fullmaktService.getFullmaktResource(href, dfe)
                        .map(Optional::of)
                        .onErrorResume(WebClientResponseException.class,
                                ex -> Mono.just(Optional.empty())),
                        8, 1)
                .collectList()
                .map(list -> list.stream()
                        .map(opt -> opt.orElse(null))
                        .collect(Collectors.toList()))
                .toFuture();
    }
}
''')
        original = resolver.read_text()
        migration.migrate(self.source, self.schema)
        text = resolver.read_text()
        self.assertEqual(original.split('@Controller', 1)[1], text.split('@Controller', 1)[1])
        self.assertEqual(set(migration.IMPORT.findall(original)), set(migration.IMPORT.findall(text)))
        self.assertIn('''import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;''', text)
        self.assertEqual(1, text.count('@SchemaMapping('))
        self.assert_idempotent()

    def test_services_and_endpoints_are_normalized_without_controller_conversion(self):
        service = self.write_source('model/person/PersonService.java', '''package example;

import reactor.core.publisher.Mono;
import java.util.List;
import org.springframework.stereotype.Service;

@Service("modelPersonService")
public class PersonService {
    public Mono<String> getPersonResource(String url) {
        return Mono.just(url);
    }
}
''')
        endpoints = self.write_source('Endpoints.java', '''package example;

import org.springframework.stereotype.Component;
import lombok.Getter;

@Getter
@Component
public class Endpoints {
    private String felles;
}
''')
        service_body = service.read_text().split('@Service', 1)[1]
        migration.migrate(self.source, self.schema)
        self.assertIn('import org.springframework.stereotype.Service;\nimport reactor.core.publisher.Mono;', service.read_text())
        self.assertNotIn('import java.util.List;', service.read_text())
        self.assertEqual(service_body, service.read_text().split('@Service', 1)[1])
        self.assertIn('import lombok.Getter;\nimport org.springframework.stereotype.Component;', endpoints.read_text())
        self.assertIn('@Component', endpoints.read_text())
        self.assertNotIn('Controller', endpoints.read_text())
        self.assert_idempotent()

    def test_existing_wildcard_and_static_imports_are_preserved(self):
        text = migration.optimize_imports('''package example;

import static java.util.Collections.emptyList;
import no.novari.fint.model.resource.administrasjon.kodeverk.*;
import no.novari.fint.model.resource.administrasjon.kodeverk.RammeResource;
import java.util.List;
import static java.util.Collections.singletonList;

public class Model {
    public List<RammeResource> rammer() {
        return emptyList();
    }
}
''')
        self.assertIn('''import no.novari.fint.model.resource.administrasjon.kodeverk.*;

import java.util.List;

import static java.util.Collections.emptyList;''', text)
        self.assertNotIn('import no.novari.fint.model.resource.administrasjon.kodeverk.RammeResource;', text)
        self.assertNotIn('singletonList', text)
        self.assertEqual(text, migration.optimize_imports(text))


if __name__ == '__main__':
    unittest.main()

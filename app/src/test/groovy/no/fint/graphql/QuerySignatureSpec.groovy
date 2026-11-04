package no.fint.graphql

import spock.lang.Specification
import spock.lang.Unroll

class QuerySignatureSpec extends Specification {
    private static final String SECRET = '12345678901'

    @Unroll
    def "#description never reaches the log"() {
        when:
        def signature = QuerySignature.of(document, null).orElseThrow()

        then:
        !signature.text().contains(SECRET)
        signature.text().contains('fodselsnummer')

        where:
        description            | document
        'an inline value'      | """{ person(fodselsnummer: "$SECRET") { fodselsnummer } }"""
        'a block string'       | "{ person(fodselsnummer: \"\"\"$SECRET\"\"\") { fodselsnummer } }"
        'a number'             | "{ person(fodselsnummer: $SECRET) { fodselsnummer } }"
        'a list value'         | """{ person(fodselsnummer: ["$SECRET"]) { fodselsnummer } }"""
        'an object value'      | """{ person(filter: {id: "$SECRET"}) { fodselsnummer } }"""
        'a variable default'   | """query(\$id: String = "$SECRET") { person(fodselsnummer: \$id) { fodselsnummer } }"""
        'a directive argument' | """{ person @tag(id: "$SECRET") { fodselsnummer } }"""
        'a variable name'      | """query(\$p$SECRET: String) { person(fodselsnummer: \$p$SECRET) { fodselsnummer } }"""
        'an alias'             | """{ p$SECRET: person { fodselsnummer } }"""
        'a comment'            | """{ # $SECRET\n person { fodselsnummer } }"""
    }

    def "queries differing only in values and aliases share a hash"() {
        when:
        def first = QuerySignature.of('{ a: skole(skolenummer: "39018") { navn } }', null).orElseThrow()
        def second = QuerySignature.of('{ b: skole(skolenummer: "39020") { navn } }', null).orElseThrow()
        def other = QuerySignature.of('{ a: skole(skolenummer: "39018") { skolenummer } }', null).orElseThrow()

        then:
        first.hash() == second.hash()
        first.text() == second.text()
        first.hash() != other.hash()
    }

    def "a lone named operation keeps its text when the request names no operation"() {
        when:
        def signature = QuerySignature.of('query Elever { skole { navn } }', null).orElseThrow()

        then:
        signature.operation() == 'Elever'
        signature.text().contains('skole')
    }

    def "only the requested operation is kept"() {
        when:
        def signature = QuerySignature.of('query A { skole { navn } } query B { elev { navn } }', 'B').orElseThrow()

        then:
        signature.operation() == 'B'
        signature.text().contains('elev')
        !signature.text().contains('skole')
    }

    def "very long queries are capped but keep the hash of the whole query"() {
        given:
        def fields = (1..3000).collect { "field$it" }.join(' ')

        when:
        def full = QuerySignature.of("{ skole { $fields } }", null).orElseThrow()
        def changedAtTheEnd = QuerySignature.of("{ skole { $fields fieldLast } }", null).orElseThrow()

        then:
        full.text().length() == 10_000
        full.hash() != changedAtTheEnd.hash()
    }

    @Unroll
    def "no signature for #description"() {
        expect:
        QuerySignature.of(document, operation).isEmpty()

        where:
        description            | document          | operation
        'a syntax error'       | '{ skole { '      | null
        'an empty document'    | ''                | null
        'no document'          | null              | null
    }
}

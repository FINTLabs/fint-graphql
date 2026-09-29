package no.fint.graphql

import spock.lang.Specification

class FintPropertyUtilsSpec extends Specification {
    def "merge fills missing values while retaining existing values and excluded links"() {
        given:
        def source = new PersonValues(name: 'new', description: 'missing', links: ['new'])
        def target = new PersonValues(name: 'existing', links: ['existing'])

        when:
        FintPropertyUtils.copyProperties(source, target, { it.name != 'links' },
                { incoming, existing -> existing == null ? incoming : existing })

        then:
        target.name == 'existing'
        target.description == 'missing'
        target.links == ['existing']
        source.name == 'new'
    }

    static class PersonValues {
        String name
        String description
        List<String> links
    }
}

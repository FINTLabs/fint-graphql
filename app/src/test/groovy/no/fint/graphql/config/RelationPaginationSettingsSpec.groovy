package no.fint.graphql.config

import org.springframework.boot.context.properties.bind.BindException
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.boot.context.properties.source.ConfigurationPropertySource
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource
import org.springframework.core.NestedExceptionUtils
import spock.lang.Specification

import java.util.OptionalInt

class RelationPaginationSettingsSpec extends Specification {
    private static final String PREFIX = 'fint.graphql.relation-pagination'

    def "defaults apply when nothing is configured"() {
        given:
        def settings = new RelationPaginationSettings()

        expect:
        settings.threshold == 50
        settings.pageSize == 500
        settings.configuredThreshold('Elevforhold', 'elev') == OptionalInt.empty()
    }

    def "properties bind, and relations are found whatever the case of type and relation"() {
        when:
        def settings = bind((PREFIX + '.threshold'): '10', (PREFIX + '.page-size'): '200',
                (PREFIX + '.relations.elevforhold.elev'): '1',
                (PREFIX + '.relations.Skole.SkoleeierType'): '0')

        then:
        settings.threshold == 10
        settings.pageSize == 200
        settings.configuredThreshold('Elevforhold', 'elev') == OptionalInt.of(1)
        settings.configuredThreshold('skole', 'skoleeierType') == OptionalInt.of(0)
        settings.configuredThreshold('Elevforhold', 'skole') == OptionalInt.empty()
        settings.configuredThreshold('Elev', 'elev') == OptionalInt.empty()
    }

    def "invalid values stop the application from binding: #property=#value"() {
        when:
        bind((PREFIX + '.' + property): value)

        then:
        def exception = thrown(BindException)
        def cause = NestedExceptionUtils.getRootCause(exception)
        cause instanceof IllegalArgumentException
        cause.message.contains(property)

        where:
        property                     | value
        'threshold'                  | '-1'
        'page-size'                  | '0'
        'page-size'                  | '-1'
        'relations.elevforhold.elev' | '-1'
    }

    private static RelationPaginationSettings bind(Map<String, String> properties) {
        new Binder([new MapConfigurationPropertySource(properties)] as ConfigurationPropertySource[])
                .bind(PREFIX, RelationPaginationSettings)
                .orElseGet({ new RelationPaginationSettings() })
    }
}

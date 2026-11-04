package no.fint.graphql.config

import org.springframework.boot.context.properties.bind.BindException
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.boot.context.properties.source.ConfigurationPropertySource
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource
import org.springframework.core.NestedExceptionUtils
import spock.lang.Specification

class RelationPaginationSettingsSpec extends Specification {

    def "defaults apply when nothing is configured"() {
        given:
        def settings = new RelationPaginationSettings()

        expect:
        settings.threshold == 50
        settings.pageSize == 500
    }

    def "properties bind"() {
        when:
        def settings = bind('threshold': '10', 'page-size': '200')

        then:
        settings.threshold == 10
        settings.pageSize == 200
    }

    def "invalid configuration stops the application from binding: #property=#value"() {
        when:
        bind((property): value)

        then:
        def exception = thrown(BindException)
        def cause = NestedExceptionUtils.getRootCause(exception)
        cause instanceof IllegalArgumentException
        cause.message.contains(property)

        where:
        property    | value
        'threshold' | '-1'
        'page-size' | '0'
        'page-size' | '-1'
    }

    private static RelationPaginationSettings bind(Map<String, String> properties) {
        def prefixed = properties.collectEntries { key, value -> ["fint.graphql.relation-pagination.$key".toString(), value] }
        new Binder([new MapConfigurationPropertySource(prefixed)] as ConfigurationPropertySource[])
                .bind('fint.graphql.relation-pagination', RelationPaginationSettings)
                .orElseGet({ new RelationPaginationSettings() })
    }
}

package de.schaefer.version


import spock.lang.Specification

class NpmVersionTest extends Specification {

    def "Version 1.2.3 wird korrekt geparst"() {
        given:
        def initial = "1.2.3"

        when:
        NpmVersion.from(initial)

        then:
        noExceptionThrown()
    }

    def "Version 1.2.3-fhweuofb-dev.125 wird korrekt geparst"() {
        given:
        def initial = "1.2.3-fhweuofb-dev.125"

        when:
        NpmVersion.from(initial)

        then:
        noExceptionThrown()
    }

    def "Version 12.1.1-dev.0 wird korrekt geparst"() {
        given:
        def initial = "12.1.1-dev.0"

        when:
        NpmVersion.from(initial)

        then:
        noExceptionThrown()
    }

    def "Version 1.2.3-fhweuofb-dev.a125 wird nicht geparst"() {
        given:
        def initial = "1.2.3-fhweuofb-dev.a125"

        when:
        NpmVersion.from(initial)

        then:
        thrown(IllegalArgumentException)
    }

    def "Version 1.2.3 wird zu 2.0.0"() {
        given:
        def initial = "1.2.3"

        when:
        def version = NpmVersion.from(initial).bumpMajor()

        then:
        assert 2L == version.properties.get('major') as long
        assert 0L == version.properties.get('minor') as long
        assert 0L == version.properties.get('hotfix') as long
    }

    def "Version 1.2.3-dev.125 wird zu 2.0.0-dev.0"() {
        given:
        def initial = "1.2.3-dev.125"

        when:
        def version = NpmVersion.from(initial).bumpMajor()

        then:
        assert 2L == version.properties.get('major') as long
        assert 0L == version.properties.get('minor') as long
        assert 0L == version.properties.get('hotfix') as long
        assert '-dev.' == version.properties.get('suffix')
        assert 0L == version.properties.get('buildNumber')
    }

    def "Version 1.2.3 wird zu 1.3.0"() {
        given:
        def initial = "1.2.3"

        when:
        def version = NpmVersion.from(initial).bumpMinor()

        then:
        assert 1L == version.properties.get('major') as long
        assert 3L == version.properties.get('minor') as long
        assert 0L == version.properties.get('hotfix') as long
    }

    def "Version 1.2.3 wird zu 1.2.4"() {
        given:
        def initial = "1.2.3"

        when:
        def version = NpmVersion.from(initial).bumpHotfix()

        then:
        assert 1L == version.properties.get('major') as long
        assert 2L == version.properties.get('minor') as long
        assert 4L == version.properties.get('hotfix') as long
    }

    def "Version 12.1.1-dev.0 wird zu 12.1.1-dev.1"() {
        given:
        def initial = "12.1.1-dev.0"

        when:
        def version = NpmVersion.from(initial).build()

        then:
        assert 12L == version.properties.get('major') as long
        assert 1L == version.properties.get('minor') as long
        assert 1L == version.properties.get('hotfix') as long
        assert "-dev." == version.properties.get('suffix')
        assert 1L == version.properties.get('buildNumber') as long
    }

    def "Version 12.1.1-dev.0 wird korrekt dargestellt"() {
        given:
        def initial = "12.1.1-dev.0"

        when:
        def version = NpmVersion.from(initial)

        then:
        assert "12.1.1-dev.0" == version.toString()
    }
}

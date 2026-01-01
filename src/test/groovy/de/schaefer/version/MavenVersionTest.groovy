package de.schaefer.version

import de.schaefer.Mode
import spock.lang.Specification

class MavenVersionTest extends Specification {

    def "Version 1.2.3 wird korrekt geparst"() {
        given:
        def initial = "1.2.3"

        when:
        MavenVersion.from(initial)

        then:
        noExceptionThrown()
    }

    def "Version 12.24535.1-SNAPSHOT wird korrekt geparst"() {
        given:
        def initial = "12.24535.1-SNAPSHOT"

        when:
        MavenVersion.from(initial)

        then:
        noExceptionThrown()
    }

    def "Version 12.1.1-dev.0 wird korrekt geparst"() {
        given:
        def initial = "12.1.1-dev.0"

        when:
        MavenVersion.from(initial)

        then:
        noExceptionThrown()
    }

    def "Version 12.a.1-dev.0 wird nicht geparst"() {
        given:
        def initial = "12.a.1-dev.0"

        when:
        MavenVersion.from(initial)

        then:
        thrown(IllegalArgumentException)
    }

    def "Version 1.2.3 wird zu 2.0.0"() {
        given:
        def initial = "1.2.3"

        when:
        def version = MavenVersion.from(initial).bumpMajor()

        then:
        assert 2L == version.properties.get('major') as long
        assert 0L == version.properties.get('minor') as long
        assert 0L == version.properties.get('hotfix') as long
    }

    def "Version 1.2.3-SNAPSHOT wird zu 2.0.0-SNAPSHOT"() {
        given:
        def initial = "1.2.3-SNAPSHOT"

        when:
        def version = MavenVersion.from(initial).bumpMajor()

        then:
        assert 2L == version.properties.get('major') as long
        assert 0L == version.properties.get('minor') as long
        assert 0L == version.properties.get('hotfix') as long
        assert '-SNAPSHOT' == version.properties.get('suffix')
    }

    def "Version 1.2.3 wird zu 1.3.0"() {
        given:
        def initial = "1.2.3"

        when:
        def version = MavenVersion.from(initial).bumpMinor()

        then:
        assert 1L == version.properties.get('major') as long
        assert 3L == version.properties.get('minor') as long
        assert 0L == version.properties.get('hotfix') as long
    }

    def "Version 1.2.3 wird zu 1.2.4"() {
        given:
        def initial = "1.2.3"

        when:
        def version = MavenVersion.from(initial).bumpHotfix()

        then:
        assert 1L == version.properties.get('major') as long
        assert 2L == version.properties.get('minor') as long
        assert 4L == version.properties.get('hotfix') as long
    }

    def "Version 12.1.1-dev.0 wird korrekt dargestellt"() {
        given:
        def initial = "12.1.1-dev.0"

        when:
        def version = MavenVersion.from(initial)

        then:
        assert "12.1.1-dev.0" == version.toString()
    }
}

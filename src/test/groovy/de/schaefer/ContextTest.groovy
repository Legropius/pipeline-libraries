package de.schaefer

import spock.lang.Specification;

class ContextTest extends Specification {

    def "log schreibt Echo mit Service-Namen"() {
        given:
        def script = new ScriptMock()
        def ctx = new Context(script)
        ctx.service = 'orders'

        when:
        ctx.log("Build started")

        then:
        script.echoes == ["[orders] Build started"]
    }

    def "inServiceDir wechselt ins Service-Verzeichnis"() {
        given:
        def script = new ScriptMock()
        def ctx = new Context(script)
        ctx.path = 'services/orders'
        def executed = false

        when:
        ctx.inServiceDir {
            executed = true
        }

        then:
        executed
        script.dirs == ['services/orders']
    }

    def "isMainBranch erkennt main"() {
        given:
        def script = new ScriptMock()
        script.env.BRANCH_NAME = 'main'

        def ctx = new Context(script)

        expect:
        ctx.isMainBranch()
    }

    def "isReleaseBranch erkennt release Branch"() {
        given:
        def script = new ScriptMock()
        script.env.BRANCH_NAME = 'release/1.2.3'
        def ctx = new Context(script)

        expect:
        ctx.isReleaseBranch()
    }

    def "isReleaseBuild erkennt release"() {
        given:
        def script = new ScriptMock()
        def ctx = new Context(script)
        ctx.mode = Mode.MAJOR_RELEASE

        expect:
        ctx.isReleaseBuild()
    }

    def "isRelease erkennt release und release Branch"() {
        given:
        def script = new ScriptMock()
        script.env.BRANCH_NAME = 'release/1.2.3'
        def ctx = new Context(script)
        ctx.mode = Mode.MAJOR_RELEASE

        expect:
        ctx.isRelease()
    }
}
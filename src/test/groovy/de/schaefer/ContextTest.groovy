package de.schaefer

import spock.lang.Specification;

class ContextTest extends Specification {

    private static final PARAMS;

    static {
        PARAMS = Params.builder()
        .branchName("release/1.2")
        .build()
    }

    def "log schreibt Echo mit Service-Namen"() {
        given:
        def script = new ScriptMock()
        def ctx = new Context(script, PARAMS)
        ctx.service = 'orders'

        when:
        ctx.log("Build started")

        then:
        script.echoes == ["[orders] Build started"]
    }

    def "inServiceDir wechselt ins Service-Verzeichnis"() {
        given:
        def script = new ScriptMock()
        def ctx = new Context(script, PARAMS)
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
        def params = Params.builder()
        .branchName("main")
        .build()

        def ctx = new Context(script, params)

        expect:
        ctx.isMainBranch()
    }

    def "isReleaseBranch erkennt release Branch"() {
        given:
        def script = new ScriptMock()
        def ctx = new Context(script, PARAMS)

        expect:
        ctx.isReleaseBranch()
    }

    def "isReleaseBuild erkennt release"() {
        given:
        def script = new ScriptMock()
        def ctx = new Context(script, PARAMS)
        ctx.mode = Mode.MAJOR_RELEASE

        expect:
        ctx.isReleaseBuild()
    }

    def "isRelease erkennt release und release Branch"() {
        given:
        def script = new ScriptMock()
        def ctx = new Context(script, PARAMS)
        ctx.mode = Mode.MAJOR_RELEASE

        expect:
        ctx.isRelease()
    }
}
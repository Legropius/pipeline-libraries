package de.schaefer.stages.test

import de.schaefer.BuildMode
import de.schaefer.Context
import de.schaefer.stages.Stage

class MavenTestStage extends Stage {

    private final BuildMode buildMode = BuildMode.MAVEN

    MavenTestStage(final Context ctx, final Map cfg = [:]) {
        super(ctx, cfg)
    }

    @Override
    String name() {
        "Test ${buildMode}"
    }

    @Override
    void execute(final Closure action = {}) {
        ctx.script.dir(ctx.path) {
            ctx.script.sh 'mvn test'
        }
        ctx.state.testsPassed = [(buildMode): true]
    }
}

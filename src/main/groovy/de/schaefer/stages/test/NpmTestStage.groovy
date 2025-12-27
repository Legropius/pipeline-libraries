package de.schaefer.stages.test

import de.schaefer.BuildMode
import de.schaefer.Context
import de.schaefer.stages.Stage

class NpmTestStage extends Stage {

    private final BuildMode buildMode = BuildMode.NPM

    NpmTestStage(final Context ctx, final Map cfg = [:]) {
        super(ctx, cfg)
    }

    @Override
    String name() {
        "Test ${buildMode}"
    }

    @Override
    void execute(final Closure action = {}) {
        ctx.script.dir(ctx.path) {
            ctx.script.sh 'npm test'
        }
        ctx.state.testsPassed = [(buildMode): true]
    }
}

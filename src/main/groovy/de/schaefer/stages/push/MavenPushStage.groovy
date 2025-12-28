package de.schaefer.stages.push

import de.schaefer.BuildMode
import de.schaefer.Context
import de.schaefer.stages.Stage

class MavenPushStage extends Stage {

    private final BuildMode buildMode = BuildMode.MAVEN

    MavenPushStage(final Context ctx, final Map cfg = [:]) {
        super(ctx, cfg)
    }

    @Override
    String name() {
        "Release ${buildMode}"
    }

    @Override
    void execute(final Closure action = {}) {
        ctx.script.dir(ctx.path) {
            ctx.script.sh 'mvn clean deploy'
        }
        ctx.state.pushed = [(buildMode): true]
    }
}

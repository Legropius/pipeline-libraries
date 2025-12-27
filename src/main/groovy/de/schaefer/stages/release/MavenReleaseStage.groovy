package de.schaefer.stages.release

import de.schaefer.BuildMode
import de.schaefer.Context
import de.schaefer.stages.Stage

class MavenReleaseStage extends Stage {

    private final BuildMode buildMode = BuildMode.MAVEN

    MavenReleaseStage(final Context ctx, final Map cfg = [:]) {
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
        ctx.state.deployed = [(buildMode): true]
    }
}

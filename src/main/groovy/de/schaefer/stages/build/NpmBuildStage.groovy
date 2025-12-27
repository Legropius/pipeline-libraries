package de.schaefer.stages.build

import de.schaefer.BuildMode
import de.schaefer.Context
import de.schaefer.stages.Stage

class NpmBuildStage extends Stage {

    private final BuildMode buildMode = BuildMode.NPM

    NpmBuildStage(final Context ctx, final Map cfg = [:]) {
        super(ctx, cfg)
    }

    @Override
    String name() {
        "Build ${buildMode}"
    }

    @Override
    void execute(final Closure action = {}) {
        ctx.inServiceDir {
            ctx.script.sh 'npm install'
            ctx.script.sh 'npm run build'
        }

        ctx.state.buildSuccessful = [(buildMode): true]
        ctx.log("Build finished.")
    }
}

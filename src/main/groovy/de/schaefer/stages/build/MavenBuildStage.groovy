package de.schaefer.stages.build

import de.schaefer.BuildMode
import de.schaefer.Context
import de.schaefer.stages.Stage

class MavenBuildStage extends Stage {

    private final BuildMode buildMode = BuildMode.MAVEN

    MavenBuildStage(Context ctx, Map cfg = [:]) {
        super(ctx, cfg)
    }

    @Override
    String name() {
        "Build ${buildMode}"
    }

    @Override
    void execute() {
        ctx.inServiceDir {
            ctx.script.sh 'mvn clean package'
        }

        ctx.state.buildSuccessful = [(buildMode): true]
        ctx.log("Build finished.")
    }
}

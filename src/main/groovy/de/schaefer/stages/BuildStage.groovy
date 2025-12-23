package de.schaefer.stages

import de.schaefer.Context

class BuildStage extends Stage {

    BuildStage(Context ctx, Map cfg = [:]) {
        super(ctx, cfg)
    }

    @Override
    String name() {
        'Build'
    }

    @Override
    void execute() {
        ctx.script.dir(ctx.path) {
            ctx.script.sh 'mvn clean package'
        }
    }
}

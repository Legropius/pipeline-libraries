package de.schaefer.stages

import de.schaefer.Context

class DeployStage extends Stage {

    DeployStage(final Context ctx, final Map cfg = [:]) {
        super(ctx, cfg)
    }

    @Override
    String name() {
        'Deploy'
    }

    @Override
    void execute(final Closure action = {}) {
        ctx.inServiceDir {
            ctx.script.sh "echo Deploying ${ctx.service}"
        }
        ctx.state.deployed = true
    }
}

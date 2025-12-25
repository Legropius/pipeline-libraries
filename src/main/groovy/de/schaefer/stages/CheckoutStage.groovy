package de.schaefer.stages

import de.schaefer.Context

class CheckoutStage extends Stage {

    CheckoutStage(Context ctx, Map cfg = [:]) {
        super(ctx, cfg)
    }

    @Override
    String name() {
        'Checkout'
    }

    @Override
    void execute() {
        ctx.log("Checking out branch: ${ctx.script.env.BRANCH_NAME}")
        ctx.inServiceDir {
            ctx.script.checkout([
                    $class: 'GitSCM',
                    branches: [[name: ctx.script.env.BRANCH_NAME ?: 'main']],
                    userRemoteConfigs: [[url: cfg.repo ?: "git@gitlab.com:example/${ctx.service}.git"]]
            ])
        }
        ctx.state.checkedOut = true
    }
}

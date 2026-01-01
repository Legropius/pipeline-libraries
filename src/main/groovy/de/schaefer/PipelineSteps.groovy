package de.schaefer

import de.schaefer.stages.CheckoutStage
import de.schaefer.stages.DeployStage
import de.schaefer.stages.Stage
import de.schaefer.stages.build.BuildStageFactory
import de.schaefer.stages.push.PushStageFactory
import de.schaefer.stages.test.TestStageFactory

class PipelineSteps {

    private final Context ctx
    private final List<Stage> stages = []

    private PipelineSteps(final Context ctx) {
        this.ctx = ctx
    }

    static PipelineSteps from(final Context context) {
        return new PipelineSteps(context)
    }

    PipelineSteps addStage(final Stage stage) {
        stages << stage
        return this
    }

    void run() {
        stages.each {
            ErrorHandling.safeStage(it.name(), it.run())
        }
    }

    PipelineSteps checkout(final Map cfg = [:]) {
        addStage(new CheckoutStage(ctx, cfg))
        return this
    }

    PipelineSteps build() {
        BuildStageFactory.from(ctx)
                .each { addStage(it) }
        return this
    }

    PipelineSteps test() {
        if (ctx.isRelease()) {
            TestStageFactory.from(ctx)
                    .each { addStage(it) }
        }
        return this
    }

    PipelineSteps push() {
        PushStageFactory.from(ctx)
                .each { addStage(it) }
        return this
    }

    PipelineSteps deploy() {
        addStage(new DeployStage(ctx))
        return this
    }
}

import de.schaefer.BuildMode
import de.schaefer.Context
import de.schaefer.Params
import de.schaefer.PipelineSteps

def call(Map config = [:]) {
    final def params = Params.builder()
    .branchName(env.BRANCH_NAME)
    .build()
    final def ctx = new Context(this, params)
    ctx.service = config.service ?: 'unknown'
    ctx.buildModes = [BuildMode.MAVEN, BuildMode.NPM]
    ctx.path = config.path ?: '.'

    PipelineSteps.from(ctx)
            .checkout()
            .build()
            .test()
            .deploy()
            .push()
            .run()
}


import de.schaefer.BuildMode
import de.schaefer.Context
import de.schaefer.PipelineSteps

def call(Map config = [:]) {
    def ctx = new Context(this)
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


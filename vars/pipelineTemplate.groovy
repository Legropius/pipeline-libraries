def call(Closure body) {
    def ctx = new Context(this)

    node {
        body.delegate = new PipelineDSL(ctx)
        body.resolveStrategy = DELEGATE_FIRST
        body()
    }
}

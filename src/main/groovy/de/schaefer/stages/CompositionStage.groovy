package de.schaefer.stages

import de.schaefer.Context

class CompositionStage extends Stage {

    private final List<Stage> stages = []

    CompositionStage(Context ctx, Map cfg = [:]) {
        super(ctx, cfg)
    }

    @Override
    String name() {
        cfg.name ?: 'Composite'
    }

    CompositionStage add(Stage stage) {
        stages << stage
        return this
    }

    @Override
    protected void execute() {
        stages.each {
            it.run()
        }
    }
}


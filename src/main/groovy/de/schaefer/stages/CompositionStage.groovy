package de.schaefer.stages

import de.schaefer.Context

class CompositionStage extends Stage {

    private final List<Stage> stages = []

    CompositionStage(final Context ctx, final Map cfg = [:]) {
        super(ctx, cfg)
    }

    @Override
    String name() {
        cfg.name ?: 'Composite'
    }

    CompositionStage add(final Stage stage) {
        stages << stage
        return this
    }

    @Override
    protected void execute(final Closure action = {}) {
        stages.each {
            it.run()
        }
    }
}


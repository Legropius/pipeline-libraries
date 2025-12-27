package de.schaefer.stages

import de.schaefer.Context

abstract class Stage {

    protected Context ctx
    protected Map cfg

    protected Stage(final Context ctx, final Map cfg) {
        this.ctx = ctx
        this.cfg = cfg
    }

    final void run() {
        ctx.script.stage(name()) {
            ctx.log("Stage \"${name()}\" started.")
            execute()
            ctx.log("Stage \"${name()}\" finished.")
        }
    }

    abstract String name()
    protected abstract void execute(final Closure action = {})
}

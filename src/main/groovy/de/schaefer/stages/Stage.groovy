package de.schaefer.stages

import de.schaefer.Context

abstract class Stage {
    protected Context ctx
    protected Map cfg

    protected Stage(Context ctx, Map cfg) {
        this.ctx = ctx
        this.cfg = cfg
    }

    final void run() {
        ctx.script.stage(name()) {
            execute()
        }
    }

    protected abstract String name()
    protected abstract void execute()
}

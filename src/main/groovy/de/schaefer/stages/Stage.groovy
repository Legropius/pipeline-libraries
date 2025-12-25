package de.schaefer.stages

import de.schaefer.BuildMode
import de.schaefer.Context

abstract class Stage {

    protected Context ctx
    protected Map cfg

    protected Stage(Context ctx, Map cfg) {
        this.ctx = ctx
        this.cfg = cfg
    }

    final void run() {
        ctx.log("Build mode: \"${BuildMode.NPM}\"")
        ctx.script.stage(name()) {
            execute()
        }
        ctx.log("Stage \"${name()}\" finished.")
    }

    abstract String name()
    protected abstract void execute()
}

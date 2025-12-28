package de.schaefer.stages.release

import de.schaefer.Context
import de.schaefer.stages.Stage
import de.schaefer.version.VersionUtils

class ReleaseStage extends Stage {

    ReleaseStage(final Context ctx, final Map cfg = [:]) {
        super(ctx, cfg)
    }

    @Override
    String name() {
        'Release'
    }

    @Override
    protected void execute(final Closure action) {
        VersionUtils.buildReleaseVersionsAndMaybeBumpMain(ctx)
        ctx.state.released = true
    }
}

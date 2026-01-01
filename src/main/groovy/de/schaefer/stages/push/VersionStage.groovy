package de.schaefer.stages.push

import de.schaefer.Context
import de.schaefer.stages.Stage
import de.schaefer.version.VersionUtils

class VersionStage extends Stage {

    VersionStage(final Context ctx, final Map cfg) {
        super(ctx, cfg)
    }

    @Override
    String name() {
        "Managing Version"
    }

    @Override
    void execute(final Closure action) {
        VersionUtils.buildAndPushVersions(ctx)
        ctx.state.versionAdjusted = true
    }
}

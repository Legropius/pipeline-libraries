package de.schaefer.stages.release

import de.schaefer.BuildMode
import de.schaefer.Context
import de.schaefer.stages.Stage
import de.schaefer.stages.test.MavenTestStage
import de.schaefer.stages.test.NpmTestStage

static Set<Stage> from(final Context context, final Map cfg = [:]) {
    context.buildModes
            .collect(it -> toStage(it, context, cfg))
}

private static Stage toStage(final BuildMode buildMode, final Context context, final Map cfg = [:]) {
    switch (buildMode) {
        case BuildMode.MAVEN:
            return new MavenReleaseStage(context, cfg)
        case BuildMode.NPM:
            return new NpmReleaseStage(context, cfg)
        default:
            throw new IllegalArgumentException("Unsupported build mode ${buildMode}!")
    }
}
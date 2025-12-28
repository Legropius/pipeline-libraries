package de.schaefer.stages.push

import de.schaefer.BuildMode
import de.schaefer.Context
import de.schaefer.stages.Stage

static Set<Stage> from(final Context context, final Map cfg = [:]) {
    context.buildModes
            .collect(it -> toStage(it, context, cfg))
}

private static Stage toStage(final BuildMode buildMode, final Context context, final Map cfg = [:]) {
    switch (buildMode) {
        case BuildMode.MAVEN:
            return new MavenPushStage(context, cfg)
        case BuildMode.NPM:
            return new NpmPushStage(context, cfg)
        default:
            throw new IllegalArgumentException("Unsupported build mode ${buildMode}!")
    }
}
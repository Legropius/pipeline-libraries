package de.schaefer

static void stashFor(final Context ctx, final BuildMode buildMode) {
    ctx.script.stash(name: buildMode.getStashName(), includes: buildMode.getIncludes(), useDefaultExcludes: false)
    ctx.log("Stashed everything in ${buildMode.getIncludes()} for mode ${buildMode}.")
}

static void unstashFor(final Context ctx, final BuildMode buildMode) {
    ctx.script.unstash(buildMode.getStashName())
    ctx.log("Unstashed everything for mode ${buildMode}.")
}
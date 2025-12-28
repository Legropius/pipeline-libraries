package de.schaefer.version

import de.schaefer.BuildMode
import de.schaefer.Context
import de.schaefer.Mode

import java.util.function.Function

// TODO: rework version workflow!
static void buildReleaseVersionsAndMaybeBumpMain(final Context ctx) {
    ctx.buildModes
            .each { buildReleaseVersions(ctx, it) }   // TODO: Loop necessary?
    maybeBumpOnMain(ctx)
}

private static void buildReleaseVersions(final Context ctx, final BuildMode buildMode) {
    switch (buildMode) {
        case BuildMode.MAVEN:
            buildMavenVersion(ctx, version -> MavenVersion.from(version).getReleaseVersion(ctx.mode))
            break
        case BuildMode.NPM:
            buildNpmVersion(ctx, version -> NpmVersion.from(version).getReleaseVersion(ctx.mode))
            break
        default:
            throw new IllegalArgumentException("Unsupported build mode ${buildMode}!")
    }
}

private static void buildMavenVersion(final Context ctx, final Function<String, Version> getVersion) {
    final String version = ctx.script.sh(
            script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout",
            returnStdout: true
    ).trim()
    final def newVersion = getVersion.apply(version)
    if (newVersion.toString() != version) {
        ctx.script.sh "mvn versions:set -DnewVersion=${newVersion} -DgenerateBackupPoms=false"
        ctx.log("Bumped to version ${newVersion}!")
    }
}

private static void buildNpmVersion(final Context ctx, final Function<String, Version> getVersion) {
    final String version = ctx.script.sh(
            script: "npm pkg get version | tr -d '\"'",
            returnStdout: true
    ).trim()
    final def newVersion = getVersion.apply(version)
    if (newVersion.toString() != version) {
        ctx.script.sh "npm version ${newVersion} --no-git-tag-version"
        ctx.log("Bumped to version ${newVersion}!")
    }
}

private static void maybeBumpOnMain(final Context ctx) {
    if (!ctx.isRelease()) {
        return
    }

    if (!(ctx.mode in [Mode.MAJOR_RELEASE, Mode.MINOR_RELEASE])) {
        return
    }

    ctx.script.echo "Release-branch detected (${ctx.branchName()}), bumping version on main..."

    ctx.script.sh """
        git fetch origin main
        git checkout main
    """

    buildMainVersions(ctx)

    ctx.script.sh """
        git commit -am "chore(mvn): bump version on main after ${ctx.mode}"
        git checkout -
    """
}


private static void buildMainVersions(final Context ctx) {
    if (ctx.buildModes.contains(BuildMode.MAVEN)) {
        buildMavenVersion(ctx, version -> MavenVersion.from(version).bumpMainAfterRelease(ctx.mode))
    }

    if (ctx.buildModes.contains(BuildMode.NPM)) {
        buildNpmVersion(ctx, version -> NpmVersion.from(version).bumpMainAfterRelease(ctx.mode))
    }
}
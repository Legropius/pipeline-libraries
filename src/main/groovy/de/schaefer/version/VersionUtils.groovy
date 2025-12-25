package de.schaefer.version

import de.schaefer.BuildMode
import de.schaefer.Context
import de.schaefer.Mode

static void bumpVersion(final Context ctx) {
    ctx.buildModes
    .each {bumpVersion(ctx, it)}
}

static void bumpVersion(final Context ctx, final BuildMode buildMode) {
    switch (buildMode) {
        case BuildMode.MAVEN:
            bumpMavenVersion(ctx)
            break
        case BuildMode.NPM:
            bumpNpmVersion(ctx)
            break
        default:
            throw new IllegalArgumentException("Unsupported build mode ${buildMode}!")
    }
}

private static void bumpMavenVersion(final Context ctx) {
    final String version = ctx.script.sh(
            script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout",
            returnStdout: true
    ).trim()
    final def newVersion = getNewVersion(MavenVersion.from(version), ctx.mode)
    if (newVersion.toString() != version) {
        ctx.script.sh "mvn versions:set -DnewVersion=${newVersion} -DgenerateBackupPoms=false"
    }
}

private static void bumpNpmVersion(final Context ctx) {
    final String version = ctx.script.sh(
            script: "npm pkg get version | tr -d '\"'",
            returnStdout: true
    ).trim()
    final def newVersion = getNewVersion(NpmVersion.from(version), ctx.mode)
    if (newVersion.toString() != version) {
        ctx.script.sh "npm version ${newVersion} --no-git-tag-version"
    }
}

private static Version getNewVersion(final Version version, final Mode mode) {
    switch (mode) {
        case Mode.MAJOR_RELEASE:
            return version.bumpMajor()
        case Mode.MINOR_RELEASE:
            return version.bumpMinor()
        case Mode.HOTFIX:
            return version.bumpHotfix()
        default:
            return version.build()
    }
}

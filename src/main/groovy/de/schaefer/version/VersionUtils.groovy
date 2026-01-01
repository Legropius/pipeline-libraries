package de.schaefer.version

import de.schaefer.BuildMode
import de.schaefer.Context

static void buildAndPushVersions(final Context ctx) {
    if (ctx.isRelease()) {
        setNewVersions(ctx)
        setNewVersions(ctx, ctx.mainBranch())
    } else {
        setNewVersions(ctx)
    }
}

private static void setNewVersions(final Context ctx) {
    setNewVersions(ctx, ctx.branchName())
}

private static void setNewVersions(final Context ctx, final String branch) {
    maybeSwitchBranch(ctx,
            branch,
            {
                ctx.buildModes
                        .each { setVersions(ctx, it, branch) }
                commitVersions(ctx, branch)
            })
}

private static void maybeSwitchBranch(final Context ctx, final String branch, final Closure<Void> closure) {
    ctx.script.sh """
        git fetch origin ${branch}
        git checkout ${branch}
    """

    closure()

    ctx.script.sh """
        git checkout -
    """
}

private static void setVersions(final Context ctx, final BuildMode buildMode, final String branch) {
    switch (buildMode) {
        case BuildMode.MAVEN:
            mavenVersion(ctx, branch)
            break
        case BuildMode.NPM:
            npmVersion(ctx, branch)
            break
        default:
            throw new IllegalArgumentException("Unsupported build mode ${buildMode}!")
    }
}

private static void mavenVersion(final Context ctx, final String branch) {
    final String stringVersion = ctx.script.sh(
            script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout",
            returnStdout: true
    ).trim()
    final def version = MavenVersion.from(stringVersion)
    final def newVersion = determineVersionFor(ctx, branch, version)
    if (newVersion != version) {
        ctx.script.sh "mvn versions:set -DnewVersion=${newVersion} -DgenerateBackupPoms=false"
    }
}

private static void npmVersion(final Context ctx, final String branch) {
    final String stringVersion = ctx.script.sh(
            script: "npm pkg get version | tr -d '\"'",
            returnStdout: true
    ).trim()
    final def version = NpmVersion.from(stringVersion)
    final def newVersion = determineVersionFor(ctx, branch, version)
    if (newVersion != version) {
        ctx.script.sh "npm version ${newVersion} --no-git-tag-version"
    }
}

private static Version determineVersionFor(final Context ctx, final String branch, final Version version) {
    final Version result
    if (ctx.isRelease()) {
        if (branch != ctx.mainBranch()) {
            result = version.getNextReleaseVersion(ctx.mode)
        } else {
            result = version.possiblyBumpMain(ctx.mode)
        }
    } else {
        result = version.build()
    }
    return result
}

private static void commitVersions(final Context ctx, final String branch) {
    ctx.script.sh """
        git commit -am "chore(mvn): bump version on ${branch} after ${ctx.mode}"
    """
}
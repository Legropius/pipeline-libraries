package de.schaefer.version

import de.schaefer.Mode

import java.util.regex.Pattern

class MavenVersion implements Version {

    private static final String PATTERN_STRING = "^(\\d+\\.\\d+\\.\\d+)(.*)?\$"
    private static final Pattern VERSION_PATTERN = Pattern.compile(PATTERN_STRING)
    private static final String SNAPSHOT = "-SNAPSHOT"

    private final long major
    private final long minor
    private final long hotfix
    private final String suffix

    private MavenVersion(final long major, final long minor, final long hotfix, final String suffix) {
        this.major = major
        this.minor = minor
        this.hotfix = hotfix
        this.suffix = suffix
    }

    private MavenVersion(final MavenVersion other, final Map overrides = [:]) {
        this.major = overrides.getOrDefault('major', other.major) as long
        this.minor = overrides.getOrDefault('minor', other.minor) as long
        this.hotfix = overrides.getOrDefault('hotfix', other.hotfix) as long
        this.suffix = overrides.getOrDefault('suffix', other.suffix)
    }

    static final MavenVersion from(final String version) {
        final def matcher = VERSION_PATTERN.matcher(version)
        if (matcher.matches()) {
            final def versionGroup = matcher.group(1)
            final def suffixGroup = matcher.group(2)
            final def parts = versionGroup.tokenize('.')
            return new MavenVersion(parts[0] as long, parts[1] as long, parts[2] as long, suffixGroup)
        } else {
            throw new IllegalArgumentException("Argument ${version} does not match Pattern ${PATTERN_STRING}")
        }
    }

    @Override
    Version possiblyBumpMain(final Mode mode) {
        final Version result
        switch (mode) {
            case Mode.MAJOR_RELEASE:
                result = new MavenVersion(this, [major: this.major + 1, minor: 0, hotfix: 0])
                break
            case Mode.MINOR_RELEASE:
                result = new MavenVersion(this, [minor: this.minor + 1, hotfix: 0])
                break
            case Mode.HOTFIX:
                result = new MavenVersion(this)
                break
            default:
                throw new IllegalStateException("Version ${this.toString()} could be behind current release version and is therefore forbidden!")
        }
        return result
    }

    @Override
    Version getNextReleaseVersion(final Mode mode) {
        final Version result
        switch (mode) {
            case Mode.MAJOR_RELEASE, Mode.MINOR_RELEASE:
                if (isAlreadyReleased()) {  // cannot major or minor a version again!
                    throw new IllegalStateException("Cannot release ${this.toString()}. Please provide a SNAPSHOT version!")
                }
                result = new MavenVersion(this, [suffix: null])
                break
            case Mode.HOTFIX:
                if (isNotYetReleased()) {  // can only hotfix a released version!
                    throw new IllegalStateException("Please release ${this.toString()} before considering a hotfix!")
                }
                result = new MavenVersion(this, [hotfix: this.hotfix + 1])
                break
            default:
                throw new IllegalStateException("Version ${this.toString()} must not exist or already exists as a release version!")
        }
        return result
    }

    private boolean isAlreadyReleased() {
        !this.suffix.endsWith(SNAPSHOT)
    }

    private isNotYetReleased() {
        this.suffix != null
    }

    @Override
    final Version build() {
        return new MavenVersion(this)
    }


    @Override
    String toString() {
        return "${major}.${minor}.${hotfix}${suffix}"
    }
}
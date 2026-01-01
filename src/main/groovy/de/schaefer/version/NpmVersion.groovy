package de.schaefer.version

import de.schaefer.Mode

import java.util.regex.Pattern

class NpmVersion implements Version {

    private static final String PATTERN_STRING = "^(\\d+\\.\\d+\\.\\d+)(?:((?:-.+)?-dev\\.)(\\d+))?\$"
    private static final Pattern VERSION_PATTERN = Pattern.compile(PATTERN_STRING)

    private final long major
    private final long minor
    private final long hotfix
    private final String suffix
    private final Long buildNumber

    private NpmVersion(final long major, final long minor, final long hotfix, final String suffix, final Long buildNumber) {
        this.major = major
        this.minor = minor
        this.hotfix = hotfix
        this.suffix = suffix
        this.buildNumber = buildNumber
    }

    private NpmVersion(final NpmVersion other, final Map overrides = [:]) {
        this.major = overrides.getOrDefault('major', other.major) as long
        this.minor = overrides.getOrDefault('minor', other.minor) as long
        this.hotfix = overrides.getOrDefault('hotfix', other.hotfix) as long
        this.suffix = overrides.getOrDefault('suffix', other.suffix)
        this.buildNumber = overrides.getOrDefault('buildNumber', other.buildNumber) as long
    }

    static final NpmVersion from(final String version) {
        final def matcher = VERSION_PATTERN.matcher(version)
        if (matcher.matches()) {
            final def versionGroup = matcher.group(1)
            final def suffixGroup = matcher.group(2)
            final def buildNumber = matcher.group(3)
            final def parts = versionGroup.tokenize('.')
            return new NpmVersion(parts[0] as long, parts[1] as long, parts[2] as long, suffixGroup, buildNumber as Long)
        } else {
            throw new IllegalArgumentException("Argument ${version} does not match Pattern ${PATTERN_STRING}")
        }
    }

    @Override
    Version possiblyBumpMain(final Mode mode) {
        final Version result
        switch (mode) {
            case Mode.MAJOR_RELEASE:
                result = new NpmVersion(this, [major: this.major + 1, minor: 0, hotfix: 0, buildNumber: 0])
                break
            case Mode.MINOR_RELEASE:
                result = new NpmVersion(this, [minor: this.minor + 1, hotfix: 0, buildNumber: 0])
                break
            case Mode.HOTFIX:
                result = new NpmVersion(this, [hotfix: this.hotfix + 1])
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
            case [Mode.MAJOR_RELEASE, Mode.MINOR_RELEASE]:
                if (isAlreadyReleased()) {  // cannot major or minor a version again!
                    throw new IllegalStateException("Cannot release ${this.toString()}. Please provide a valid dev-version!")
                }
                result = new NpmVersion(this, [suffix: null, buildNumber: null])
                break
            case Mode.HOTFIX:
                if (isNotYetReleased()) {  // can only hotfix a released version!
                    throw new IllegalStateException("Cannot release ${this.toString()}. Please provide a valid dev-version!")
                }
                result = new NpmVersion(this, [hotfix: this.hotfix + 1, suffix: null])
                break
            default:
                throw new IllegalStateException("Version ${this.toString()} must not exist or already exists as a release version!")
        }
        return result
    }

    private boolean isAlreadyReleased() {
        this.suffix == null && this.buildNumber == null
    }

    private boolean isNotYetReleased() {
        this.suffix != null || this.buildNumber != null
    }

    @Override
    final Version build() {
        return new NpmVersion(this, [buildNumber: this.buildNumber + 1])
    }

    @Override
    String toString() {
        return "${major}.${minor}.${hotfix}${suffix}${buildNumber}"
    }
}
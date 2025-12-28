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
    Version bumpMainAfterRelease(final Mode mode) {
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
                result = new NpmVersion(this)
                break
        }
        return result
    }

    @Override
    Version getReleaseVersion(final Mode mode) {
        final Version result
        switch (mode) {
            case Mode.MAJOR_RELEASE:
                result = new NpmVersion(this, [suffix: null])
                break
            case Mode.MINOR_RELEASE:
                result = new NpmVersion(this, [suffix: null])
                break
            case Mode.HOTFIX:
                result = new NpmVersion(this, [suffix: null])
                break
            default:
                result = new NpmVersion(this)
                break
        }
        return result
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
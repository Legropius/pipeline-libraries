package de.schaefer.version

import java.util.regex.Pattern

class MavenVersion implements Version {

    private static final String PATTERN_STRING = "^(\\d+\\.\\d+\\.\\d+)(.*)?\$"
    private static final Pattern VERSION_PATTERN = Pattern.compile(PATTERN_STRING)

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

    static final MavenVersion from(String version) {
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
    final def bumpMajor() {
        return new MavenVersion(this, [major: this.major + 1, minor: 0, hotfix: 0])
    }

    @Override
    final def bumpMinor() {
        return new MavenVersion(this, [minor: this.minor + 1, hotfix: 0])
    }

    @Override
    final def bumpHotfix() {
        return new MavenVersion(this, [hotfix: this.hotfix + 1])
    }

    @Override
    final def build() {
        return this
    }

    @Override
    String toString() {
        return "${major}.${minor}.${hotfix}${suffix}"
    }
}
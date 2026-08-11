package de.schaefer

enum BuildMode {
    MAVEN("maven-stash", "target"),
    NPM("npm-stash", "dist");

    private final String stashName
    private final String includes

    BuildMode(final String stashName, final String includes) {
        this.stashName = stashName
        this.includes = includes
    }

    String getStashName() {
        return stashName
    }

    String getIncludes() {
        return includes
    }
}
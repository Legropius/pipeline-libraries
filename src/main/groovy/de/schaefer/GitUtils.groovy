package de.schaefer

static void pushToBranch(final String branch) {
    if (!branch) {
        throw new IllegalArgumentException("Branch must not be empty")
    }

    def process = ["git", "push", "origin", branch].execute()
    process.waitFor()

    if (process.exitValue() != 0) {
        throw new RuntimeException("Git push failed:\n${process.err.text}")
    }

    println "Successfully pushed to branch '${branch}'."
}

static void createGitTag(final String tagName) {
    if (!tagName) {
        throw new IllegalArgumentException("Tag-name must not be empty")
    }

    def process = ["git", "tag", tagName].execute()
    process.waitFor()

    if (process.exitValue() != 0) {
        throw new RuntimeException("Git tag could not be created:\n${process.err.text}")
    }

    println "Git tag '${tagName}' was created."
}

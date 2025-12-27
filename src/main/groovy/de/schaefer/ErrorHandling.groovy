package de.schaefer

static def safeStage(final String name, final Closure body) {
    stage(name) {
        try {
            body()
        } catch (e) {
            currentBuild.result = 'FAILURE'
            throw e
        }
    }
}


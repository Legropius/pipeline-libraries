package de.schaefer

static def safeStage(String name, Closure body) {
    stage(name) {
        try {
            body()
        } catch (e) {
            currentBuild.result = 'FAILURE'
            throw e
        }
    }
}


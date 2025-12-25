package de.schaefer

class Context {

    /** Jenkins Script Context (this aus Jenkinsfile: org.jenkinsci.plugins.workflow.cps.CpsScript) */
    final def script

    Mode mode = Mode.BUILD
    Set<BuildMode> buildModes

    /** Monorepo-Infos */
    String path

    /** Build-Infos */
    String service

    /** Pipeline-Umgebung */
    final Map<String, String> env = [:]

    /** Build-Parameter */
    final Map<String, Object> params = [:]

    /** Zwischenergebnisse */
    final Map<String, Object> state = [:]

    Context(def script) {
        this.script = script
    }

    void inServiceDir(Closure body) {
        script.dir(path, body)
    }

    void log(String msg) {
        script.echo "[${service}] ${msg}"
    }

    String branchName() {
        script.env.BRANCH_NAME
    }

    boolean isMainBranch() {
        branchName() == 'main'
    }

    boolean isRelease() {
        isReleaseBranch() && isReleaseBuild()
    }

    boolean isReleaseBranch() {
        branchName().startsWith('release')
    }

    boolean isReleaseBuild() {
        mode == Mode.HOTFIX || mode == Mode.MINOR_RELEASE || mode == Mode.MAJOR_RELEASE
    }
}


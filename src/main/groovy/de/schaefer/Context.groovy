package de.schaefer

class Context {

    private static final def RELEASE = 'release'

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
    final Params params

    /** Zwischenergebnisse */
    final Map<String, Object> state = [:]

    Context(final def script, final Params params) {
        this.script = script
        this.params = params
    }

    void inServiceDir(Closure body) {
        script.dir(path, body)
    }

    void log(String msg) {
        script.echo "[${service}] ${msg}"
    }

    String branchName() {
        params.branchName
    }

    boolean isMainBranch() {
        branchName() == params.mainBranch
    }

    String mainBranch() {
        params.mainBranch
    }

    boolean isRelease() {
        isReleaseBranch() && isReleaseBuild()
    }

    boolean isReleaseBranch() {
        branchName().startsWith(RELEASE)
    }

    boolean isReleaseBuild() {
        mode == Mode.HOTFIX || mode == Mode.MINOR_RELEASE || mode == Mode.MAJOR_RELEASE
    }
}


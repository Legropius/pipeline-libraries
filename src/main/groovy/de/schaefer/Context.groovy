package de.schaefer

class Context {

    /** Jenkins Script Context (this aus Jenkinsfile) */
    final def script

    /** Monorepo-Infos */
    String service
    String path

    /** Pipeline-Umgebung */
    final Map<String, String> env = [:]

    /** Build-Parameter */
    final Map<String, Object> params = [:]

    /** Zwischenergebnisse */
    final Map<String, Object> state = [:]

    Context(def script) {
        this.script = script
    }

    /* -------------------------
       Convenience-Methoden
       ------------------------- */

    void inServiceDir(Closure body) {
        script.dir(path, body)
    }

    void log(String msg) {
        script.echo "[${service}] ${msg}"
    }

    boolean isMainBranch() {
        script.env.BRANCH_NAME == 'main'
    }
}


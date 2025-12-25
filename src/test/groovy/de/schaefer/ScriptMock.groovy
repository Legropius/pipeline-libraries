package de.schaefer

class ScriptMock {
    Map env = [:]
    List<String> echoes = []
    List<String> dirs = []

    void echo(String msg) {
        echoes << msg
    }

    void dir(String path, Closure body) {
        dirs << path
        body()
    }
}

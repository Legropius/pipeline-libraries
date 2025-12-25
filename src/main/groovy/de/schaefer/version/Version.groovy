package de.schaefer.version

interface Version {

    Version bumpMajor()

    Version bumpMinor()

    Version bumpHotfix()

    Version build()
}
package de.schaefer.version

import de.schaefer.Mode

interface Version {

    Version bumpMainAfterRelease(Mode mode)

    Version getReleaseVersion(Mode mode)

    Version build()
}
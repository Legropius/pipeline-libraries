package de.schaefer.version

import de.schaefer.Mode

interface Version {

    /**
     * Must only be called when releasing.
     *
     * @param mode determines whether a release is build or not.
     * @return a new immutable version for the main branch after a release.
     */
    Version possiblyBumpMain(Mode mode)

    /**
     * Must only be called when releasing.
     *
     * @param mode determines whether a release is build or not.
     * @return the next version when planning a release e.g. 1.2.3-SNAPSHOT => 1.2.3
     */
    Version getNextReleaseVersion(Mode mode)

    /**
     * Can always be called.
     *
     * @return a new Version when building an artifact.
     */
    Version build()
}
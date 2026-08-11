package de.schaefer

class Params {

    final String mainBranch
    final String branchName

    private Params(final Builder builder) {
        this.mainBranch = builder.mainBranch
        this.branchName = builder.branchName
    }

    static Builder builder() {
        return new Builder()
    }

    static class Builder {
        private String mainBranch = 'main'
        private String branchName

        Builder mainBranch(final String mainBranch) {
            this.mainBranch = mainBranch
            return this
        }

        Builder branchName(final String branchName) {
            this.branchName = branchName
            return this
        }

        Params build() {
            if (!branchName) {
                throw new IllegalStateException("branchName must be set")
            }
            if (!mainBranch) {
                throw new IllegalStateException("mainBranch must be set")
            }
            return new Params(this)
        }
    }
}

package com.mapzen.jpostal;

import java.util.Objects;

public final class Config {
    private final String dataDir;
    private final String libraryFile;

    private Config(final String dataDir, final String libraryFile) {
        this.dataDir = dataDir;
        this.libraryFile = libraryFile;
    }

    public String getDataDir() {
        return dataDir;
    }

    public String getLibraryFile() {
        return libraryFile;
    }

    void loadLibrary() {
        if (this.libraryFile != null) {
            System.load(this.libraryFile);
        } else {
            System.loadLibrary("jpostal");
        }
    }

    static IllegalArgumentException mismatchException(final Config current, final Config requested) {
        return new IllegalArgumentException(String.format("Config mismatch: initialized instance uses [%s], but requested [%s]", current, requested));
    }

    @Override
    public String toString() {
        return "Config{" + "dataDir=" + dataDir + ",libraryFile=" + libraryFile + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Config other)) {
            return false;
        } else {
            return Objects.equals(this.dataDir, other.dataDir) &&
                    Objects.equals(this.libraryFile, other.libraryFile);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String dataDir;
        private String libraryFile;

        private Builder() {}

        public Config build() {
            return new Config(dataDir, libraryFile);
        }

        public Builder dataDir(final String dataDir) {
            this.dataDir = dataDir;
            return this;
        }

        public Builder libraryFile(final String libraryFile) {
            this.libraryFile = libraryFile;
            return this;
        }
    }
}

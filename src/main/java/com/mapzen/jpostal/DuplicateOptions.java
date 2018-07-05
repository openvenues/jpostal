package com.mapzen.jpostal;

public class DuplicateOptions {

    private String[] languages;

    public String[] getLanguages() {
        return languages;
    }

    public void setLanguages(String[] languages) {
        this.languages = languages;
    }

    public static class Builder {
        static {
            System.loadLibrary("jpostal_dedupe"); // Load native library at runtime
        }

        private String[] languages;

        private native synchronized void setDefaultOptions();

        public Builder() {
            super();
            setDefaultOptions();
        }

        public Builder languages(String[] languages) {
            this.languages = languages;
            return this;
        }

        public DuplicateOptions build() {
            return new DuplicateOptions(this);
        }

    }

    private DuplicateOptions(Builder builder) {
        languages = builder.languages;
    }

}

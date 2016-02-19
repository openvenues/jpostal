package com.mapzen.jpostal;

public class ParserOptions {
    private String language;
    private String country;

    public static class Builder {
        static {
            System.loadLibrary("jpostal_parser"); // Load native library at runtime
        }

        private String language;
        private String country;

        private native synchronized void setDefaultOptions();

        public Builder() {
            super();
            setDefaultOptions();
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public ParserOptions build() {
            return new ParserOptions(this);
        }
    }

    public ParserOptions(Builder builder) {
        this.language = builder.language;
        this.country = builder.country;
    }

}
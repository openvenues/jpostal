package com.mapzen.jpostal;

public class ParserOptions {
    private String language;
    private String country;

    public static class Builder {
        private String language;
        private String country;

        native synchronized void setDefaultOptions();

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

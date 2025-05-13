package com.mapzen.jpostal;

public class ParserOptions {
    private final String language;
    private final String country;

    public static class Builder {
        private String language;
        private String country;

        private native void setDefaultOptions();

        public Builder() {
            if (!AddressParser.isInitialized()) {
                throw new IllegalStateException("Initialize AddressParser through getInstance* before creating a ParserOptions Builder");
            }

            synchronized (ParserOptions.class) {
                setDefaultOptions(); // Load default options from libpostal into this Builder.
            }
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

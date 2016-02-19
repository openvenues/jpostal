package com.mapzen.jpostal;

public class ExpanderOptions {

    private String[] languages;
    private short addressComponents;
    private boolean latinAscii;
    private boolean transliterate;
    private boolean stripAccents;
    private boolean decompose;
    private boolean lowercase;
    private boolean trimString;
    private boolean dropParentheticals;
    private boolean replaceNumericHyphens;
    private boolean deleteNumericHyphens;
    private boolean splitAlphaFromNumeric;
    private boolean replaceWordHyphens;
    private boolean deleteWordHyphens;
    private boolean deleteFinalPeriods;
    private boolean deleteAcronymPeriods;
    private boolean dropEnglishPossessives;
    private boolean deleteApostrophes;
    private boolean expandNumex;
    private boolean romanNumerals;

    public short getAddressComponents() {
        return this.addressComponents;
    }

    public boolean getLatinAscii() {
        return this.latinAscii;
    }

    public boolean getTransliterate() {
        return this.transliterate;
    }

    public boolean getStripAccents() {
        return this.stripAccents;
    }

    public boolean getDecompose() {
        return this.decompose;
    }

    public boolean getLowercase() {
        return this.lowercase;
    }

    public boolean getTrimString() {
        return this.trimString;
    }

    public boolean getDropParentheticals() {
        return this.dropParentheticals;
    }

    public boolean getReplaceNumericHyphens() {
        return this.replaceNumericHyphens;
    }

    public boolean getDeleteNumericHyphens() {
        return this.deleteNumericHyphens;
    }

    public boolean getSplitAlphaFromNumeric() {
        return this.splitAlphaFromNumeric;
    }

    public boolean getReplaceWordHyphens() {
        return this.replaceWordHyphens;
    }

    public boolean getDeleteWordHyphens() {
        return this.deleteWordHyphens;
    }

    public boolean getDeleteFinalPeriods() {
        return this.deleteFinalPeriods;
    }

    public boolean getDeleteAcronymPeriods() {
        return this.deleteAcronymPeriods;
    }

    public boolean getDropEnglishPossessives() {
        return this.dropEnglishPossessives;
    }

    public boolean getDeleteApostrophes() {
        return this.deleteApostrophes;
    }

    public boolean getExpandNumex() {
        return this.expandNumex;
    }

    public boolean getRomanNumerals() {
        return this.romanNumerals;
    }

    public void setAddressComponents(short addressComponents) {
        this.addressComponents = addressComponents;
    }

    public void setLatinAscii(boolean latinAscii) {
        this.latinAscii = latinAscii;
    }

    public void setTransliterate(boolean transliterate) {
        this.transliterate = transliterate;
    }

    public void setStripAccents(boolean stripAccents) {
        this.stripAccents = stripAccents;
    }

    public void setDecompose(boolean decompose) {
        this.decompose = decompose;
    }

    public void setLowercase(boolean lowercase) {
        this.lowercase = lowercase;
    }

    public void setTrimString(boolean trimString) {
        this.trimString = trimString;
    }

    public void setDropParentheticals(boolean dropParentheticals) {
        this.dropParentheticals = dropParentheticals;
    }

    public void setReplaceNumericHyphens(boolean replaceNumericHyphens) {
        this.replaceNumericHyphens = replaceNumericHyphens;
    }

    public void setDeleteNumericHyphens(boolean deleteNumericHyphens) {
        this.deleteNumericHyphens = deleteNumericHyphens;
    }

    public void setSplitAlphaFromNumeric(boolean splitAlphaFromNumeric) {
        this.splitAlphaFromNumeric = splitAlphaFromNumeric;
    }

    public void setReplaceWordHyphens(boolean replaceWordHyphens) {
        this.replaceWordHyphens = replaceWordHyphens;
    }

    public void setDeleteWordHyphens(boolean deleteWordHyphens) {
        this.deleteWordHyphens = deleteWordHyphens;
    }

    public void setDeleteFinalPeriods(boolean deleteFinalPeriods) {
        this.deleteFinalPeriods = deleteFinalPeriods;
    }

    public void setDeleteAcronymPeriods(boolean deleteAcronymPeriods) {
        this.deleteAcronymPeriods = deleteAcronymPeriods;
    }

    public void setDropEnglishPossessives(boolean dropEnglishPossessives) {
        this.dropEnglishPossessives = dropEnglishPossessives;
    }

    public void setDeleteApostrophes(boolean deleteApostrophes) {
        this.deleteApostrophes = deleteApostrophes;
    }

    public void setExpandNumex(boolean expandNumex) {
        this.expandNumex = expandNumex;
    }

    public void setRomanNumerals(boolean romanNumerals) {
        this.romanNumerals = romanNumerals;
    }

    public static class Builder {
        static {
            System.loadLibrary("jpostal_expander"); // Load native library at runtime
        }

        private String[] languages;
        private short addressComponents;
        private boolean latinAscii;
        private boolean transliterate;
        private boolean stripAccents;
        private boolean decompose;
        private boolean lowercase;
        private boolean trimString;
        private boolean dropParentheticals;
        private boolean replaceNumericHyphens;
        private boolean deleteNumericHyphens;
        private boolean splitAlphaFromNumeric;
        private boolean replaceWordHyphens;
        private boolean deleteWordHyphens;
        private boolean deleteFinalPeriods;
        private boolean deleteAcronymPeriods;
        private boolean dropEnglishPossessives;
        private boolean deleteApostrophes;
        private boolean expandNumex;
        private boolean romanNumerals;

        private native synchronized void setDefaultOptions();

        public Builder() {
            super();
            setDefaultOptions();
        }

        public Builder languages(String[] languages) { 
            this.languages = languages;
            return this;
        }

        public Builder addressComponents(short addressComponents) {
            this.addressComponents = addressComponents;
            return this;
        }

        public Builder latinAscii (boolean latinAscii) {
            this.latinAscii = latinAscii;
            return this;
        }

        public Builder transliterate (boolean transliterate) {
            this.transliterate = transliterate;
            return this;
        }

        public Builder stripAccents (boolean stripAccents) {
            this.stripAccents = stripAccents;
            return this;
        }

        public Builder decompose (boolean decompose) {
            this.decompose = decompose;
            return this;
        }

        public Builder lowercase (boolean lowercase) {
            this.lowercase = lowercase;
            return this;
        }

        public Builder trimString (boolean trimString) {
            this.trimString = trimString;
            return this;
        }

        public Builder dropParentheticals (boolean dropParentheticals) {
            this.dropParentheticals = dropParentheticals;
            return this;
        }

        public Builder replaceNumericHyphens (boolean replaceNumericHyphens) {
            this.replaceNumericHyphens = replaceNumericHyphens;
            return this;
        }

        public Builder deleteNumericHyphens (boolean deleteNumericHyphens) {
            this.deleteNumericHyphens = deleteNumericHyphens;
            return this;
        }

        public Builder splitAlphaFromNumeric (boolean splitAlphaFromNumeric) {
            this.splitAlphaFromNumeric = splitAlphaFromNumeric;
            return this;
        }

        public Builder replaceWordHyphens (boolean replaceWordHyphens) {
            this.replaceWordHyphens = replaceWordHyphens;
            return this;
        }

        public Builder deleteWordHyphens (boolean deleteWordHyphens) {
            this.deleteWordHyphens = deleteWordHyphens;
            return this;
        }

        public Builder deleteFinalPeriods (boolean deleteFinalPeriods) {
            this.deleteFinalPeriods = deleteFinalPeriods;
            return this;
        }

        public Builder deleteAcronymPeriods (boolean deleteAcronymPeriods) {
            this.deleteAcronymPeriods = deleteAcronymPeriods;
            return this;
        }

        public Builder dropEnglishPossessives (boolean dropEnglishPossessives) {
            this.dropEnglishPossessives = dropEnglishPossessives;
            return this;
        }

        public Builder deleteApostrophes (boolean deleteApostrophes) {
            this.deleteApostrophes = deleteApostrophes;
            return this;
        }

        public Builder expandNumex (boolean expandNumex) {
            this.expandNumex = expandNumex;
            return this;
        }

        public Builder romanNumerals (boolean romanNumerals) {
            this.romanNumerals = romanNumerals;
            return this;
        }

        public ExpanderOptions build() {
            return new ExpanderOptions(this);
        }

    }

    private ExpanderOptions(Builder builder) {
        languages = builder.languages;
        addressComponents = builder.addressComponents;
        latinAscii = builder.latinAscii;
        transliterate = builder.transliterate;
        stripAccents = builder.stripAccents;
        decompose = builder.decompose;
        lowercase = builder.lowercase;
        trimString = builder.trimString;
        dropParentheticals = builder.dropParentheticals;
        latinAscii = builder.latinAscii;
        replaceNumericHyphens = builder.replaceNumericHyphens;
        deleteNumericHyphens = builder.deleteNumericHyphens;
        splitAlphaFromNumeric = builder.splitAlphaFromNumeric;
        replaceWordHyphens = builder.replaceWordHyphens;
        deleteWordHyphens = builder.deleteWordHyphens;
        deleteFinalPeriods = builder.deleteFinalPeriods;
        deleteAcronymPeriods = builder.deleteAcronymPeriods;
        dropEnglishPossessives = builder.dropEnglishPossessives;
        deleteApostrophes = builder.deleteApostrophes;
        expandNumex = builder.expandNumex;
        romanNumerals = builder.romanNumerals;
    } 

}

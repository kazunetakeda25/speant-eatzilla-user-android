package com.speant.user.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlacesPojo {


    @SerializedName("predictions")
    private List<Predictions> predictions;
    @SerializedName("status")
    private String status;

    public List<Predictions> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Predictions> predictions) {
        this.predictions = predictions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class MatchedSubstrings {
        @SerializedName("length")
        private int length;
        @SerializedName("offset")
        private int offset;

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }
    }

    public static class MainTextMatchedSubstrings {
        @SerializedName("length")
        private int length;
        @SerializedName("offset")
        private int offset;

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }
    }

    public static class StructuredFormatting {
        @SerializedName("main_text")
        private String mainText;
        @SerializedName("main_text_matched_substrings")
        private List<MainTextMatchedSubstrings> mainTextMatchedSubstrings;
        @SerializedName("secondary_text")
        private String secondaryText;

        public String getMainText() {
            return mainText;
        }

        public void setMainText(String mainText) {
            this.mainText = mainText;
        }

        public List<MainTextMatchedSubstrings> getMainTextMatchedSubstrings() {
            return mainTextMatchedSubstrings;
        }

        public void setMainTextMatchedSubstrings(List<MainTextMatchedSubstrings> mainTextMatchedSubstrings) {
            this.mainTextMatchedSubstrings = mainTextMatchedSubstrings;
        }

        public String getSecondaryText() {
            return secondaryText;
        }

        public void setSecondaryText(String secondaryText) {
            this.secondaryText = secondaryText;
        }
    }

    public static class Terms {
        @SerializedName("offset")
        private int offset;
        @SerializedName("value")
        private String value;

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class Predictions {
        @SerializedName("description")
        private String description;
        @SerializedName("id")
        private String id;
        @SerializedName("matched_substrings")
        private List<MatchedSubstrings> matchedSubstrings;
        @SerializedName("place_id")
        private String placeId;
        @SerializedName("reference")
        private String reference;
        @SerializedName("structured_formatting")
        private StructuredFormatting structuredFormatting;
        @SerializedName("terms")
        private List<Terms> terms;
        @SerializedName("types")
        private List<String> types;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<MatchedSubstrings> getMatchedSubstrings() {
            return matchedSubstrings;
        }

        public void setMatchedSubstrings(List<MatchedSubstrings> matchedSubstrings) {
            this.matchedSubstrings = matchedSubstrings;
        }

        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public StructuredFormatting getStructuredFormatting() {
            return structuredFormatting;
        }

        public void setStructuredFormatting(StructuredFormatting structuredFormatting) {
            this.structuredFormatting = structuredFormatting;
        }

        public List<Terms> getTerms() {
            return terms;
        }

        public void setTerms(List<Terms> terms) {
            this.terms = terms;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }
    }
}

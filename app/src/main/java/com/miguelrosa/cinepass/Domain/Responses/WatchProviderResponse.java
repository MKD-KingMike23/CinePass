package com.miguelrosa.cinepass.Domain.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class WatchProviderResponse {
    @SerializedName("results")
    private Map<String, ProviderCountry> results;

    public Map<String, ProviderCountry> getResults() {
        return results;
    }

    public static class ProviderCountry {
        @SerializedName("link")
        private String link;

        @SerializedName("flatrate")
        private List<Provider> flatrate;

        // Getters
        public String getLink() {
            return link;
        }

        public List<Provider> getFlatrate() {
            return flatrate;
        }
    }

    public static class Provider {
        @SerializedName("provider_name")
        private String providerName;

        @SerializedName("logo_path")
        private String logoPath;

        // Getters
        public String getProviderName() {
            return providerName;
        }

        public String getLogoPath() {
            return logoPath;
        }
    }
}

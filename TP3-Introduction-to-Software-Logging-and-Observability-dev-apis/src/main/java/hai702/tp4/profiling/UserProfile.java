package hai702.tp4.profiling;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Set;

public class UserProfile {

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("profileType")
    private String profileType;

    @JsonProperty("statistics")
    private Statistics statistics;

    @JsonProperty("productsAccessed")
    private Set<String> productsAccessed;

    @JsonProperty("timestamp")
    private String timestamp;

    public UserProfile() {}

    public UserProfile(String userId, String profileType, Statistics statistics,
                       Set<String> productsAccessed) {
        this.userId = userId;
        this.profileType = profileType;
        this.statistics = statistics;
        this.productsAccessed = productsAccessed;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }

    // Getters et Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getProfileType() { return profileType; }
    public void setProfileType(String profileType) { this.profileType = profileType; }

    public Statistics getStatistics() { return statistics; }
    public void setStatistics(Statistics statistics) { this.statistics = statistics; }

    public Set<String> getProductsAccessed() { return productsAccessed; }
    public void setProductsAccessed(Set<String> productsAccessed) {
        this.productsAccessed = productsAccessed;
    }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    // Classe interne pour les statistiques
    public static class Statistics {
        @JsonProperty("totalOperations")
        private int totalOperations;

        @JsonProperty("readOperations")
        private int readOperations;

        @JsonProperty("writeOperations")
        private int writeOperations;

        @JsonProperty("expensiveSearches")
        private int expensiveSearches;

        @JsonProperty("readPercentage")
        private double readPercentage;

        @JsonProperty("writePercentage")
        private double writePercentage;

        @JsonProperty("averageExpensivePrice")
        private double averageExpensivePrice;

        @JsonProperty("uniqueProductsAccessed")
        private int uniqueProductsAccessed;

        public Statistics() {}

        // Getters et Setters
        public int getTotalOperations() { return totalOperations; }
        public void setTotalOperations(int totalOperations) {
            this.totalOperations = totalOperations;
        }

        public int getReadOperations() { return readOperations; }
        public void setReadOperations(int readOperations) {
            this.readOperations = readOperations;
        }

        public int getWriteOperations() { return writeOperations; }
        public void setWriteOperations(int writeOperations) {
            this.writeOperations = writeOperations;
        }

        public int getExpensiveSearches() { return expensiveSearches; }
        public void setExpensiveSearches(int expensiveSearches) {
            this.expensiveSearches = expensiveSearches;
        }

        public double getReadPercentage() { return readPercentage; }
        public void setReadPercentage(double readPercentage) {
            this.readPercentage = readPercentage;
        }

        public double getWritePercentage() { return writePercentage; }
        public void setWritePercentage(double writePercentage) {
            this.writePercentage = writePercentage;
        }

        public double getAverageExpensivePrice() { return averageExpensivePrice; }
        public void setAverageExpensivePrice(double averageExpensivePrice) {
            this.averageExpensivePrice = averageExpensivePrice;
        }

        public int getUniqueProductsAccessed() { return uniqueProductsAccessed; }
        public void setUniqueProductsAccessed(int uniqueProductsAccessed) {
            this.uniqueProductsAccessed = uniqueProductsAccessed;
        }
    }
}
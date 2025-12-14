package hai702.tp4.profiling;
public class UserProfile {
    @com.fasterxml.jackson.annotation.JsonProperty("userId")
    private java.lang.String userId;

    @com.fasterxml.jackson.annotation.JsonProperty("profileType")
    private java.lang.String profileType;

    @com.fasterxml.jackson.annotation.JsonProperty("statistics")
    private hai702.tp4.profiling.UserProfile.Statistics statistics;

    @com.fasterxml.jackson.annotation.JsonProperty("productsAccessed")
    private java.util.Set<java.lang.String> productsAccessed;

    @com.fasterxml.jackson.annotation.JsonProperty("timestamp")
    private java.lang.String timestamp;

    public UserProfile() {
    }

    public UserProfile(java.lang.String userId, java.lang.String profileType, hai702.tp4.profiling.UserProfile.Statistics statistics, java.util.Set<java.lang.String> productsAccessed) {
        this.userId = userId;
        this.profileType = profileType;
        this.statistics = statistics;
        this.productsAccessed = productsAccessed;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }

    // Getters et Setters
    public java.lang.String getUserId() {
        return userId;
    }

    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }

    public java.lang.String getProfileType() {
        return profileType;
    }

    public void setProfileType(java.lang.String profileType) {
        this.profileType = profileType;
    }

    public hai702.tp4.profiling.UserProfile.Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(hai702.tp4.profiling.UserProfile.Statistics statistics) {
        this.statistics = statistics;
    }

    public java.util.Set<java.lang.String> getProductsAccessed() {
        return productsAccessed;
    }

    public void setProductsAccessed(java.util.Set<java.lang.String> productsAccessed) {
        this.productsAccessed = productsAccessed;
    }

    public java.lang.String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(java.lang.String timestamp) {
        this.timestamp = timestamp;
    }

    // Classe interne pour les statistiques
    public static class Statistics {
        @com.fasterxml.jackson.annotation.JsonProperty("totalOperations")
        private int totalOperations;

        @com.fasterxml.jackson.annotation.JsonProperty("readOperations")
        private int readOperations;

        @com.fasterxml.jackson.annotation.JsonProperty("writeOperations")
        private int writeOperations;

        @com.fasterxml.jackson.annotation.JsonProperty("expensiveSearches")
        private int expensiveSearches;

        @com.fasterxml.jackson.annotation.JsonProperty("readPercentage")
        private double readPercentage;

        @com.fasterxml.jackson.annotation.JsonProperty("writePercentage")
        private double writePercentage;

        @com.fasterxml.jackson.annotation.JsonProperty("averageExpensivePrice")
        private double averageExpensivePrice;

        @com.fasterxml.jackson.annotation.JsonProperty("uniqueProductsAccessed")
        private int uniqueProductsAccessed;

        public Statistics() {
        }

        // Getters et Setters
        public int getTotalOperations() {
            return totalOperations;
        }

        public void setTotalOperations(int totalOperations) {
            this.totalOperations = totalOperations;
        }

        public int getReadOperations() {
            return readOperations;
        }

        public void setReadOperations(int readOperations) {
            this.readOperations = readOperations;
        }

        public int getWriteOperations() {
            return writeOperations;
        }

        public void setWriteOperations(int writeOperations) {
            this.writeOperations = writeOperations;
        }

        public int getExpensiveSearches() {
            return expensiveSearches;
        }

        public void setExpensiveSearches(int expensiveSearches) {
            this.expensiveSearches = expensiveSearches;
        }

        public double getReadPercentage() {
            return readPercentage;
        }

        public void setReadPercentage(double readPercentage) {
            this.readPercentage = readPercentage;
        }

        public double getWritePercentage() {
            return writePercentage;
        }

        public void setWritePercentage(double writePercentage) {
            this.writePercentage = writePercentage;
        }

        public double getAverageExpensivePrice() {
            return averageExpensivePrice;
        }

        public void setAverageExpensivePrice(double averageExpensivePrice) {
            this.averageExpensivePrice = averageExpensivePrice;
        }

        public int getUniqueProductsAccessed() {
            return uniqueProductsAccessed;
        }

        public void setUniqueProductsAccessed(int uniqueProductsAccessed) {
            this.uniqueProductsAccessed = uniqueProductsAccessed;
        }
    }
}
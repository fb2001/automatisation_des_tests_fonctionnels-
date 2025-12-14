package hai702.tp4.profiling;
public class LogAnalyzer {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(hai702.tp4.profiling.LogAnalyzer.class);

    // Patterns pour extraire les informations des logs
    private static final java.util.regex.Pattern USER_PATTERN = java.util.regex.Pattern.compile("userId=([^|\\s]+)");

    private static final java.util.regex.Pattern OPERATION_PATTERN = java.util.regex.Pattern.compile("(READ_OPERATION|WRITE_OPERATION|EXPENSIVE_PRODUCT_SEARCH)");

    private static final java.util.regex.Pattern PRODUCT_ID_PATTERN = java.util.regex.Pattern.compile("productId=([^|\\s]+)");

    private static final java.util.regex.Pattern PRICE_PATTERN = java.util.regex.Pattern.compile("price=([^|\\s]+)");

    private java.util.Map<java.lang.String, hai702.tp4.profiling.LogAnalyzer.UserActivity> userActivities = new java.util.HashMap<>();

    public void analyzeLogFile(java.lang.String logFilePath) throws java.io.IOException {
        hai702.tp4.profiling.LogAnalyzer.logger.info("Analyzing log file: {}", logFilePath);
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(logFilePath))) {
            java.lang.String line;
            while ((line = reader.readLine()) != null) {
                parseLine(line);
            } 
        }
        hai702.tp4.profiling.LogAnalyzer.logger.info("Analysis complete. Found {} users", userActivities.size());
    }

    private void parseLine(java.lang.String line) {
        // Extraire l'userId
        java.util.regex.Matcher userMatcher = hai702.tp4.profiling.LogAnalyzer.USER_PATTERN.matcher(line);
        if (!userMatcher.find()) {
            return;// Pas d'userId dans cette ligne

        }
        java.lang.String userId = userMatcher.group(1);
        // Obtenir ou créer l'activité utilisateur
        hai702.tp4.profiling.LogAnalyzer.UserActivity activity = userActivities.computeIfAbsent(userId, id -> new hai702.tp4.profiling.LogAnalyzer.UserActivity(id));
        // Extraire le type d'opération
        java.util.regex.Matcher opMatcher = hai702.tp4.profiling.LogAnalyzer.OPERATION_PATTERN.matcher(line);
        if (opMatcher.find()) {
            java.lang.String operationType = opMatcher.group(1);
            switch (operationType) {
                case "READ_OPERATION" :
                    activity.incrementReadOps();
                    break;
                case "WRITE_OPERATION" :
                    activity.incrementWriteOps();
                    break;
                case "EXPENSIVE_PRODUCT_SEARCH" :
                    activity.incrementExpensiveSearches();
                    // Extraire le prix si disponible
                    java.util.regex.Matcher priceMatcher = hai702.tp4.profiling.LogAnalyzer.PRICE_PATTERN.matcher(line);
                    if (priceMatcher.find()) {
                        try {
                            double price = java.lang.Double.parseDouble(priceMatcher.group(1));
                            activity.addExpensiveProductPrice(price);
                        } catch (java.lang.NumberFormatException e) {
                            // Ignorer si le prix n'est pas valide
                        }
                    }
                    break;
            }
            // Extraire l'ID du produit si disponible
            java.util.regex.Matcher productMatcher = hai702.tp4.profiling.LogAnalyzer.PRODUCT_ID_PATTERN.matcher(line);
            if (productMatcher.find()) {
                activity.addProductAccessed(productMatcher.group(1));
            }
        }
    }

    public java.util.Map<java.lang.String, hai702.tp4.profiling.LogAnalyzer.UserActivity> getUserActivities() {
        return userActivities;
    }

    // Classe interne pour stocker l'activité d'un utilisateur
    public static class UserActivity {
        private java.lang.String userId;

        private int readOperations = 0;

        private int writeOperations = 0;

        private int expensiveSearches = 0;

        private java.util.Set<java.lang.String> productsAccessed = new java.util.HashSet<>();

        private java.util.List<java.lang.Double> expensiveProductPrices = new java.util.ArrayList<>();

        public UserActivity(java.lang.String userId) {
            this.userId = userId;
        }

        public void incrementReadOps() {
            readOperations++;
        }

        public void incrementWriteOps() {
            writeOperations++;
        }

        public void incrementExpensiveSearches() {
            expensiveSearches++;
        }

        public void addProductAccessed(java.lang.String productId) {
            productsAccessed.add(productId);
        }

        public void addExpensiveProductPrice(double price) {
            expensiveProductPrices.add(price);
        }

        // Getters
        public java.lang.String getUserId() {
            return userId;
        }

        public int getReadOperations() {
            return readOperations;
        }

        public int getWriteOperations() {
            return writeOperations;
        }

        public int getExpensiveSearches() {
            return expensiveSearches;
        }

        public java.util.Set<java.lang.String> getProductsAccessed() {
            return productsAccessed;
        }

        public java.util.List<java.lang.Double> getExpensiveProductPrices() {
            return expensiveProductPrices;
        }

        public int getTotalOperations() {
            return readOperations + writeOperations;
        }

        public double getReadPercentage() {
            int total = getTotalOperations();
            return total == 0 ? 0 : (readOperations * 100.0) / total;
        }

        public double getWritePercentage() {
            int total = getTotalOperations();
            return total == 0 ? 0 : (writeOperations * 100.0) / total;
        }

        public double getAverageExpensivePrice() {
            return expensiveProductPrices.stream().mapToDouble(java.lang.Double::doubleValue).average().orElse(0.0);
        }
    }
}
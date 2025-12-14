package hai702.tp4.profiling;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogAnalyzer {

    private static final Logger logger = LoggerFactory.getLogger(LogAnalyzer.class);

    // Patterns pour extraire les informations des logs
    private static final Pattern USER_PATTERN = Pattern.compile("userId=([^|\\s]+)");
    private static final Pattern OPERATION_PATTERN = Pattern.compile("(READ_OPERATION|WRITE_OPERATION|EXPENSIVE_PRODUCT_SEARCH)");
    private static final Pattern PRODUCT_ID_PATTERN = Pattern.compile("productId=([^|\\s]+)");
    private static final Pattern PRICE_PATTERN = Pattern.compile("price=([^|\\s]+)");

    private Map<String, UserActivity> userActivities = new HashMap<>();

    public void analyzeLogFile(String logFilePath) throws IOException {
        logger.info("Analyzing log file: {}", logFilePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                parseLine(line);
            }
        }

        logger.info("Analysis complete. Found {} users", userActivities.size());
    }

    private void parseLine(String line) {
        // Extraire l'userId
        Matcher userMatcher = USER_PATTERN.matcher(line);
        if (!userMatcher.find()) {
            return; // Pas d'userId dans cette ligne
        }
        String userId = userMatcher.group(1);

        // Obtenir ou créer l'activité utilisateur
        UserActivity activity = userActivities.computeIfAbsent(
                userId,
                id -> new UserActivity(id)
        );

        // Extraire le type d'opération
        Matcher opMatcher = OPERATION_PATTERN.matcher(line);
        if (opMatcher.find()) {
            String operationType = opMatcher.group(1);

            switch (operationType) {
                case "READ_OPERATION":
                    activity.incrementReadOps();
                    break;

                case "WRITE_OPERATION":
                    activity.incrementWriteOps();
                    break;

                case "EXPENSIVE_PRODUCT_SEARCH":
                    activity.incrementExpensiveSearches();

                    // Extraire le prix si disponible
                    Matcher priceMatcher = PRICE_PATTERN.matcher(line);
                    if (priceMatcher.find()) {
                        try {
                            double price = Double.parseDouble(priceMatcher.group(1));
                            activity.addExpensiveProductPrice(price);
                        } catch (NumberFormatException e) {
                            // Ignorer si le prix n'est pas valide
                        }
                    }
                    break;
            }

            // Extraire l'ID du produit si disponible
            Matcher productMatcher = PRODUCT_ID_PATTERN.matcher(line);
            if (productMatcher.find()) {
                activity.addProductAccessed(productMatcher.group(1));
            }
        }
    }

    public Map<String, UserActivity> getUserActivities() {
        return userActivities;
    }

    // Classe interne pour stocker l'activité d'un utilisateur
    public static class UserActivity {
        private String userId;
        private int readOperations = 0;
        private int writeOperations = 0;
        private int expensiveSearches = 0;
        private Set<String> productsAccessed = new HashSet<>();
        private List<Double> expensiveProductPrices = new ArrayList<>();

        public UserActivity(String userId) {
            this.userId = userId;
        }

        public void incrementReadOps() { readOperations++; }
        public void incrementWriteOps() { writeOperations++; }
        public void incrementExpensiveSearches() { expensiveSearches++; }
        public void addProductAccessed(String productId) { productsAccessed.add(productId); }
        public void addExpensiveProductPrice(double price) { expensiveProductPrices.add(price); }

        // Getters
        public String getUserId() { return userId; }
        public int getReadOperations() { return readOperations; }
        public int getWriteOperations() { return writeOperations; }
        public int getExpensiveSearches() { return expensiveSearches; }
        public Set<String> getProductsAccessed() { return productsAccessed; }
        public List<Double> getExpensiveProductPrices() { return expensiveProductPrices; }

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
            return expensiveProductPrices.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
        }
    }
}
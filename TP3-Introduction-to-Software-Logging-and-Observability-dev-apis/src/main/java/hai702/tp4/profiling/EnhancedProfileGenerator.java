package hai702.tp4.profiling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class EnhancedProfileGenerator {

    private static final Logger logger = LoggerFactory.getLogger(EnhancedProfileGenerator.class);
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    // Seuils pour la classification des profils
    private static final double READ_DOMINANT_THRESHOLD = 60.0;
    private static final double WRITE_DOMINANT_THRESHOLD = 60.0;
    private static final int EXPENSIVE_SEARCH_THRESHOLD = 3;

    /**
     * Génère les profils à partir des LPS parsées
     */
    public void generateProfilesFromLPS(List<LogPrintStructure> lpsList, String outputDir)
            throws IOException {

        logger.info("Génération des profils à partir de {} LPS", lpsList.size());

        // Créer le répertoire de sortie
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Agréger les activités par utilisateur
        Map<Integer, UserActivityFromLPS> userActivities = aggregateActivities(lpsList);

        logger.info("Activités agrégées pour {} utilisateurs", userActivities.size());

        // Générer les profils
        List<UserProfile> readProfiles = new ArrayList<>();
        List<UserProfile> writeProfiles = new ArrayList<>();
        List<UserProfile> expensiveProfiles = new ArrayList<>();

        for (UserActivityFromLPS activity : userActivities.values()) {
            UserProfile.Statistics stats = buildStatistics(activity);

            // Profil READ dominant
            if (activity.getReadPercentage() >= READ_DOMINANT_THRESHOLD) {
                UserProfile profile = new UserProfile(
                        String.valueOf(activity.getUserId()),
                        "READ_DOMINANT",
                        stats,
                        activity.getProductsAccessed()
                );
                readProfiles.add(profile);
            }

            // Profil WRITE dominant
            if (activity.getWritePercentage() >= WRITE_DOMINANT_THRESHOLD) {
                UserProfile profile = new UserProfile(
                        String.valueOf(activity.getUserId()),
                        "WRITE_DOMINANT",
                        stats,
                        activity.getProductsAccessed()
                );
                writeProfiles.add(profile);
            }

            // Profil recherche de produits chers
            if (activity.getExpensiveSearchCount() >= EXPENSIVE_SEARCH_THRESHOLD) {
                UserProfile profile = new UserProfile(
                        String.valueOf(activity.getUserId()),
                        "EXPENSIVE_PRODUCT_SEEKER",
                        stats,
                        activity.getProductsAccessed()
                );
                expensiveProfiles.add(profile);
            }
        }

        // Sauvegarder les profils
        saveProfiles(readProfiles, outputDir + "/read_dominant_profiles.json");
        saveProfiles(writeProfiles, outputDir + "/write_dominant_profiles.json");
        saveProfiles(expensiveProfiles, outputDir + "/expensive_seeker_profiles.json");

        logger.info("Profils générés: {} READ, {} WRITE, {} EXPENSIVE",
                readProfiles.size(), writeProfiles.size(), expensiveProfiles.size());
    }

    /**
     * Agrège les LPS par utilisateur pour construire les activités
     */
    private Map<Integer, UserActivityFromLPS> aggregateActivities(List<LogPrintStructure> lpsList) {
        Map<Integer, UserActivityFromLPS> activities = new HashMap<>();

        for (LogPrintStructure lps : lpsList) {
            // Ignorer les LPS sans userId
            if (lps.getUserId() == null) {
                continue;
            }

            int userId = lps.getUserId();
            UserActivityFromLPS activity = activities.computeIfAbsent(
                    userId,
                    UserActivityFromLPS::new
            );

            // Mettre à jour l'activité selon la catégorie de l'événement
            String category = lps.getEventCategory();
            if (category != null) {
                switch (category) {
                    case "READ":
                        activity.incrementReadOps();
                        break;
                    case "WRITE":
                        activity.incrementWriteOps();
                        break;
                    case "SEARCH":
                        activity.incrementExpensiveSearches();
                        if (lps.getPrice() != null) {
                            activity.addExpensivePrice(lps.getPrice());
                        }
                        break;
                }
            }

            // Ajouter le produit accédé
            if (lps.getProductId() != null) {
                activity.addProductAccessed(String.valueOf(lps.getProductId()));
            }
        }

        return activities;
    }

    /**
     * Construit les statistiques pour un profil
     */
    private UserProfile.Statistics buildStatistics(UserActivityFromLPS activity) {
        UserProfile.Statistics stats = new UserProfile.Statistics();
        stats.setTotalOperations(activity.getTotalOperations());
        stats.setReadOperations(activity.getReadOps());
        stats.setWriteOperations(activity.getWriteOps());
        stats.setExpensiveSearches(activity.getExpensiveSearchCount());
        stats.setReadPercentage(activity.getReadPercentage());
        stats.setWritePercentage(activity.getWritePercentage());
        stats.setAverageExpensivePrice(activity.getAverageExpensivePrice());
        stats.setUniqueProductsAccessed(activity.getProductsAccessed().size());
        return stats;
    }

    /**
     * Sauvegarde les profils en JSON
     */
    private void saveProfiles(List<UserProfile> profiles, String filename) throws IOException {
        if (profiles.isEmpty()) {
            logger.warn("Aucun profil à sauvegarder pour: {}", filename);
            return;
        }

        File file = new File(filename);
        objectMapper.writeValue(file, profiles);
        logger.info("Sauvegardé {} profils dans {}", profiles.size(), filename);
    }

    /**
     * Classe pour agréger les activités utilisateur à partir des LPS
     */
    private static class UserActivityFromLPS {
        private final int userId;
        private int readOps = 0;
        private int writeOps = 0;
        private int expensiveSearches = 0;
        private final Set<String> productsAccessed = new HashSet<>();
        private final List<Double> expensivePrices = new ArrayList<>();

        public UserActivityFromLPS(int userId) {
            this.userId = userId;
        }

        public void incrementReadOps() { readOps++; }
        public void incrementWriteOps() { writeOps++; }
        public void incrementExpensiveSearches() { expensiveSearches++; }
        public void addProductAccessed(String productId) { productsAccessed.add(productId); }
        public void addExpensivePrice(double price) { expensivePrices.add(price); }

        public int getUserId() { return userId; }
        public int getReadOps() { return readOps; }
        public int getWriteOps() { return writeOps; }
        public int getExpensiveSearchCount() { return expensiveSearches; }
        public Set<String> getProductsAccessed() { return productsAccessed; }

        public int getTotalOperations() {
            return readOps + writeOps;
        }

        public double getReadPercentage() {
            int total = getTotalOperations();
            return total == 0 ? 0 : (readOps * 100.0) / total;
        }

        public double getWritePercentage() {
            int total = getTotalOperations();
            return total == 0 ? 0 : (writeOps * 100.0) / total;
        }

        public double getAverageExpensivePrice() {
            return expensivePrices.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
        }
    }

    /**
     * Méthode principale - Pipeline complet
     */
    public static void main(String[] args) {
        try {
            logger.info("=== Début du pipeline de génération de profils ===");

            // Étape 1 : Parser les logs en LPS
            LogParser parser = new LogParser();
            List<LogPrintStructure> lpsList = parser.parseLogFile("logs/app.log");
            logger.info("✓ {} LPS parsées", lpsList.size());

            // Étape 2 : Générer les profils à partir des LPS
            EnhancedProfileGenerator generator = new EnhancedProfileGenerator();
            generator.generateProfilesFromLPS(lpsList, "profiles");
            logger.info("✓ Profils générés avec succès");

            // Étape 3 : Afficher quelques statistiques
            System.out.println("\n=== STATISTIQUES DES LPS ===");

            Map<String, Long> categoryCounts = lpsList.stream()
                    .filter(lps -> lps.getEventCategory() != null)
                    .collect(Collectors.groupingBy(
                            LogPrintStructure::getEventCategory,
                            Collectors.counting()
                    ));

            categoryCounts.forEach((category, count) ->
                    System.out.println(category + ": " + count + " opérations")
            );

            long uniqueUsers = lpsList.stream()
                    .map(LogPrintStructure::getUserId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .count();

            System.out.println("\nUtilisateurs uniques: " + uniqueUsers);

            System.out.println("\n✓ Pipeline terminé avec succès !");

        } catch (IOException e) {
            logger.error("Erreur lors du pipeline", e);
            e.printStackTrace();
        }
    }
}
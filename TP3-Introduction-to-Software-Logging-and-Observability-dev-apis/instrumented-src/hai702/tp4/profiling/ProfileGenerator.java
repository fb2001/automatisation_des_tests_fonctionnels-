package hai702.tp4.profiling;
import SerializationFeature.INDENT_OUTPUT;
public class ProfileGenerator {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(hai702.tp4.profiling.ProfileGenerator.class);

    private static final com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper().enable(INDENT_OUTPUT);

    // Seuils pour déterminer les profils
    private static final double READ_DOMINANT_THRESHOLD = 60.0;// 60% de reads


    private static final double WRITE_DOMINANT_THRESHOLD = 60.0;// 60% de writes


    private static final int EXPENSIVE_SEARCH_THRESHOLD = 3;// Au moins 3 recherches


    public void generateProfiles(java.util.Map<java.lang.String, hai702.tp4.profiling.LogAnalyzer.UserActivity> activities, java.lang.String outputDir) throws java.io.IOException {
        hai702.tp4.profiling.ProfileGenerator.logger.info("Generating profiles for {} users", activities.size());
        // Créer le répertoire de sortie s'il n'existe pas
        java.io.File dir = new java.io.File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        java.util.List<hai702.tp4.profiling.UserProfile> readProfiles = new java.util.ArrayList<>();
        java.util.List<hai702.tp4.profiling.UserProfile> writeProfiles = new java.util.ArrayList<>();
        java.util.List<hai702.tp4.profiling.UserProfile> expensiveProfiles = new java.util.ArrayList<>();
        for (hai702.tp4.profiling.LogAnalyzer.UserActivity activity : activities.values()) {
            // Créer les statistiques
            hai702.tp4.profiling.UserProfile.Statistics stats = new hai702.tp4.profiling.UserProfile.Statistics();
            stats.setTotalOperations(activity.getTotalOperations());
            stats.setReadOperations(activity.getReadOperations());
            stats.setWriteOperations(activity.getWriteOperations());
            stats.setExpensiveSearches(activity.getExpensiveSearches());
            stats.setReadPercentage(activity.getReadPercentage());
            stats.setWritePercentage(activity.getWritePercentage());
            stats.setAverageExpensivePrice(activity.getAverageExpensivePrice());
            stats.setUniqueProductsAccessed(activity.getProductsAccessed().size());
            // Profil READ dominant
            if (activity.getReadPercentage() >= hai702.tp4.profiling.ProfileGenerator.READ_DOMINANT_THRESHOLD) {
                hai702.tp4.profiling.UserProfile profile = new hai702.tp4.profiling.UserProfile(activity.getUserId(), "READ_DOMINANT", stats, activity.getProductsAccessed());
                readProfiles.add(profile);
            }
            // Profil WRITE dominant
            if (activity.getWritePercentage() >= hai702.tp4.profiling.ProfileGenerator.WRITE_DOMINANT_THRESHOLD) {
                hai702.tp4.profiling.UserProfile profile = new hai702.tp4.profiling.UserProfile(activity.getUserId(), "WRITE_DOMINANT", stats, activity.getProductsAccessed());
                writeProfiles.add(profile);
            }
            // Profil recherche de produits chers
            if (activity.getExpensiveSearches() >= hai702.tp4.profiling.ProfileGenerator.EXPENSIVE_SEARCH_THRESHOLD) {
                hai702.tp4.profiling.UserProfile profile = new hai702.tp4.profiling.UserProfile(activity.getUserId(), "EXPENSIVE_PRODUCT_SEEKER", stats, activity.getProductsAccessed());
                expensiveProfiles.add(profile);
            }
        }
        // Sauvegarder les profils en JSON
        saveProfiles(readProfiles, outputDir + "/read_dominant_profiles.json");
        saveProfiles(writeProfiles, outputDir + "/write_dominant_profiles.json");
        saveProfiles(expensiveProfiles, outputDir + "/expensive_seeker_profiles.json");
        hai702.tp4.profiling.ProfileGenerator.logger.info("Profiles generated: {} READ, {} WRITE, {} EXPENSIVE", readProfiles.size(), writeProfiles.size(), expensiveProfiles.size());
    }

    private void saveProfiles(java.util.List<hai702.tp4.profiling.UserProfile> profiles, java.lang.String filename) throws java.io.IOException {
        if (profiles.isEmpty()) {
            hai702.tp4.profiling.ProfileGenerator.logger.warn("No profiles to save for: {}", filename);
            return;
        }
        java.io.File file = new java.io.File(filename);
        hai702.tp4.profiling.ProfileGenerator.objectMapper.writeValue(file, profiles);
        hai702.tp4.profiling.ProfileGenerator.logger.info("Saved {} profiles to {}", profiles.size(), filename);
    }

    public static void main(java.lang.String[] args) {
        try {
            // Analyser les logs
            hai702.tp4.profiling.LogAnalyzer analyzer = new hai702.tp4.profiling.LogAnalyzer();
            analyzer.analyzeLogFile("logs/app.log");
            // Générer les profils
            hai702.tp4.profiling.ProfileGenerator generator = new hai702.tp4.profiling.ProfileGenerator();
            generator.generateProfiles(analyzer.getUserActivities(), "profiles");
            java.lang.System.out.println("✓ Profile generation completed successfully!");
        } catch (java.io.IOException e) {
            hai702.tp4.profiling.ProfileGenerator.logger.error("Error generating profiles", e);
            e.printStackTrace();
        }
    }
}
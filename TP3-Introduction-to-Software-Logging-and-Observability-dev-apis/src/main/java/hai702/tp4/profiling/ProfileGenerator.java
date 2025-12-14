package hai702.tp4.profiling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProfileGenerator {

    private static final Logger logger = LoggerFactory.getLogger(ProfileGenerator.class);
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    // Seuils pour déterminer les profils
    private static final double READ_DOMINANT_THRESHOLD = 60.0;  // 60% de reads
    private static final double WRITE_DOMINANT_THRESHOLD = 60.0; // 60% de writes
    private static final int EXPENSIVE_SEARCH_THRESHOLD = 3;     // Au moins 3 recherches

    public void generateProfiles(Map<String, LogAnalyzer.UserActivity> activities,
                                 String outputDir) throws IOException {

        logger.info("Generating profiles for {} users", activities.size());

        // Créer le répertoire de sortie s'il n'existe pas
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        List<UserProfile> readProfiles = new ArrayList<>();
        List<UserProfile> writeProfiles = new ArrayList<>();
        List<UserProfile> expensiveProfiles = new ArrayList<>();

        for (LogAnalyzer.UserActivity activity : activities.values()) {

            // Créer les statistiques
            UserProfile.Statistics stats = new UserProfile.Statistics();
            stats.setTotalOperations(activity.getTotalOperations());
            stats.setReadOperations(activity.getReadOperations());
            stats.setWriteOperations(activity.getWriteOperations());
            stats.setExpensiveSearches(activity.getExpensiveSearches());
            stats.setReadPercentage(activity.getReadPercentage());
            stats.setWritePercentage(activity.getWritePercentage());
            stats.setAverageExpensivePrice(activity.getAverageExpensivePrice());
            stats.setUniqueProductsAccessed(activity.getProductsAccessed().size());

            // Profil READ dominant
            if (activity.getReadPercentage() >= READ_DOMINANT_THRESHOLD) {
                UserProfile profile = new UserProfile(
                        activity.getUserId(),
                        "READ_DOMINANT",
                        stats,
                        activity.getProductsAccessed()
                );
                readProfiles.add(profile);
            }

            // Profil WRITE dominant
            if (activity.getWritePercentage() >= WRITE_DOMINANT_THRESHOLD) {
                UserProfile profile = new UserProfile(
                        activity.getUserId(),
                        "WRITE_DOMINANT",
                        stats,
                        activity.getProductsAccessed()
                );
                writeProfiles.add(profile);
            }

            // Profil recherche de produits chers
            if (activity.getExpensiveSearches() >= EXPENSIVE_SEARCH_THRESHOLD) {
                UserProfile profile = new UserProfile(
                        activity.getUserId(),
                        "EXPENSIVE_PRODUCT_SEEKER",
                        stats,
                        activity.getProductsAccessed()
                );
                expensiveProfiles.add(profile);
            }
        }

        // Sauvegarder les profils en JSON
        saveProfiles(readProfiles, outputDir + "/read_dominant_profiles.json");
        saveProfiles(writeProfiles, outputDir + "/write_dominant_profiles.json");
        saveProfiles(expensiveProfiles, outputDir + "/expensive_seeker_profiles.json");

        logger.info("Profiles generated: {} READ, {} WRITE, {} EXPENSIVE",
                readProfiles.size(), writeProfiles.size(), expensiveProfiles.size());
    }

    private void saveProfiles(List<UserProfile> profiles, String filename) throws IOException {
        if (profiles.isEmpty()) {
            logger.warn("No profiles to save for: {}", filename);
            return;
        }

        File file = new File(filename);
        objectMapper.writeValue(file, profiles);
        logger.info("Saved {} profiles to {}", profiles.size(), filename);
    }

    public static void main(String[] args) {
        try {
            // Analyser les logs
            LogAnalyzer analyzer = new LogAnalyzer();
            analyzer.analyzeLogFile("logs/app.log");

            // Générer les profils
            ProfileGenerator generator = new ProfileGenerator();
            generator.generateProfiles(analyzer.getUserActivities(), "profiles");

            System.out.println("✓ Profile generation completed successfully!");

        } catch (IOException e) {
            logger.error("Error generating profiles", e);
            e.printStackTrace();
        }
    }
}
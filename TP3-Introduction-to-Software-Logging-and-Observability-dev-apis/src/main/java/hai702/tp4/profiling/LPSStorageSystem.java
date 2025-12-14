package hai702.tp4.profiling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * LPSStorageSystem - Stockage et affichage des Log Print Structures
 *
 * Cette classe permet de :
 * 1. Sauvegarder toutes les LPS en JSON
 * 2. Afficher les LPS dans un format structuré et lisible
 * 3. Organiser les LPS par utilisateur
 * 4. Générer des rapports textuels
 */
public class LPSStorageSystem {

    private static final Logger logger = LoggerFactory.getLogger(LPSStorageSystem.class);
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.INDENT_OUTPUT);

    /**
     * Sauvegarde toutes les LPS en JSON
     */
    public void saveAllLPS(List<LogPrintStructure> lpsList, String filename) throws IOException {
        File file = new File(filename);

        // Créer le répertoire parent si nécessaire
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        objectMapper.writeValue(file, lpsList);
        logger.info("✓ {} LPS sauvegardées dans {}", lpsList.size(), filename);
    }

    /**
     * Sauvegarde les LPS organisées par utilisateur
     */
    public void saveLPSByUser(List<LogPrintStructure> lpsList, String outputDir) throws IOException {
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Grouper par userId
        Map<Integer, List<LogPrintStructure>> lpsByUser = lpsList.stream()
                .filter(lps -> lps.getUserId() != null)
                .collect(Collectors.groupingBy(LogPrintStructure::getUserId));

        // Sauvegarder un fichier par utilisateur
        for (Map.Entry<Integer, List<LogPrintStructure>> entry : lpsByUser.entrySet()) {
            int userId = entry.getKey();
            List<LogPrintStructure> userLPS = entry.getValue();

            String filename = String.format("%s/user_%d_lps.json", outputDir, userId);
            objectMapper.writeValue(new File(filename), userLPS);

            logger.info("✓ Utilisateur {} : {} LPS sauvegardées", userId, userLPS.size());
        }
    }

    /**
     * Charge les LPS depuis un fichier JSON
     */
    public List<LogPrintStructure> loadLPS(String filename) throws IOException {
        return objectMapper.readValue(
                new File(filename),
                objectMapper.getTypeFactory().constructCollectionType(List.class, LogPrintStructure.class)
        );
    }

    /**
     * Affiche les LPS dans un format structuré lisible
     */
    public void displayLPS(List<LogPrintStructure> lpsList, int maxToDisplay) {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║           LOG PRINT STRUCTURES (LPS) - FORMAT STRUCTURÉ                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════╝\n");

        int count = 0;
        for (LogPrintStructure lps : lpsList) {
            if (maxToDisplay > 0 && count >= maxToDisplay) {
                System.out.println("\n... (" + (lpsList.size() - maxToDisplay) + " LPS supplémentaires non affichées)\n");
                break;
            }

            displaySingleLPS(lps, count + 1);
            count++;
        }
    }

    /**
     * Affiche une seule LPS dans un format structuré
     */
    private void displaySingleLPS(LogPrintStructure lps, int index) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println("┌─────────────────────────────────────────────────────────────────────────┐");
        System.out.printf("│ LPS #%-4d                                                                │%n", index);
        System.out.println("├─────────────────────────────────────────────────────────────────────────┤");

        // Timestamp
        System.out.printf("│ Timestamp: %-60s │%n",
                lps.getTimestamp() != null ? lps.getTimestamp().format(formatter) : "N/A");

        System.out.println("├─────────────────────────────────────────────────────────────────────────┤");

        // Event
        System.out.println("│ Event:                                                                  │");
        System.out.printf("│   - Type     : %-56s │%n",
                lps.getOperationType() != null ? lps.getOperationType() : "N/A");
        System.out.printf("│   - Category : %-56s │%n",
                lps.getEventCategory() != null ? lps.getEventCategory() : "N/A");
        System.out.printf("│   - Method   : %-56s │%n",
                lps.getMethodName() != null ? lps.getMethodName() : "N/A");

        System.out.println("├─────────────────────────────────────────────────────────────────────────┤");

        // User
        System.out.println("│ User:                                                                   │");
        System.out.printf("│   - ID       : %-56s │%n",
                lps.getUserId() != null ? lps.getUserId() : "N/A");

        System.out.println("├─────────────────────────────────────────────────────────────────────────┤");

        // Action
        System.out.println("│ Action:                                                                 │");
        System.out.printf("│   - Product ID : %-52s │%n",
                lps.getProductId() != null ? lps.getProductId() : "N/A");
        System.out.printf("│   - Price      : %-52s │%n",
                lps.getPrice() != null ? String.format("%.2f", lps.getPrice()) : "N/A");
        System.out.printf("│   - Threshold  : %-52s │%n",
                lps.getThreshold() != null ? String.format("%.2f", lps.getThreshold()) : "N/A");

        System.out.println("└─────────────────────────────────────────────────────────────────────────┘\n");
    }

    /**
     * Génère un rapport textuel structuré
     */
    public void generateTextReport(List<LogPrintStructure> lpsList, String filename) throws IOException {
        StringBuilder report = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        report.append("═══════════════════════════════════════════════════════════════════════════\n");
        report.append("                    RAPPORT DES LOG PRINT STRUCTURES                       \n");
        report.append("═══════════════════════════════════════════════════════════════════════════\n\n");

        for (int i = 0; i < lpsList.size(); i++) {
            LogPrintStructure lps = lpsList.get(i);

            report.append(String.format("LPS #%d\n", i + 1));
            report.append("───────────────────────────────────────────────────────────────────────────\n");

            report.append(String.format("Timestamp: %s\n",
                    lps.getTimestamp() != null ? lps.getTimestamp().format(formatter) : "N/A"));

            report.append("\nEvent:\n");
            report.append(String.format("  Type     : %s\n", lps.getOperationType() != null ? lps.getOperationType() : "N/A"));
            report.append(String.format("  Category : %s\n", lps.getEventCategory() != null ? lps.getEventCategory() : "N/A"));
            report.append(String.format("  Method   : %s\n", lps.getMethodName() != null ? lps.getMethodName() : "N/A"));

            report.append("\nUser:\n");
            report.append(String.format("  ID       : %s\n", lps.getUserId() != null ? lps.getUserId() : "N/A"));

            report.append("\nAction:\n");
            report.append(String.format("  Product ID : %s\n", lps.getProductId() != null ? lps.getProductId() : "N/A"));
            report.append(String.format("  Price      : %s\n", lps.getPrice() != null ? String.format("%.2f", lps.getPrice()) : "N/A"));
            report.append(String.format("  Threshold  : %s\n", lps.getThreshold() != null ? String.format("%.2f", lps.getThreshold()) : "N/A"));

            report.append("\n═══════════════════════════════════════════════════════════════════════════\n\n");
        }

        // Sauvegarder le rapport
        File file = new File(filename);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        java.nio.file.Files.writeString(file.toPath(), report.toString());
        logger.info("✓ Rapport textuel généré : {}", filename);
    }

    /**
     * Génère des statistiques sur les LPS
     */
    public void generateStatistics(List<LogPrintStructure> lpsList) {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                         STATISTIQUES DES LPS                              ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════╝\n");

        // Total
        System.out.printf("Total de LPS : %d%n%n", lpsList.size());

        // Par catégorie
        Map<String, Long> byCategory = lpsList.stream()
                .filter(lps -> lps.getEventCategory() != null)
                .collect(Collectors.groupingBy(LogPrintStructure::getEventCategory, Collectors.counting()));

        System.out.println("Par catégorie d'événement :");
        byCategory.forEach((cat, count) ->
                System.out.printf("  - %-10s : %d (%.1f%%)%n", cat, count, (count * 100.0) / lpsList.size()));

        // Par utilisateur
        Map<Integer, Long> byUser = lpsList.stream()
                .filter(lps -> lps.getUserId() != null)
                .collect(Collectors.groupingBy(LogPrintStructure::getUserId, Collectors.counting()));

        System.out.printf("%nNombre d'utilisateurs uniques : %d%n", byUser.size());

        // Prix moyens
        double avgPrice = lpsList.stream()
                .filter(lps -> lps.getPrice() != null)
                .mapToDouble(LogPrintStructure::getPrice)
                .average()
                .orElse(0.0);

        System.out.printf("%nPrix moyen des produits accédés : %.2f%n", avgPrice);

        System.out.println("\n═══════════════════════════════════════════════════════════════════════════\n");
    }

    /**
     * Programme principal de démonstration
     */
    public static void main(String[] args) {
        try {
            LPSStorageSystem storage = new LPSStorageSystem();

            System.out.println("╔═══════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║              SYSTÈME DE STOCKAGE DES LOG PRINT STRUCTURES                ║");
            System.out.println("╚═══════════════════════════════════════════════════════════════════════════╝\n");

            // Étape 1 : Parser les logs
            logger.info("Étape 1 : Parsing des logs...");
            LogParser parser = new LogParser();
            List<LogPrintStructure> lpsList = parser.parseLogFile("logs/app.log");
            logger.info("✓ {} LPS parsées", lpsList.size());

            // Étape 2 : Sauvegarder toutes les LPS en JSON
            logger.info("\nÉtape 2 : Sauvegarde des LPS...");
            storage.saveAllLPS(lpsList, "lps_output/all_lps.json");

            // Étape 3 : Sauvegarder par utilisateur
            logger.info("\nÉtape 3 : Sauvegarde par utilisateur...");
            storage.saveLPSByUser(lpsList, "lps_output/by_user");

            // Étape 4 : Générer un rapport textuel
            logger.info("\nÉtape 4 : Génération du rapport textuel...");
            storage.generateTextReport(lpsList, "lps_output/lps_report.txt");

            // Étape 5 : Afficher quelques LPS
            logger.info("\nÉtape 5 : Affichage des LPS...");
            storage.displayLPS(lpsList, 5);

            // Étape 6 : Statistiques
            logger.info("\nÉtape 6 : Génération des statistiques...");
            storage.generateStatistics(lpsList);

            System.out.println("\n✓✓✓ Système de stockage des LPS terminé avec succès ! ✓✓✓\n");
            System.out.println("Fichiers générés :");
            System.out.println("  - lps_output/all_lps.json            (Toutes les LPS en JSON)");
            System.out.println("  - lps_output/by_user/user_X_lps.json (LPS par utilisateur)");
            System.out.println("  - lps_output/lps_report.txt          (Rapport textuel structuré)");

        } catch (IOException e) {
            logger.error("Erreur lors du traitement", e);
            e.printStackTrace();
        }
    }
}
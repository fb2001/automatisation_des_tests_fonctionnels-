package hai702.tp4.profiling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LogParser {

    private static final Logger logger = LoggerFactory.getLogger(LogParser.class);

    // Pattern regex pour extraire les composants du log
    private static final Pattern LOG_PATTERN = Pattern.compile(
            "\\[(.+?)\\]\\s+(\\w+)\\s+(\\w+)\\s+-\\s+(.+)"
    );

    // Patterns pour extraire les informations métier
    private static final Pattern OPERATION_PATTERN = Pattern.compile(
            "(READ_OPERATION|WRITE_OPERATION|EXPENSIVE_PRODUCT_SEARCH)"
    );
    private static final Pattern METHOD_PATTERN = Pattern.compile(
            ":\\s*(\\w+)"
    );
    private static final Pattern USER_ID_PATTERN = Pattern.compile("userId=(\\d+)");
    private static final Pattern PRODUCT_ID_PATTERN = Pattern.compile("productId=(\\d+)");
    private static final Pattern PRICE_PATTERN = Pattern.compile("price=([\\d.]+)");
    private static final Pattern THRESHOLD_PATTERN = Pattern.compile("threshold=([\\d.]+)");

    /**
     * Parse un fichier de log complet et retourne une liste de LPS
     */
    public List<LogPrintStructure> parseLogFile(String logFilePath) throws IOException {
        List<LogPrintStructure> structures = new ArrayList<>();

        logger.info("Début du parsing du fichier : {}", logFilePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                try {
                    LogPrintStructure lps = parseLine(line);
                    if (lps != null) {
                        structures.add(lps);
                    }
                } catch (Exception e) {
                    logger.warn("Erreur lors du parsing de la ligne {} : {}", lineNumber, e.getMessage());
                }
            }
        }

        logger.info("Parsing terminé : {} structures extraites", structures.size());
        return structures;
    }

    /**
     * Parse une ligne de log et construit une LogPrintStructure
     * Utilise le Builder Pattern pour construire progressivement la structure
     */
    public LogPrintStructure parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        Matcher logMatcher = LOG_PATTERN.matcher(line);
        if (!logMatcher.matches()) {
            logger.debug("Ligne de log non reconnue : {}", line);
            return null;
        }

        // Extraction des composants de base
        String timestamp = logMatcher.group(1);
        String level = logMatcher.group(2);
        String className = logMatcher.group(3);
        String message = logMatcher.group(4);

        // Utilisation du Builder Pattern pour construire la LPS
        LogPrintStructure.Builder builder = new LogPrintStructure.Builder()
                .setTimestamp(parseTimestamp(timestamp))
                .setLevel(level)
                .setClassName(className)
                .setRawMessage(message);

        // Extraction des informations métier
        extractEventInfo(message, builder);
        extractUserInfo(message, builder);
        extractActionInfo(message, builder);

        return builder.build();
    }

    /**
     * Parse le timestamp du log
     */
    private LocalDateTime parseTimestamp(String timestampStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(timestampStr, formatter);
        } catch (Exception e) {
            logger.warn("Impossible de parser le timestamp : {}", timestampStr);
            return LocalDateTime.now();
        }
    }

    /**
     * Extrait les informations sur l'événement (type d'opération)
     */
    private void extractEventInfo(String message, LogPrintStructure.Builder builder) {
        Matcher opMatcher = OPERATION_PATTERN.matcher(message);
        if (opMatcher.find()) {
            String operationType = opMatcher.group(1);
            builder.setOperationType(operationType);

            // Déterminer la catégorie de l'événement
            if (operationType.contains("READ")) {
                builder.setEventCategory("READ");
            } else if (operationType.contains("WRITE")) {
                builder.setEventCategory("WRITE");
            } else if (operationType.contains("EXPENSIVE")) {
                builder.setEventCategory("SEARCH");
            }
        }

        // Extraire la méthode appelée
        Matcher methodMatcher = METHOD_PATTERN.matcher(message);
        if (methodMatcher.find()) {
            builder.setMethodName(methodMatcher.group(1));
        }
    }

    /**
     * Extrait les informations sur l'utilisateur
     */
    private void extractUserInfo(String message, LogPrintStructure.Builder builder) {
        Matcher userMatcher = USER_ID_PATTERN.matcher(message);
        if (userMatcher.find()) {
            try {
                int userId = Integer.parseInt(userMatcher.group(1));
                builder.setUserId(userId);
            } catch (NumberFormatException e) {
                logger.debug("UserId invalide dans le message : {}", message);
            }
        }
    }

    /**
     * Extrait les informations sur l'action effectuée
     */
    private void extractActionInfo(String message, LogPrintStructure.Builder builder) {
        // Product ID
        Matcher productMatcher = PRODUCT_ID_PATTERN.matcher(message);
        if (productMatcher.find()) {
            try {
                int productId = Integer.parseInt(productMatcher.group(1));
                builder.setProductId(productId);
            } catch (NumberFormatException e) {
                logger.debug("ProductId invalide");
            }
        }

        // Price
        Matcher priceMatcher = PRICE_PATTERN.matcher(message);
        if (priceMatcher.find()) {
            try {
                double price = Double.parseDouble(priceMatcher.group(1));
                builder.setPrice(price);
            } catch (NumberFormatException e) {
                logger.debug("Price invalide");
            }
        }

        // Threshold pour les recherches de produits chers
        Matcher thresholdMatcher = THRESHOLD_PATTERN.matcher(message);
        if (thresholdMatcher.find()) {
            try {
                double threshold = Double.parseDouble(thresholdMatcher.group(1));
                builder.setThreshold(threshold);
            } catch (NumberFormatException e) {
                logger.debug("Threshold invalide");
            }
        }
    }

    /**
     * Méthode de test
     */
    public static void main(String[] args) {
        LogParser parser = new LogParser();

        try {
            List<LogPrintStructure> structures = parser.parseLogFile("logs/app.log");

            System.out.println("\n=== EXEMPLES DE LPS PARSÉES ===\n");

            // Afficher les 5 premières structures
            structures.stream()
                    .limit(5)
                    .forEach(lps -> {
                        System.out.println(lps.toFormattedString());
                        System.out.println("JSON: " + lps.toJSON());
                        System.out.println("---");
                    });

            // Statistiques
            System.out.println("\n=== STATISTIQUES ===");
            System.out.println("Total de logs parsés : " + structures.size());

            long readOps = structures.stream()
                    .filter(lps -> "READ".equals(lps.getEventCategory()))
                    .count();

            long writeOps = structures.stream()
                    .filter(lps -> "WRITE".equals(lps.getEventCategory()))
                    .count();

            long searchOps = structures.stream()
                    .filter(lps -> "SEARCH".equals(lps.getEventCategory()))
                    .count();

            System.out.println("READ operations: " + readOps);
            System.out.println("WRITE operations: " + writeOps);
            System.out.println("SEARCH operations: " + searchOps);

        } catch (IOException e) {
            logger.error("Erreur lors du parsing des logs", e);
            e.printStackTrace();
        }
    }
}

// comment executé :
/*
mvn exec:java -Dexec.mainClass="hai702.tp4.scenario.Scenario"

→ Crée logs/app.log avec les logs bruts


2️⃣ Parsing + Stockage des LPS (Question 5)

 mvn exec:java -Dexec.mainClass="hai702.tp4.profiling.LPSStorageSystem"

```

**Ce qui se passe :**

1. **Parse** `logs/app.log` → LPS (via `LogParser`)
2. **Sauvegarde** toutes les LPS en JSON
3. **Organise** les LPS par utilisateur
4. **Génère** un rapport textuel structuré
5. **Affiche** les LPS à l'écran (format structuré)
6. **Calcule** des statistiques


lps_output/
├── all_lps.json                    ← TOUTES les LPS en JSON
├── lps_report.txt                  ← Rapport textuel structuré
└── by_user/
    ├── user_1_lps.json             ← LPS de l'utilisateur 1
    ├── user_2_lps.json             ← LPS de l'utilisateur 2
    ├── user_3_lps.json
    ...
    └── user_10_lps.json
* */
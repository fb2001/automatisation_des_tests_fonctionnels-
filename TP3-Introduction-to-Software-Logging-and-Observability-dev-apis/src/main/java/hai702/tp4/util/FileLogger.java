package hai702.tp4.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLogger {

    private static final String LOG_FILE = "logs/app.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static void log(String level, String className, String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            String timestamp = LocalDateTime.now().format(formatter);
            // Format : [YYYY-MM-DD HH:MM:SS] LEVEL ClassName - Message
            String logLine = String.format("[%s] %s %s - %s%n", timestamp, level, className, message);
            writer.write(logLine);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du log : " + e.getMessage());
        }
    }

    public static void info(String className, String message) {
        log("INFO", className, message);
        System.out.println("[INFO] " + message);
    }

    public static void warn(String className, String message) {
        log("WARN", className, message);
        System.out.println("[WARN] " + message);
    }

    public static void error(String className, String message) {
        log("ERROR", className, message);
        System.err.println("[ERROR] " + message);
    }
}
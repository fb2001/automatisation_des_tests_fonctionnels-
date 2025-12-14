package hai702.tp4.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Configuration Selenium pour les tests frontend
 */
@Configuration
public class SeleniumConfig {

    /**
     * Configure et retourne un WebDriver Chrome
     * @Scope("cucumber-glue") assure qu'une seule instance est créée par scénario
     */
    @Bean
    @Scope("cucumber-glue")
    public WebDriver webDriver() {
        // WebDriverManager télécharge automatiquement chromedriver
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        // Mode headless (sans interface graphique) pour CI/CD
        // Commentez cette ligne si vous voulez voir le navigateur pendant les tests
        // options.addArguments("--headless");

        // Options recommandées pour la stabilité
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        return new ChromeDriver(options);
    }
}
package hai702.tp4.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Hooks exécutés avant et après chaque scénario Cucumber
 */
public class CucumberHooks {

    @Autowired
    private WebDriver driver;

    /**
     * Exécuté AVANT chaque scénario
     */
    @Before
    public void setUp() {
        // Initialisation si nécessaire
        System.out.println("🚀 Démarrage du scénario avec Selenium");
    }

    /**
     * Exécuté APRÈS chaque scénario
     * Prend un screenshot en cas d'échec
     */
    @After
    public void tearDown(Scenario scenario) {
        // Si le scénario a échoué, on prend un screenshot
        if (scenario.isFailed()) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Screenshot de l'échec");
            System.err.println(" Scénario échoué : " + scenario.getName());
        }

        // Ferme le navigateur après chaque scénario
        if (driver != null) {
            driver.quit();
            System.out.println(" Navigateur fermé");
        }
    }
}
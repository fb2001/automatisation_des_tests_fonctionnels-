package hai702.tp4.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import static org.slf4j.MDC.clear;

/**
 * Page Object pour LoginForm.tsx
 * Gère l'authentification et l'inscription
 */
public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Sélecteurs basés sur votre code React Material-UI
    // Les TextField MUI génèrent des inputs avec des labels
    private final By emailInput = By.xpath("//input[@type='email']");
    private final By passwordInput = By.xpath("//input[@type='password']");

    // Champs uniquement pour l'inscription
    // Utiliser un sélecteur plus robuste basé sur le label visible
    private final By idInput = By.xpath("//label[contains(text(), 'ID Utilisateur')]//following::input[1]");
    private final By nameInput = By.xpath("//label[contains(text(), 'Nom Complet')]//following::input[1]");
    private final By ageInput = By.xpath("//label[contains(text(), 'Âge')]//following::input[1]");

    // Boutons
    private final By loginButton = By.xpath("//button[contains(., 'Se connecter')]");
    private final By registerButton = By.xpath("//button[contains(., \"S'inscrire\")]");
    private final By switchModeButton = By.xpath("//button[contains(., 'Pas de compte') or contains(., 'Déjà un compte')]");

    // Messages
    private final By errorAlert = By.xpath("//div[contains(@class, 'MuiAlert-root') and contains(@class, 'MuiAlert-standardError')]");
    private final By successAlert = By.xpath("//div[contains(@class, 'MuiAlert-root') and contains(@class, 'MuiAlert-standardSuccess')]");

    // Après connexion - présence de la navbar avec le nom de l'utilisateur
    private final By userChip = By.xpath("//div[contains(@class, 'MuiChip-root')]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Navigue vers la page de login (page d'accueil)
     */
    public void navigateTo(String baseUrl) {
        driver.get(baseUrl);
        // Attendre que le formulaire soit chargé
        wait.until(ExpectedConditions.presenceOfElementLocated(emailInput));
    }

    /**
     * Remplit le formulaire de connexion
     */

    public void fillLoginForm(String email, String password) {
        WebElement emailField = driver.findElement(this.emailInput);
        WebElement passwordField = driver.findElement(this.passwordInput);

        // soit clear() classique
        emailField.clear();
        passwordField.clear();

        emailField.sendKeys(email);
        passwordField.sendKeys(password);
    }



    /**
     * Remplit le formulaire d'inscription complet
     */
    public void fillRegisterForm(String id, String name, String age, String email, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(idInput));

        clearAndSendKeys(driver.findElement(idInput), id);
        clearAndSendKeys(driver.findElement(nameInput), name);
        clearAndSendKeys(driver.findElement(ageInput), age);
        clearAndSendKeys(driver.findElement(emailInput), email);
        clearAndSendKeys(driver.findElement(passwordInput), password);
    }

    /**
     * Méthode utilitaire pour vider un champ de manière fiable avant d'écrire dedans.
     * @param element L'élément WebElement (input)
     * @param text Le texte à saisir
     */
    private void clearAndSendKeys(WebElement element, String text) {
        // Utiliser JavaScript pour vider le champ est plus fiable que element.clear()
        // ou les raccourcis clavier avec les frameworks JS modernes.
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", element);
        element.sendKeys(text);
    }

    /**
     * Clique sur "Se connecter"
     */
    public void clickLogin() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        button.click();
    }

    /**
     * Clique sur "S'inscrire"
     */
    public void clickRegister() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(registerButton));
        button.click();
    }

    /**
     * Clique sur le lien pour passer en mode inscription/connexion
     */
    public void switchToRegisterMode() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(switchModeButton));
        button.click();
        // Attendre que le formulaire se mette à jour
        wait.until(ExpectedConditions.presenceOfElementLocated(idInput));
    }

    public void switchToLoginMode() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(switchModeButton));
        button.click();
        // Attendre que le formulaire revienne en mode connexion
        wait.until(ExpectedConditions.invisibilityOfElementLocated(idInput));
    }

    /**
     * Vérifie si un message d'erreur est affiché
     */
    public boolean isErrorDisplayed() {
        try {
            WebElement alert = wait.until(ExpectedConditions.presenceOfElementLocated(errorAlert));
            return alert.isDisplayed() && alert.getText().contains("échouée");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Récupère le texte du message d'erreur
     */
    public String getErrorMessage() {
        try {
            return driver.findElement(errorAlert).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Vérifie si un message de succès est affiché
     */
    public boolean isSuccessDisplayed() {
        try {
            WebElement alert = wait.until(ExpectedConditions.presenceOfElementLocated(successAlert));
            return alert.isDisplayed() && alert.getText().contains("réussie");
        } catch (Exception e) {
            return false;
        }
    }

    //Attend que l'alerte de succès disparaisse (généralement après quelques secondes)

    public void waitForSuccessAlertToDisappear() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(successAlert));
    }

   // Vérifie si l'utilisateur est connecté (présence du Chip avec le nom)

    public boolean isUserLoggedIn() {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(userChip))
                    .isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Récupère le nom de l'utilisateur connecté depuis le Chip
    public String getLoggedInUserName() {
        try {
            WebElement chip = driver.findElement(userChip);
            return chip.getText();
        } catch (Exception e) {
            return "";
        }
    }
}
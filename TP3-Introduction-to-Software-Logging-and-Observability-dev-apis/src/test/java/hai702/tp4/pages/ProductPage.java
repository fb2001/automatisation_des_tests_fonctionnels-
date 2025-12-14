package hai702.tp4.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

/**
 * Page Object pour ProductList.tsx
 * Gère l'affichage et les opérations CRUD sur les produits
 */
public class ProductPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Tab "Produits" - sélecteur plus flexible
    private final By productsTab = By.xpath("//button[contains(@class, 'MuiTab-root') and contains(., 'Produits')]");

    // Bouton "Ajouter un produit" (probablement un FAB ou un bouton en haut)
    private final By addProductButton = By.xpath("//button[contains(., 'Nouveau')]");

    // Dialog/Modal d'ajout de produit (si vous utilisez un MUI Dialog)
    private final By productDialog = By.xpath("//div[@role='dialog']");

    // Champs du formulaire produit (dans le dialog ou inline)
    // Ces sélecteurs devront être adaptés selon votre implémentation exacte
    private final By productIdInput = By.xpath("//label[contains(text(), 'ID')]//following::input[1]");
    private final By productNameInput = By.xpath("//label[contains(text(), 'Nom')]//following::input[1]");
    private final By productPriceInput = By.xpath("//label[contains(text(), 'Prix')]//following::input[1]");
    private final By productExpirationInput = By.xpath("//label[contains(text(), 'Date Expiration')]//following::input[1]");

    // Boutons dans le dialog
    private final By saveButton = By.xpath(
            "//button[contains(., 'Ajouter') or contains(., 'Enregistrer') or contains(., 'Sauvegarder') or contains(., 'Save')]"
    );
    private final By cancelButton = By.xpath("//button[contains(., 'Annuler') or contains(., 'Cancel')]");

    // Liste des produits (MUI Cards)
    private final By productCards = By.xpath("//div[contains(@class, 'MuiDataGrid-row')]");

    // Boutons d'action sur un produit (Edit, Delete)
    private final By deleteButtons = By.xpath("//button[contains(@aria-label, 'delete') or .//svg[contains(@data-testid, 'DeleteIcon')]]");
    private final By editButtons = By.xpath("//button[contains(@aria-label, 'edit') or .//svg[contains(@data-testid, 'EditIcon')]]");

    // Bouton de déconnexion
    private final By logoutButton = By.xpath("//button[contains(., 'Déconnexion')]");

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Clique sur l'onglet "Produits"
     */
    public void clickProductsTab() {
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(productsTab));
        tab.click();
    }

    /**
     * Clique sur le bouton "Ajouter un produit"
     */
    public void clickAddProduct() {
        // Cliquer sur le bouton "Ajouter un produit"
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(addProductButton));
        button.click();

        // Attendre que le dialog soit visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(productDialog));
        System.out.println("✓ Dialog d'ajout de produit ouvert");
    }


    /**
     * Remplit le formulaire d'ajout de produit
     */

    public void fillProductForm(String id, String name, String price, String expirationDate) {
        // Attendre que le dialog soit présent
        wait.until(ExpectedConditions.visibilityOfElementLocated(productDialog));

        // ID
        WebElement idField = wait.until(ExpectedConditions.visibilityOfElementLocated(productIdInput));
        idField.clear();
        idField.sendKeys(id);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                idField
        );

        // Nom
        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(productNameInput));
        nameField.clear();
        nameField.sendKeys(name);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                nameField
        );

        // Prix
        WebElement priceField = wait.until(ExpectedConditions.visibilityOfElementLocated(productPriceInput));
        priceField.clear();
        priceField.sendKeys(price);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                priceField
        );

        // Date Expiration (date picker React)
        WebElement dateField = wait.until(ExpectedConditions.visibilityOfElementLocated(productExpirationInput));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = arguments[1];" +
                        "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                dateField,
                expirationDate
        );

        // Attente courte pour que React mette à jour le state
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Vérification console
        System.out.println("✓ ID rempli: " + idField.getAttribute("value"));
        System.out.println("✓ Nom rempli: " + nameField.getAttribute("value"));
        System.out.println("✓ Prix rempli: " + priceField.getAttribute("value"));
        System.out.println("✓ Date remplie: " + dateField.getAttribute("value"));
    }



    /**
     * Clique sur le bouton de sauvegarde
     */
    public void clickSave() {
        WebElement saveBtn = wait.until(ExpectedConditions.presenceOfElementLocated(saveButton));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                saveBtn
        );

        wait.until(ExpectedConditions.elementToBeClickable(saveBtn));

        try {
            saveBtn.click();
        } catch (Exception e) {
            // fallback JS click
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
        }

        System.out.println("✓ Bouton Enregistrer cliqué");
        try {
            Thread.sleep(1500); // attendre que le produit soit ajouté
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Clique sur le bouton Annuler
     */
    public void clickCancel() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(cancelButton));
        button.click();
    }

    /**
     * Retourne le nombre de produits affichés
     */
    public int getProductCount() {
        try {
            // Attendre que le DataGrid charge
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(productCards));
            List<WebElement> products = driver.findElements(productCards);
            return products.size();
        } catch (Exception e) {
            System.out.println("⚠ Aucun produit trouvé dans le DataGrid");
            return 0;
        }
    }

    //Vérifie si un produit avec un nom donné existe
    public boolean productExists(String productName) {
        try {
            // Chercher dans les cellules du DataGrid
            By productLocator = By.xpath("//div[contains(@class, 'MuiDataGrid-cell') and contains(text(), '" + productName + "')]");
            wait.until(ExpectedConditions.presenceOfElementLocated(productLocator));
            return driver.findElements(productLocator).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }


    //Supprime le premier produit de la liste
    public void deleteFirstProduct() {
        List<WebElement> deleteButtonsList = driver.findElements(deleteButtons);
        if (!deleteButtonsList.isEmpty()) {
            deleteButtonsList.get(0).click();

            // Si une dialog de confirmation apparaît, confirmer
            try {
                By confirmButton = By.xpath("//button[contains(., 'Confirmer') or contains(., 'Oui') or contains(., 'Supprimer')]");
                WebElement confirm = wait.until(ExpectedConditions.elementToBeClickable(confirmButton));
                confirm.click();
            } catch (Exception e) {
                // Pas de confirmation, la suppression est directe
            }

            // Attendre que la liste se mette à jour
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void logout() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(logoutButton));
        button.click();
    }
}
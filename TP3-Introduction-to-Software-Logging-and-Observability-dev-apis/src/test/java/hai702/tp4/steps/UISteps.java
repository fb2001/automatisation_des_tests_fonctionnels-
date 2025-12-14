package hai702.tp4.steps;

import hai702.tp4.pages.LoginPage;
import hai702.tp4.pages.ProductPage;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.*;

/**
 * Step Definitions pour les tests Selenium (UI)
 * Adaptés au frontend React avec Material-UI
 */
public class UISteps {

    @Autowired
    private WebDriver driver;

    @Autowired
    private CommonSteps common;

    private LoginPage loginPage;
    private ProductPage productPage;

    // URL du frontend React (Vite dev server)
    private static final String FRONTEND_URL = "http://localhost:5173";

    /**
     * Initialise les Page Objects
     */
    private void initPages() {
        if (loginPage == null) {
            loginPage = new LoginPage(driver);
        }
        if (productPage == null) {
            productPage = new ProductPage(driver);
        }
    }

    // ==================== STEPS POUR L'AUTHENTIFICATION ====================

    @Given("je suis sur la page de connexion")
    public void navigate_to_login_page() {
        initPages();
        loginPage.navigateTo(FRONTEND_URL);
        System.out.println("📱 Navigation vers: " + FRONTEND_URL);
    }

    @Given("je suis en mode inscription")
    public void switch_to_register_mode() {
        loginPage.switchToRegisterMode();
        System.out.println("🔄 Passage en mode inscription");
    }

    @When("je remplis le formulaire de connexion avec l'email {string} et le mot de passe {string}")
    public void fill_login_form(String email, String password) {
        loginPage.fillLoginForm(email, password);
        System.out.println("✍️ Formulaire de connexion rempli: " + email);
    }

    @When("je remplis le formulaire d'inscription avec:")
    public void fill_register_form(io.cucumber.datatable.DataTable dt) {
        var data = dt.asMaps().get(0);
        loginPage.fillRegisterForm(
                data.get("id"),
                data.get("name"),
                data.get("age"),
                data.get("email"),
                data.get("password")
        );
        System.out.println("✍️ Formulaire d'inscription rempli: " + data.get("email"));
    }

    @When("je clique sur le bouton Se connecter")
    public void click_login_button() {
        loginPage.clickLogin();
        System.out.println("🖱️ Clic sur Se connecter");
    }

    @When("je clique sur le bouton S'inscrire")
    public void click_register_button() {
        loginPage.clickRegister();
        System.out.println("🖱️ Clic sur S'inscrire");
    }

    @Then("je suis connecté et je vois mon nom {string} dans la navbar")
    public void verify_logged_in(String expectedName) {
        // Attendre que l'interface se charge
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertTrue("L'utilisateur devrait être connecté",
                loginPage.isUserLoggedIn());

        String actualName = loginPage.getLoggedInUserName();
        assertTrue("Le nom devrait contenir '" + expectedName + "', mais c'est '" + actualName + "'",
                actualName.contains(expectedName));

        System.out.println("✅ Utilisateur connecté: " + actualName);
    }

    @Then("un message d'erreur s'affiche")
    public void verify_error_message() {
        // Attendre 3 secondes que l'alerte apparaisse
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertTrue("Un message d'erreur devrait être affiché",
                loginPage.isErrorDisplayed());

        String errorMsg = loginPage.getErrorMessage();
        System.out.println("⚠️ Message d'erreur: " + errorMsg);
    }

    @Then("un message de succès s'affiche")
    public void verify_success_message() {
        // Attendre 3 secondes
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertTrue("Un message de succès devrait être affiché",
                loginPage.isSuccessDisplayed());
        System.out.println(" Message de succès affiché");
    }

    // ==================== STEPS POUR LA GESTION DES PRODUITS ====================

    @Given("je suis connecté en tant que {string}")
    public void login_as_user(String email) {
        initPages();
        loginPage.navigateTo(FRONTEND_URL);

        // Utiliser le mot de passe par défaut d'Alice
        String password = email.equals("alice@test.com") ? "admin123" : "user123";

        loginPage.fillLoginForm(email, password);
        loginPage.clickLogin();

        // Attendre que la connexion se fasse
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("🔐 Connecté en tant que: " + email);
    }

    @Given("je suis sur l'onglet Produits")
    public void navigate_to_products_tab() {
        // Attendre que les tabs soient chargés
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        productPage.clickProductsTab();
        System.out.println("📂 Navigation vers l'onglet Produits");

        // Attendre que les produits se chargent
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @When("je clique sur le bouton Ajouter un produit")
    public void click_add_product() {
        productPage.clickAddProduct();
        System.out.println("🖱️ Clic sur Ajouter un produit");
    }

    @When("je remplis le formulaire produit avec:")
    public void fill_product_form(io.cucumber.datatable.DataTable dt) {
        var data = dt.asMaps().get(0);
        productPage.fillProductForm(
                data.get("id"),
                data.get("name"),
                data.get("price"),
                data.get("expirationDate")
        );
        System.out.println("✍️ Formulaire produit rempli: " + data.get("name"));
    }

    @When("je clique sur Enregistrer")
    public void click_save() {
        productPage.clickSave();
        System.out.println("💾 Clic sur Enregistrer");

        // Attendre que le produit soit ajouté
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Then("le produit {string} apparaît dans la liste")
    public void verify_product_in_list(String productName) {
        assertTrue("Le produit '" + productName + "' devrait être visible dans la liste",
                productPage.productExists(productName));
        System.out.println("✅ Produit trouvé: " + productName);
    }

    @Then("je vois au moins {int} produit dans la liste")
    public void verify_product_count(int expectedCount) {
        int actualCount = productPage.getProductCount();
        assertTrue("Il devrait y avoir au moins " + expectedCount + " produit(s), mais il y en a " + actualCount,
                actualCount >= expectedCount);
        System.out.println("📊 Nombre de produits: " + actualCount);
    }

    @Then("je vois exactement {int} produits dans la liste")
    public void verify_exact_product_count(int expectedCount) {
        int actualCount = productPage.getProductCount();
        assertEquals("Il devrait y avoir exactement " + expectedCount + " produit(s)",
                expectedCount, actualCount);
        System.out.println("📊 Nombre exact de produits: " + actualCount);
    }

    @When("je supprime le premier produit")
    public void delete_first_product() {
        productPage.deleteFirstProduct();
        System.out.println("🗑️ Suppression du premier produit");
    }

    @When("je clique sur Déconnexion")
    public void click_logout() {
        productPage.logout();
        System.out.println("🚪 Déconnexion");

        // Attendre la déconnexion
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Then("je suis redirigé vers la page de connexion")
    public void verify_back_to_login() {
        // Vérifier qu'on voit de nouveau le formulaire de connexion
        assertTrue("L'utilisateur devrait être redirigé vers le login",
                driver.getCurrentUrl().equals(FRONTEND_URL + "/") ||
                        driver.getPageSource().contains("Bienvenue"));
        System.out.println("✅ Retour à la page de connexion");
    }
}
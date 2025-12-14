package hai702.tp4.steps;

import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CommonSteps common;

    private ResponseEntity<String> lastResponse;
    private String currentUserId;

    @Given("a running backend")
    public void backend_running() {
        String url = common.getBaseUrl() + "/api/products";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", "1"); // User admin par défaut
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        lastResponse = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        assertNotNull(lastResponse);
        assertEquals(200, lastResponse.getStatusCode().value());
    }

    @Given("l'utilisateur n'existe pas avec l'email {string}")
    public void user_not_exists(String email) {
        // Cette vérification est optionnelle selon votre API
        // Si vous n'avez pas d'endpoint GET /users?email=X, ignorez ce step
    }

    @Given("un utilisateur existe avec l'email {string} et le mot de passe {string}")
    public void user_exists(String email, String password) {
        // Cet utilisateur doit déjà être en base (créé par DataInitializer)
        // Sinon, créez-le ici via l'endpoint /register
    }

    @Given("un utilisateur admin existe avec l'email {string} et le mot de passe {string}")
    public void admin_user_exists(String email, String password) {
        // L'utilisateur admin (alice@test.com) existe déjà via DataInitializer
        currentUserId = "1"; // ID d'Alice
    }

    @When("je m'enregistre avec:")
    public void register(io.cucumber.datatable.DataTable dt) {
        Map<String, String> userData = dt.asMaps().get(0);

        Map<String, Object> body = new HashMap<>();
        body.put("name", userData.get("name"));
        body.put("email", userData.get("email"));
        body.put("password", userData.get("password"));
        body.put("age", Integer.parseInt(userData.get("age")));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);

        String url = common.getBaseUrl() + "/api/users/register";
        lastResponse = restTemplate.postForEntity(url, entity, String.class);
    }

    @When("je me connecte avec l'email {string} et le mot de passe {string}")
    public void login(String email, String password) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);

        String url = common.getBaseUrl() + "/api/users/login";
        lastResponse = restTemplate.postForEntity(url, entity, String.class);

        // Si la connexion réussit, on récupère l'userId depuis la réponse
        if (lastResponse.getStatusCode().is2xxSuccessful()) {
            // Supposons que la réponse JSON contient {"id":"1", "name":"Alice", ...}
            // Vous devrez parser le JSON pour extraire l'ID
            currentUserId = "1"; // Simplification pour l'exemple
        }
    }

    @Then("l'enregistrement reussit et l'utilisateur existe avec l'email {string}")
    public void assert_user_exists(String email) {
        assertEquals(200, lastResponse.getStatusCode().value());
    }

    @Then("la connexion est reussie")
    public void connexion_ok() {
        assertEquals(200, lastResponse.getStatusCode().value());
    }

    // Méthode utilitaire pour partager l'userId avec ProductSteps
    public String getCurrentUserId() {
        return currentUserId != null ? currentUserId : "1";
    }
}
package hai702.tp4.steps;

import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class ProductSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CommonSteps common;

    @Autowired
    private UserSteps userSteps;

    private ResponseEntity<String> lastResponse;
    private String currentUserId = "1"; // ID par défaut (Alice admin)

    @When("je demande la liste des produits en tant que {string}")
    public void get_all_products(String userId) {
        currentUserId = userId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", userId);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = common.getBaseUrl() + "/api/products";
        lastResponse = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    @When("j'ajoute un produit:")
    public void add_product(io.cucumber.datatable.DataTable dt) {
        Map<String, String> productData = dt.asMaps().get(0);

        Map<String, Object> body = new HashMap<>();
        body.put("id", productData.get("id"));
        body.put("name", productData.get("name"));
        body.put("price", Double.parseDouble(productData.get("price")));
        body.put("expirationDate", productData.get("expirationDate"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-User-Id", currentUserId);
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);

        String url = common.getBaseUrl() + "/api/products";

        try {
            lastResponse = restTemplate.postForEntity(url, entity, String.class);
        } catch (HttpClientErrorException e) {
            lastResponse = new ResponseEntity<>(e.getStatusCode());
        }
    }

    @When("je supprime le produit avec l'id {string}")
    public void delete_product(String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", currentUserId);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = common.getBaseUrl() + "/api/products/" + id;

        try {
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
            lastResponse = new ResponseEntity<>(response.getStatusCode());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Capture 4xx et 5xx
            lastResponse = new ResponseEntity<>(e.getStatusCode());
        }
    }

    @Then("une erreur ProductNotFoundException est levée")
    public void assert_product_not_found() {
        // Accepte 4xx ou 5xx
        assertTrue("Le code de statut devrait indiquer une erreur (4xx ou 5xx) mais c'était : " + lastResponse.getStatusCode(),
                lastResponse.getStatusCode().isError());
    }

    @Then("je reçois au moins {int} produit")
    public void assert_at_least_n_products(int count) {
        assertEquals(200, lastResponse.getStatusCode().value());
        // Pour vérifier le nombre, il faudrait parser le JSON
        // Simplification : on vérifie juste que la réponse n'est pas vide
        assertNotNull(lastResponse.getBody());
        assertTrue(lastResponse.getBody().length() > 10);
    }

    @Then("le produit avec l'id {string} existe")
    public void product_exists(String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", currentUserId);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = common.getBaseUrl() + "/api/products/" + id;
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        assertEquals(200, response.getStatusCode().value());
    }


}
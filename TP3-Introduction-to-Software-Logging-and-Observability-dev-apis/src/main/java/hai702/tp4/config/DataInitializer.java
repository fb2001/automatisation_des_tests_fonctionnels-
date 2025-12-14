package hai702.tp4.config;

import hai702.tp4.model.Product;
import hai702.tp4.model.User;
import hai702.tp4.repository.ProductRepository;
import hai702.tp4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // On ajoute MongoTemplate pour avoir accès aux commandes de bas niveau (drop)
    private final MongoTemplate mongoTemplate;

    @Autowired // Injection explicite
    public DataInitializer(ProductRepository productRepository,
                           UserRepository userRepository,
                           MongoTemplate mongoTemplate) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== 🧹 NETTOYAGE COMPLET DE LA BDD (Hard Reset) ===");

        // Cette commande détruit complètement les collections et leurs index
        // C'est plus radical et efficace que deleteAll()
        mongoTemplate.dropCollection("users");
        mongoTemplate.dropCollection("products");

        System.out.println("✅ Collections supprimées.");

        System.out.println("=== 🌱 REMPLISSAGE INITIAL ===");

        // --- Création Produits ---
        Product p1 = new Product();
        p1.setId("1"); p1.setName("Laptop Gaming"); p1.setPrice(1200.00); p1.setExpirationDate("2028-01-01");

        Product p2 = new Product();
        p2.setId("2"); p2.setName("Souris sans fil"); p2.setPrice(25.50); p2.setExpirationDate("2026-06-15");

        productRepository.saveAll(Arrays.asList(p1, p2));

        // --- Création Utilisateurs ---
        User u1 = new User();
        u1.setId("1");
        u1.setName("Alice Admin");
        u1.setEmail("alice@test.com");
        u1.setPassword("admin123");
        u1.setAge(30);

        User u2 = new User();
        u2.setId("2");
        u2.setName("Bob User");
        u2.setEmail("bob@test.com");
        u2.setPassword("user123");
        u2.setAge(22);

        userRepository.saveAll(Arrays.asList(u1, u2));

        System.out.println("✅ Base repeuplée avec succès.");
    }
}
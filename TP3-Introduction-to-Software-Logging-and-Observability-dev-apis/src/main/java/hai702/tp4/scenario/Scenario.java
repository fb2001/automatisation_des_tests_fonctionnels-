package hai702.tp4.scenario;

import hai702.tp4.model.Product;
import hai702.tp4.model.User;
import hai702.tp4.service.ProductService;
import hai702.tp4.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Random;

//@Component
//@Order(2)
public class Scenario implements CommandLineRunner {

    private final UserService userService;
    private final ProductService productService;
    private final Random random = new Random();

    public Scenario(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== 🎬 DÉBUT SCÉNARIOS AUTOMATISÉS (Logging) ===");

        // Création de 10 utilisateurs
        for (int u = 1; u <= 10; u++) {

            String email = "user" + u + "@mail.com";
            User user = new User();

            user.setId(String.valueOf(u));

            user.setName("User" + u);
            user.setEmail(email);
            user.setPassword("pass" + u);
            user.setAge(20 + random.nextInt(20));


            try {
                userService.register(user);

            } catch (Exception e) {
                // Ignorer si existe déjà
            }

            if (userService.login(user.getEmail(), user.getPassword())) {
                User loggedUser = userService.findByEmail(email);

                String currentUserId = loggedUser.getId();

                // Chaque utilisateur fait 20 actions
                for (int i = 0; i < 20; i++) {
                    int action = random.nextInt(6) + 1;

                    try {
                        switch (action) {
                            case 1: // READ all
                                productService.findAll(currentUserId);
                                break;

                            case 2: // READ by ID
                                // Conversion de l'ID aléatoire en String
                                String randomReadId = String.valueOf(random.nextInt(10) + 1);
                                productService.getProductById(randomReadId, currentUserId);
                                break;

                            case 3: // ADD product
                                Product p = new Product();
                                // Génération d'un ID String (ex: "504")
                                p.setId(String.valueOf(random.nextInt(1000) + 100));
                                p.setName("Prod" + random.nextInt(100));
                                p.setPrice(random.nextDouble() * 500);
                                p.setExpirationDate("2026-12-31");

                                productService.addProduct(p, currentUserId);
                                break;

                            case 4: // DELETE product
                                String randomDeleteId = String.valueOf(random.nextInt(10) + 1);
                                productService.deleteProduct(randomDeleteId, currentUserId);
                                break;

                            case 5: // UPDATE product
                                Product pUpdate = new Product();
                                // ID String entre "1" et "10" (produits de base)
                                pUpdate.setId(String.valueOf(random.nextInt(10) + 1));
                                pUpdate.setName("Updated" + random.nextInt(100));
                                pUpdate.setPrice(random.nextDouble() * 400);
                                pUpdate.setExpirationDate("2027-01-01");

                                productService.updateProduct(pUpdate, currentUserId);
                                break;

                            case 6: // EXPENSIVE search
                                productService.findExpensiveProducts(100 + random.nextDouble() * 300, currentUserId);
                                break;
                        }
                    } catch (Exception e) {
                        // On continue malgré les erreurs fonctionnelles (Not Found, etc.)
                    }
                }
            }
        }

        System.out.println("=== ✅ SCÉNARIOS TERMINÉS — Serveur Web toujours actif ===");
    }
}
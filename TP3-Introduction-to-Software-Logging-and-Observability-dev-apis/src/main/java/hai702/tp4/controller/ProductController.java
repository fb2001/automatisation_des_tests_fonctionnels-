package hai702.tp4.controller;

import hai702.tp4.exception.DuplicateProductException;
import hai702.tp4.exception.ProductNotFoundException;
import hai702.tp4.model.Product;
import hai702.tp4.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //todo : les endpoints sont accessibles par tout type d'utilisateur, il faut restreindre si besoin !
    // Par exemple, seuls les admins peuvent ajouter, supprimer ou modifier des produits.
    // Amelioer la gestion des erreurs (retourner des codes HTTP appropriés)

    @GetMapping
    public List<Product> getAllProducts(@RequestHeader("X-User-Id") String userId) {
        return productService.findAll(userId);
    }

    @GetMapping("/{id}")
    public Product getProductById(@RequestHeader("X-User-Id") String userId, @PathVariable String id) throws ProductNotFoundException {
        return productService.getProductById(id, userId);
    }

    @PostMapping
    public void addProduct(@RequestHeader("X-User-Id") String userId , @RequestBody Product product) throws DuplicateProductException {
        productService.addProduct(product, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@RequestHeader("X-User-Id") String userId, @PathVariable String id) throws ProductNotFoundException {
        productService.deleteProduct(id, userId);
    }

    @PutMapping("/{id}")
    public void updateProduct(@RequestHeader("X-User-Id") String userId, @RequestBody Product product) throws ProductNotFoundException {
        productService.updateProduct(product, userId);
    }

    @GetMapping("/expensive")
    public List<Product> getExpensiveProducts(@RequestHeader("X-User-Id") String userId, @RequestParam double threshold) {
        return productService.findExpensiveProducts(threshold, userId);
    }
}
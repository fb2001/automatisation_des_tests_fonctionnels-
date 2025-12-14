package hai702.tp4.service;

import hai702.tp4.exception.DuplicateProductException;
import hai702.tp4.exception.ProductNotFoundException;
import hai702.tp4.model.Product;
import hai702.tp4.repository.ProductRepository;

import java.util.List;

import hai702.tp4.util.FileLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    // On utilise SLF4J pour les logs internes du Service
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private static final String CLASS_NAME = "ProductService";

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }


    public void addProduct(Product p, String userId) throws DuplicateProductException {
        if (repo.existsById(p.getId())) {

            FileLogger.error(CLASS_NAME,
                    "WRITE_OPERATION: addProduct failed | userId=" + userId +
                            " | productId=" + p.getId());

            throw new DuplicateProductException("Product already exists");
        }

        repo.save(p);

        FileLogger.info(CLASS_NAME,
                "WRITE_OPERATION: addProduct | userId=" + userId +
                        " | productId=" + p.getId() + " | price=" + p.getPrice());
    }

    public Product getProductById(String id, String userId) throws ProductNotFoundException {

        FileLogger.info(CLASS_NAME,
                "READ_OPERATION: getProductById | userId=" + userId +
                        " | productId=" + id);

        Product product = repo.findById(id)
                .orElseThrow(() -> {
                    FileLogger.warn(CLASS_NAME, "Product not found: ID=" + id);
                    return new ProductNotFoundException("Product not found");
                });

        if (product.getPrice() > 50) {
            FileLogger.info(CLASS_NAME,
                    "EXPENSIVE_PRODUCT_SEARCH: userId=" + userId +
                            " | productId=" + id + " | price=" + product.getPrice());
        }

        return product;
    }

    public void updateProduct(Product p, String userId) throws ProductNotFoundException {
        if (!repo.existsById(p.getId())) {
            FileLogger.error(CLASS_NAME,
                    "WRITE_OPERATION: updateProduct failed | userId=" + userId +
                            " | productId=" + p.getId());
            throw new ProductNotFoundException("Product not found");
        }

        repo.save(p);

        FileLogger.info(CLASS_NAME,
                "WRITE_OPERATION: updateProduct | userId=" + userId +
                        " | productId=" + p.getId() + " | price=" + p.getPrice());
    }

    public void deleteProduct(String id, String userId) throws ProductNotFoundException {
        if (!repo.existsById(id)) {
            FileLogger.warn(CLASS_NAME, "Cannot delete missing product ID=" + id);
            throw new ProductNotFoundException("Product not found");
        }

        repo.deleteById(id);

        FileLogger.info(CLASS_NAME,
                "WRITE_OPERATION: deleteProduct | userId=" + userId +
                        " | productId=" + id);
    }

    public List<Product> findAll(String userId) {
        FileLogger.info(CLASS_NAME,
                "READ_OPERATION: findAll | userId=" + userId);
        return repo.findAll();
    }

    public List<Product> findExpensiveProducts(double threshold, String userId) {
        FileLogger.info(CLASS_NAME,
                "EXPENSIVE_PRODUCT_SEARCH: findExpensiveProducts | userId=" + userId +
                        " | threshold=" + threshold);
        return repo.findByPriceGreaterThanOrderByPriceDesc(threshold);
    }
}
package hai702.tp4.service;
public class ProductService {
    // private static final Logger logger = Logger.getLogger(ProductService.class.getName());
    // changment avec slf4j
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(hai702.tp4.service.ProductService.class);

    private hai702.tp4.repository.ProductRepository repo = new hai702.tp4.repository.ProductRepository();

    public void addProduct(hai702.tp4.model.Product p) throws hai702.tp4.exception.DuplicateProductException {
        org.slf4j.LoggerFactory.getLogger(hai702.tp4.service.ProductService.class).info("ACTION: addProduct | p={}", p);
        if (repo.exists(p.getId())) {
            // logger.severe("Duplicate product ID=" + p.getId());
            // changement avec slf4j
            // logger.error("Duplicate product ID={}", p.getId()); 1er test qst 2
            hai702.tp4.service.ProductService.logger.error("WRITE_OPERATION: addProduct failed | userId={} | productId={}", org.slf4j.MDC.get("userId"), p.getId());
            throw new hai702.tp4.exception.DuplicateProductException("Product already exists");
        }
        repo.save(p);
        // logger.info("Product added: " + p);
        // changement avec slf4j
        // logger.info("Product added: {}", p); 1 er test
        hai702.tp4.service.ProductService.logger.info("WRITE_OPERATION: addProduct | userId={} | productId={} | price={}", org.slf4j.MDC.get("userId"), p.getId(), p.getPrice());
    }

    public hai702.tp4.model.Product getProductById(int id) throws hai702.tp4.exception.ProductNotFoundException {
        org.slf4j.LoggerFactory.getLogger(hai702.tp4.service.ProductService.class).info("ACTION: getProductById | id={}", id);
        hai702.tp4.service.ProductService.logger.info("READ_OPERATION: getProductById | userId={} | productId={}", org.slf4j.MDC.get("userId"), id);
        hai702.tp4.model.Product p = repo.findById(id);
        if (p == null) {
            // logger.warning("Product not found: ID=" + id);
            // changement avec slf4j
            hai702.tp4.service.ProductService.logger.warn("Product not found: ID={}", id);
            throw new hai702.tp4.exception.ProductNotFoundException("Product not found");
        }
        // Log si c'est un produit cher
        if (p.getPrice() > 50) {
            hai702.tp4.service.ProductService.logger.info("EXPENSIVE_PRODUCT_SEARCH: userId={} | productId={} | price={}", org.slf4j.MDC.get("userId"), id, p.getPrice());
        }
        return p;
    }

    public void updateProduct(hai702.tp4.model.Product p) throws hai702.tp4.exception.ProductNotFoundException {
        org.slf4j.LoggerFactory.getLogger(hai702.tp4.service.ProductService.class).info("ACTION: updateProduct | p={}", p);
        if (!repo.exists(p.getId())) {
            // logger.severe("Cannot update missing product ID=" + p.getId());
            // changement avec slf4j
            // logger.error("Cannot update missing product ID={}", p.getId()); 1 er test
            hai702.tp4.service.ProductService.logger.error("WRITE_OPERATION: updateProduct failed | userId={} | productId={}", org.slf4j.MDC.get("userId"), p.getId());
            throw new hai702.tp4.exception.ProductNotFoundException("Product not found");
        }
        repo.update(p);
        // logger.info("Product updated: " + p);
        // changement avec slf4j
        // logger.info("Product updated: {}", p);
        hai702.tp4.service.ProductService.logger.info("WRITE_OPERATION: updateProduct | userId={} | productId={} | price={}", org.slf4j.MDC.get("userId"), p.getId(), p.getPrice());
    }

    public void deleteProduct(int id) throws hai702.tp4.exception.ProductNotFoundException {
        org.slf4j.LoggerFactory.getLogger(hai702.tp4.service.ProductService.class).info("ACTION: deleteProduct | id={}", id);
        if (!repo.exists(id)) {
            // logger.warning("Cannot delete missing product ID=" + id);
            // changement avec slf4j
            hai702.tp4.service.ProductService.logger.warn("Cannot delete missing product ID={}", id);
            throw new hai702.tp4.exception.ProductNotFoundException("Product not found");
        }
        repo.delete(id);
        // logger.info("Product deleted ID=" + id);
        // changement avec slf4j
        // logger.info("Product deleted ID={}", id); 1 er test
        hai702.tp4.service.ProductService.logger.info("WRITE_OPERATION: deleteProduct | userId={} | productId={}", org.slf4j.MDC.get("userId"), id);
    }

    public java.util.List<hai702.tp4.model.Product> findAll() {
        org.slf4j.LoggerFactory.getLogger(hai702.tp4.service.ProductService.class).info("ACTION: findAll");
        hai702.tp4.service.ProductService.logger.info("READ_OPERATION: findAll | userId={}", org.slf4j.MDC.get("userId"));
        return repo.findAll();
    }

    public java.util.List<hai702.tp4.model.Product> findExpensiveProducts(double threshold) {
        org.slf4j.LoggerFactory.getLogger(hai702.tp4.service.ProductService.class).info("ACTION: findExpensiveProducts | threshold={}", threshold);
        hai702.tp4.service.ProductService.logger.info("EXPENSIVE_PRODUCT_SEARCH: findExpensiveProducts | userId={} | threshold={}", org.slf4j.MDC.get("userId"), threshold);
        return repo.findAll().stream().filter(p -> p.getPrice() > threshold).sorted(java.util.Comparator.comparingDouble(hai702.tp4.model.Product::getPrice).reversed()).toList();
    }
}
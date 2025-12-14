package hai702.tp4.repository;

import hai702.tp4.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByPriceGreaterThanOrderByPriceDesc(double threshold);
}

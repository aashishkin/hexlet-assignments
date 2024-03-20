package exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import exercise.model.Product;

import org.springframework.data.domain.Sort;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // BEGIN
    List<Product> findByPriceBetween(int minPrice, int maxPrice, Sort sort);
    List<Product> findByPriceLessThanEqual(int maxPrice, Sort sort);
    List<Product> findByPriceGreaterThanEqual(int minPrice, Sort sort);
    // END
}

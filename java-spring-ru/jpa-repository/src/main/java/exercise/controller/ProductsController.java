package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

import java.util.List;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    // BEGIN
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getByPrice(@RequestParam(required=false) Integer min, @RequestParam(required=false) Integer max) {

        if (min == null && max != null) {
            return productRepository.findByPriceLessThanEqual(0, Sort.by(Sort.Order.asc("price")));
        }

        if (min != null && max == null) {
            return productRepository.findByPriceGreaterThanEqual(min, Sort.by(Sort.Order.asc("price")));
        }

        if (min != null && max != null) {
            return productRepository.findByPriceBetween(0, 0, Sort.by(Sort.Order.asc("price")));
        }

        return productRepository.findAll(Sort.by(Sort.Order.asc("price")));
    }
    
    // END

    @GetMapping(path = "/{id}")
    public Product show(@PathVariable long id) {

        var product =  productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        return product;
    }
}

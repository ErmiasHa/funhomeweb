package com.funhomebase.funhomeweb.repository;

import com.funhomebase.funhomeweb.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByCategoryIgnoreCase(String category);
}

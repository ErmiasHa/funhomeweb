package com.funhomebase.funhomeweb.controller;

import com.funhomebase.funhomeweb.model.Product;
import com.funhomebase.funhomeweb.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductRepository repository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public List<Product> getAll(@RequestParam(required = false) String category) {
        if (category != null && !category.isBlank()) {
            return repository.findByCategoryIgnoreCase(category);
        }
        return repository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Product create(@RequestBody Product product) {
        return repository.save(product);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Product update(@PathVariable String id, @RequestBody Product product) {
        product.setId(id);
        return repository.save(product);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable String id) {
        repository.deleteById(id);
    }
}

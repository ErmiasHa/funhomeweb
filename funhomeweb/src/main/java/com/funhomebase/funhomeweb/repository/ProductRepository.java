package com.funhomebase.funhomeweb.repository;

import com.funhomebase.funhomeweb.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

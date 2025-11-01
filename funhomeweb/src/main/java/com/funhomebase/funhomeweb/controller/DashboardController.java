package com.funhomebase.funhomeweb.controller;

import com.funhomebase.funhomeweb.model.Product;
import com.funhomebase.funhomeweb.model.User;
import com.funhomebase.funhomeweb.repository.ProductRepository;
import com.funhomebase.funhomeweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> getDashboardData() {
        Map<String, Object> stats = new HashMap<>();

        List<Product> products = productRepository.findAll();
        List<User> users = userRepository.findAll();

        double totalValue = products.stream().mapToDouble(Product::getPrice).sum();
        double avgPrice = products.isEmpty() ? 0 : totalValue / products.size();

        stats.put("productCount", products.size());
        stats.put("totalValue", totalValue);
        stats.put("avgPrice", avgPrice);
        stats.put("userCount", users.size());
        stats.put("productNames", products.stream().map(Product::getName).toArray());
        stats.put("productPrices", products.stream().mapToDouble(Product::getPrice).toArray());

        return stats;
    }
}

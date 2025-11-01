package com.funhomebase.funhomeweb.repository;

import com.funhomebase.funhomeweb.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByUserEmail(String userEmail);
}

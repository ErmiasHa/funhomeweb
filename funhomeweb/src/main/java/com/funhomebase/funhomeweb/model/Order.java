package com.funhomebase.funhomeweb.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "orders")
public class Order {

    @Id
    private String id;
    private String userEmail;
    private List<OrderItem> items;
    private double totalPrice;
    private String status = "Pending";
    private LocalDateTime createdAt = LocalDateTime.now();
}

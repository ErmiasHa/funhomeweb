package com.funhomebase.funhomeweb.controller;

import com.funhomebase.funhomeweb.model.Order;
import com.funhomebase.funhomeweb.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // --- Skapa ny order ---
    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        double total = order.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        order.setTotalPrice(total);
        order.setStatus("Pending");
        order.setCreatedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        // 🔔 Realtidsnotifiering (WebSocket)
        messagingTemplate.convertAndSend("/topic/orders", savedOrder);

        return savedOrder;
    }

    // 👤 Hämta kundens ordrar
    @GetMapping("/user/{email}")
    public List<Order> getUserOrders(@PathVariable String email) {
        return orderRepository.findByUserEmail(email);
    }

    // 🧑‍💼 Admin: Hämta alla ordrar
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // --- Uppdatera orderstatus ---
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable String id, @RequestParam String status) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isEmpty()) return ResponseEntity.notFound().build();

        Order order = optionalOrder.get();
        order.setStatus(status);
        orderRepository.save(order);

        // 🔔 Realtidsnotifiering (WebSocket)
        messagingTemplate.convertAndSend("/topic/orders", order);

        return ResponseEntity.ok(order);
    }

    // ❌ Ta bort order
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable String id) {
        orderRepository.deleteById(id);
    }
}

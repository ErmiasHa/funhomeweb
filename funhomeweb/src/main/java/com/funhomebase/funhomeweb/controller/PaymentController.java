package com.funhomebase.funhomeweb.controller;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin
public class PaymentController {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @PostMapping("/create-checkout-session")
    public Map<String, Object> createCheckoutSession(@RequestBody Map<String, Object> data) throws Exception {
        Stripe.apiKey = stripeSecretKey;

        List<Map<String, Object>> items = (List<Map<String, Object>>) data.get("items");

        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
        for (Map<String, Object> item : items) {
            String name = (String) item.get("name");
            int price = ((Number) item.get("price")).intValue() * 100; // öre
            int qty = ((Number) item.get("quantity")).intValue();

            lineItems.add(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity((long) qty)
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("sek")
                                            .setUnitAmount((long) price)
                                            .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName(name)
                                                    .build())
                                            .build())
                            .build()
            );
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/success.html")
                .setCancelUrl("http://localhost:8080/cancel.html")
                .addAllLineItem(lineItems)
                .build();

        Session session = Session.create(params);
        Map<String, Object> response = new HashMap<>();
        response.put("url", session.getUrl());
        return response;
    }
}

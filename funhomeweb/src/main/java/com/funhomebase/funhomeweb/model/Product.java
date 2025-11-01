package com.funhomebase.funhomeweb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "products")
public class Product {

    @Id
    private String id;
    private String name;
    private String description;
    private double price;
    private String category; // ✅ Nytt fält för filtrering
    private String imageUrl;
    private boolean bestseller;
    private Date createdAt = new Date();

    public Product() {}

    public Product(String name, String description, double price, String category, String imageUrl, boolean bestseller) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
        this.bestseller = bestseller;
        this.createdAt = new Date();
    }

    // --- Getters och Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isBestseller() { return bestseller; }
    public void setBestseller(boolean bestseller) { this.bestseller = bestseller; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}

package com.example.tejidospulido_app.Model.Classes;

import java.io.Serializable;
import java.util.Date;

public class OrderLine implements Serializable {
    private String product;
    private String quantity;
    private String price;

    public OrderLine() {
        this.product = "";
        this.quantity = "";
        this.price = "";
    }

    public OrderLine(String product, String quantity, String price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderLine{" +
                "product='" + product + '\'' +
                ", quantity='" + quantity + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}

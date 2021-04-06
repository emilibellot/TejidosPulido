package com.example.tejidospulido_app.Model.Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Serializable {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String image;
    private Map<String, String> favoritos;
    private Map<String, String> carrito;
    private Order pedido;
    private List<String> orders;
    private String address;
    private String billingAddress;
    private Map<String, Address> addresses;

    public User(){};

    public User(String email, String password) {
        this.username = "";
        this.password = password;
        this.email = email;
        this.phone = "";
        this.image = "";
        this.orders = new ArrayList<>();
        this.address = "";
        this.billingAddress = "";
        this.addresses = new HashMap<>();
        this.favoritos = new HashMap<>();
        this.carrito = new HashMap<>();
        this.pedido = new Order();
        this.orders = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phoneNumber) {
        this.phone = phoneNumber;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Map<String, String> getFavoritos() {
        return this.favoritos;
    }

    public void setFavoritos(Map<String, String> favoritos) {
        this.favoritos = favoritos;
    }

    public Map<String, String> getCarrito() {
        return this.carrito;
    }

    public void setCarrito(Map<String, String> carrito) {
        this.carrito = carrito;
    }

    public List<String> getOrders() {
        return this.orders;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Map<String, Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Map<String, Address> addresses) {
        this.addresses = addresses;
    }

    public Order getPedido() {
        return pedido;
    }

    public void setPedido(Order pedido) {
        this.pedido = pedido;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", image='" + image + '\'' +
                ", favoritos=" + favoritos +
                ", carrito=" + carrito +
                ", pedido=" + pedido +
                ", orders=" + orders +
                ", address='" + address + '\'' +
                ", billingAddress='" + billingAddress + '\'' +
                ", addresses=" + addresses +
                '}';
    }
}

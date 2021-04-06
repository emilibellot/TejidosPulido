package com.example.tejidospulido_app.Model.Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Order implements Serializable {
    private String customer;
    private String date;
    private Address address;
    private Address billingAddress;
    private ArrayList<OrderLine> order;
    private String price;
    private String totalPrice;
    private String state;
    private String payForm;
    private String phone;

    public Order(){}

    public Order(String customer, ArrayList<OrderLine> order, String price, Address address, Address billingAddress, String date) {
        this.customer = customer;
        this.address = address;
        this.billingAddress = billingAddress;
        this.order = order;
        this.date = date;
        this.price = price;
        this.totalPrice = "";
        this.state = "sin confirmar";
        this.payForm = "";
        this.phone = "";
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public ArrayList<OrderLine> getOrder() {
        return order;
    }

    public void setOrder(ArrayList<OrderLine> order) {
        this.order = order;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPayForm() {
        return payForm;
    }

    public void setPayForm(String payForm) {
        this.payForm = payForm;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Order{" +
                "customer='" + customer + '\'' +
                ", date=" + date +
                ", address=" + address +
                ", billingAddress=" + billingAddress +
                ", order=" + order +
                ", price='" + price + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", state='" + state + '\'' +
                ", payForm='" + payForm + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
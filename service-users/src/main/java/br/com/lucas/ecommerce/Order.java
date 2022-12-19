package br.com.lucas.ecommerce;

import lombok.Data;

public class Order {

    private String email;

    public Order(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Order{" +
                "email='" + email + '\'' +
                '}';
    }
}

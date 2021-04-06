package com.example.tejidospulido_app.Model.Classes;

import java.io.Serializable;

public class ProductReference implements Serializable {
    private String numero;

    public ProductReference(){}

    public ProductReference(String numero) {
        this.numero = numero;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "ProductReference{" +
                "numero='" + numero + '\'' +
                '}';
    }
}

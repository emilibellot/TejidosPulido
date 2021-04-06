package com.example.tejidospulido_app.Model.Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Page implements Serializable {
    private String nombre;
    private String descripcion;
    private String estado;
    private Map<String, ProductReference> productos;

    public Page() {
    }

    public Page(String nombre) {
        this.nombre = nombre;
        this.descripcion = "";
        this.estado = "";
    }

    public Page(String nombre, Map<String, ProductReference> productos) {
        this.nombre = nombre;
        this.descripcion = "";
        this.estado = "";
        this.productos = productos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Map<String, ProductReference> getProductos() {
        return this.productos;
    }

    public void setProductos(Map<String, ProductReference> productos) {
        this.productos = productos;
    }

    public ArrayList<ProductReference> getListOfProducts() {
        ArrayList<ProductReference> productReferences = new ArrayList<>();
        if (productos != null) {
            for (Map.Entry<String, ProductReference> product : productos.entrySet()) {
                productReferences.add(product.getValue());
            }
        }
        return productReferences;
    }

    @Override
    public String toString() {
        return "Page{" +
                "nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", estado='" + estado + '\'' +
                ", productos=" + productos +
                '}';
    }
}
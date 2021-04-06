package com.example.tejidospulido_app.Model.Classes;

import java.io.Serializable;
import java.util.ArrayList;

public class PageReference implements Serializable {
    private String nombre;

    public PageReference(){}

    public PageReference(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "PageReference{" +
                "nombre='" + nombre + '\'' +
                '}';
    }
}


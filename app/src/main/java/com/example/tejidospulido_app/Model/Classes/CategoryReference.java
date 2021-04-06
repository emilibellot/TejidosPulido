package com.example.tejidospulido_app.Model.Classes;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryReference implements Serializable {
    private String nombre;
    private ArrayList<PageReference> pagina;

    public CategoryReference(){}

    public CategoryReference(String nombre) {
        this.nombre = nombre;
        this.pagina = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<PageReference> getPagina() {
        return pagina;
    }

    public void setPagina(ArrayList<PageReference> pagina) {
        this.pagina = pagina;
    }

    @Override
    public String toString() {
        return "CategoryReference{" +
                "nombre='" + nombre + '\'' +
                ", pagina=" + pagina +
                '}';
    }
}


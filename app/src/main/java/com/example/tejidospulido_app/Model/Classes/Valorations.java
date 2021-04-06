package com.example.tejidospulido_app.Model.Classes;

import java.io.Serializable;
import java.util.ArrayList;

public class Valorations implements Serializable {
    private String valoracion;
    private ArrayList<Valoration> valoraciones;

    public Valorations() {
        this.valoracion = "0";
        this.valoraciones = new ArrayList<>();
    }

    public Valorations(String valoracion, ArrayList<Valoration> valoraciones) {
        this.valoracion = valoracion;
        this.valoraciones = valoraciones;
    }

    public String getValoracion() {
        return valoracion;
    }

    public void setValoracion(String valoracion) {
        this.valoracion = valoracion;
    }

    public ArrayList<Valoration> getValoraciones() {
        return valoraciones;
    }

    public void setValoraciones(ArrayList<Valoration> valoraciones) {
        this.valoraciones = valoraciones;
    }

    @Override
    public String toString() {
        return "Valorations{" +
                "valoracion='" + valoracion + '\'' +
                ", valoraciones=" + valoraciones +
                '}';
    }
}

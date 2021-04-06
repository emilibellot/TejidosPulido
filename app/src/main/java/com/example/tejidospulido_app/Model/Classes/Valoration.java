package com.example.tejidospulido_app.Model.Classes;

import java.io.Serializable;

public class Valoration implements Serializable {
    private String username;
    private String comentario;
    private String valoracion;

    public Valoration() {
        this.username = "";
        this.comentario = "";
        this.valoracion = "0";
    }

    public Valoration(String username, String comentario, String valoracion) {
        this.username = username;
        this.comentario = comentario;
        this.valoracion = valoracion;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getValoracion() {
        return valoracion;
    }

    public void setValoracion(String valoracion) {
        this.valoracion = valoracion;
    }

    @Override
    public String toString() {
        return "Valoration{" +
                "username='" + username + '\'' +
                ", comenatrio='" + comentario + '\'' +
                ", valoracion='" + valoracion + '\'' +
                '}';
    }
}

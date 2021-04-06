package com.example.tejidospulido_app.Model.Classes;

import java.io.Serializable;

public class Address implements Serializable {
    private String nombre;
    private String apellidos;
    private String direccion;
    private String codigoPostal;
    private String poblacion;
    private String pais;

    public Address(){
        this.nombre = "";
        this.apellidos = "";
        this.direccion = "";
        this.codigoPostal = "";
        this.poblacion = "";
        this.pais = "";
    }

    public Address(String nombre, String apellidos, String direccion, String codigoPostal, String poblacion, String pais) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.codigoPostal = codigoPostal;
        this.poblacion = poblacion;
        this.pais = pais;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    @Override
    public String toString() {
        return "Adress{" +
                "nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", direccion='" + direccion + '\'' +
                ", codigoPostal='" + codigoPostal + '\'' +
                ", poblacion='" + poblacion + '\'' +
                ", pais='" + pais + '\'' +
                '}';
    }
}

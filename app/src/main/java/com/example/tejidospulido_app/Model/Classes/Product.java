package com.example.tejidospulido_app.Model.Classes;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;

public class Product implements Serializable {
    private String nombre;
    private String numero;
    private String referencia;
    private String visibilidad;
    private String descripcion;
    private String funciones;
    private String imagen;
    private String precio;
    private String peso;
    private String stock;
    private String palabras_clave;
    private CategoryReference categorias;
    private Valorations valoracion;
    private HashMap<String, String> combinaciones;

    public Product(){}

    public Product(String nombre, String numero, String referencia) {
        this.nombre = nombre;
        this.numero = numero;
        this.referencia = referencia;
        this.visibilidad = "";
        this.descripcion = "";
        this.funciones = "";
        this.imagen = "";
        this.precio = "";
        this.peso = "";
        this.stock = "";
        this.palabras_clave = "";
        this.categorias = new CategoryReference();
        this.valoracion = new Valorations();
        this.combinaciones = new HashMap<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getVisibilidad() {
        return visibilidad;
    }

    public void setVisibilidad(String visibilidad) {
        this.visibilidad = visibilidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFunciones() {
        return funciones;
    }

    public void setFunciones(String funciones) {
        this.funciones = funciones;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getPalabras_clave() {
        return palabras_clave;
    }

    public void setPalabras_clave(String palabras_clave) {
        this.palabras_clave = palabras_clave;
    }

    public CategoryReference getCategorias() {
        return categorias;
    }

    public void setCategorias(CategoryReference categorias) {
        this.categorias = categorias;
    }

    public Valorations getValoracion() {
        return valoracion;
    }

    public void setValoracion(Valorations valoracion) {
        this.valoracion = valoracion;
    }

    public HashMap<String, String> getCombinaciones() {
        return combinaciones;
    }

    public void setCombinaciones(HashMap<String, String> combinaciones) {
        this.combinaciones = combinaciones;
    }

    @Override
    public String toString() {
        return "Product{" +
                "nombre='" + nombre + '\'' +
                ", numero='" + numero + '\'' +
                ", referencia='" + referencia + '\'' +
                ", visibilidad='" + visibilidad + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", funciones='" + funciones + '\'' +
                ", imagen='" + imagen + '\'' +
                ", precio='" + precio + '\'' +
                ", peso='" + peso + '\'' +
                ", stock='" + stock + '\'' +
                ", palabras_clave='" + palabras_clave + '\'' +
                ", categorias=" + categorias +
                ", valoracion=" + valoracion +
                ", combinaciones=" + combinaciones +
                '}';
    }
}

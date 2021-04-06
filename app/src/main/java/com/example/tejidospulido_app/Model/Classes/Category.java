package com.example.tejidospulido_app.Model.Classes;
import java.util.ArrayList;
import java.util.Map;

public class Category {
    private String nombre;
    private String descripcion;
    private String estado;
    private Map<String, ProductReference> productos;
    private Map<String, Page> paginas;

    public Category(){}

    public Category(String nombre) {
        this.nombre = nombre;
        this.descripcion = "";
        this.estado = "";
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

    public Map<String, Page> getPaginas() { return this.paginas; }

    public ArrayList<Page> getListOfPaginas() {
        Page pagina = new Page("Todo " + this.getNombre(), this.getProductos());
        ArrayList<Page> pages = new ArrayList<>();
        pages.add(pagina);
        if (paginas != null) {
            for (Map.Entry<String, Page> page : paginas.entrySet()) {
                pages.add(page.getValue());
            }
        }
        return pages;
    }


    public void setPaginas(Map<String, Page> paginas) {
        this.paginas = paginas;
    }

    @Override
    public String toString() {
        return nombre;
    }
}

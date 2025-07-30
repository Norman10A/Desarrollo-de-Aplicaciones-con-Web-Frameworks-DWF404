package sv.edu.udb.model;

import java.util.ArrayList;
import java.util.List;

public class Producto {
    private String nombre;
    private double precio;
    private int cantidad;


    public static List<Producto> productosPredefinidos = new ArrayList<>();

    static {
        productosPredefinidos.add(new Producto("Mouse", 10.5, 20));
        productosPredefinidos.add(new Producto("Teclado", 25.0, 15));
        productosPredefinidos.add(new Producto("Monitor", 99.9, 5));
    }

    public Producto() {
    }

    public Producto(String nombre, double precio, int cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
package com.example.backend_veterinaria.model;

import jakarta.persistence.*;

@Entity
@Table(name = "servicios")
public class Servicios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String codigo;

    @Column
    private String categoria;
    @Column
    private String nombre;
    @Column
    private String especie;
    @Column
    private int duracion;
    @Column
    private int precio;
    @Column
    private String observaciones;

    public Servicios() {}
    public Servicios(String codigo, String categoria, String nombre, String especie, int duracion, int precio) {}

    public String getCodigo() {return codigo;}
    public void setCodigo(String codigo) {this.codigo = codigo;}
    public String getCategoria() {return categoria;}
    public void setCategoria(String categoria) {this.categoria = categoria;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getEspecie() {return especie;}
    public void setEspecie(String especie) {this.especie = especie;}
    public int getDuracion() {return duracion;}
    public void setDuracion(int duracion) {this.duracion = duracion;}
    public int getPrecio() {return precio;}
    public void setPrecio(int precio) {this.precio = precio;}
    public String getObservaciones() {return observaciones;}
    public void setObservaciones(String observaciones) {this.observaciones = observaciones;}
}

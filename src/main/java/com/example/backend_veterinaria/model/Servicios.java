package com.example.backend_veterinaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "servicios")
public class Servicios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long codigo;

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

    public Servicios(@NotBlank @Size(max = 20) String categoria, @NotBlank @Size(max = 40) String nombre, @NotBlank @Size(max = 20) String especie, @PositiveOrZero int duracion, @PositiveOrZero int precio, @NotBlank @Size(max = 100) String observaciones) {
        this.categoria = categoria;
        this.nombre = nombre;
        this.especie = especie;
        this.duracion = duracion;
        this.precio = precio;
        this.observaciones = observaciones;
    }


    public long getCodigo() {return codigo;}
    public void setCodigo(long codigo) {this.codigo = codigo;}
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

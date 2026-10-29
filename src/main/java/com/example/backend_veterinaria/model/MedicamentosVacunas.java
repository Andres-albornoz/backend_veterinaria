package com.example.backend_veterinaria.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Medicamentos y Vacunas")
public class MedicamentosVacunas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String codigo;

    @Column
    private String categoria;
    @Column
    private String nombre;
    @Column
    private String principioActivo;
    @Column
    private String presentacion;
    @Column
    private String especie;
    @Column
    private int Stock;
    @Column
    private int precio;

    public MedicamentosVacunas() {}

    public MedicamentosVacunas(String codigo, String categoria, String nombre, String principioActivo, String presentacion, String especie, int stock, int precio) {
        this.codigo = codigo;
        this.categoria = categoria;
        this.nombre = nombre;
        this.principioActivo = principioActivo;
        this.presentacion = presentacion;
        this.especie = especie;
        Stock = stock;
        this.precio = precio;
    }

    public String getCodigo() {return codigo;}
    public void setCodigo(String codigo) {this.codigo = codigo;}
    public String getCategoria() {return categoria;}
    public void setCategoria(String categoria) {this.categoria = categoria;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getPrincipioActivo() {return principioActivo;}
    public void setPrincipioActivo(String principioActivo) {this.principioActivo = principioActivo;}
    public String getPresentacion() {return presentacion;}
    public void setPresentacion(String presentacion) {this.presentacion = presentacion;}
    public String getEspecie() {return especie;}
    public void setEspecie(String especie) {this.especie = especie;}
    public int getStock() {return Stock;}
    public void setStock(int stock) {Stock = stock;}
    public int getPrecio() {return precio;}
    public void setPrecio(int precio) {this.precio = precio;}
}

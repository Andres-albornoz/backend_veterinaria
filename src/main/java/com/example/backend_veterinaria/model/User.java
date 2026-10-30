package com.example.backend_veterinaria.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Usuario")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String nombre;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private  String rol;

    public User() {}
    public User(int id, String nombre, String email, String password, String rol) {}

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getNombre() {return this.nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getEmail() {return this.email;}
    public void setEmail(String email) {this.email = email;}
    public String getPassword() {return this.password;}
    public void setPassword(String password) {this.password = password;}
    public String getRol() {return this.rol;}
    public void setRol(String rol) {this.rol = rol;}

}

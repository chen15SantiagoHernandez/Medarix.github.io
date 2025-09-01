package com.example.medarix.models;

public class Enfermedad {
    public int id_enfermedad;
    public String nombre;
    public String descripcion;
    public String sintomas;
    public String tratamientos;

    public Enfermedad(int id_enfermedad, String nombre, String descripcion, String sintomas, String tratamientos) {
        this.id_enfermedad = id_enfermedad;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.sintomas = sintomas;
        this.tratamientos = tratamientos;
    }

    public int getId_enfermedad() {
        return id_enfermedad;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getSintomas() {
        return sintomas;
    }

    public String getTratamientos() {
        return tratamientos;
    }

}

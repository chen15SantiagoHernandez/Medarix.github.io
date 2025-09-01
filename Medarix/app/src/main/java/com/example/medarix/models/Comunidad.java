package com.example.medarix.models;

public class Comunidad {

    public int id_enfermedad;
    public String nombre;
    public String descripcion;
    public int respuestas;

    public Comunidad(int id_enfermedad, String nombre, String descripcion, int respuestas) {
        this.id_enfermedad = id_enfermedad;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.respuestas = respuestas;
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

    public int getRespuestas() {
        return respuestas;
    }
}
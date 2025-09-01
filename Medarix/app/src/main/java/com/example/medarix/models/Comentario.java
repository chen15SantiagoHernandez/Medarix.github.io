package com.example.medarix.models;

public class Comentario {

    public String nombre;
    public String texto;
    public String fecha_comentario;

    public Comentario(String nombre, String texto, String fecha_comentario) {
        this.nombre = nombre;
        this.texto = texto;
        this.fecha_comentario = fecha_comentario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTexto() {
        return texto;
    }
    public String getFecha_comentario() {
        return fecha_comentario;
    }
}
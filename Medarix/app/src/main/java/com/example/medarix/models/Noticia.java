package com.example.medarix.models;

public class Noticia {
    public int id;
    public String titulo;
    public String fuente;
    public String descripcion;
    public String fecha;
    public String url;
    public int logoRes;

    public Noticia(int id, String titulo, String fuente, String descripcion, String fecha,String url, int logoRes) {
        this.id = id;
        this.titulo = titulo;
        this.fuente = fuente;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.url = url;
        this.logoRes = logoRes;
    }

    public String getUrl() {
        return url;
    }
}
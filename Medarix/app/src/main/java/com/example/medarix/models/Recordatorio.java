package com.example.medarix.models;

public class Recordatorio {
    public String titulo;
    public int hora;
    public int minuto;
    public boolean activo;

    public Recordatorio(String titulo, int hora, int minuto, boolean activo) {
        this.titulo = titulo;
        this.hora = hora;
        this.minuto = minuto;
        this.activo = activo;
    }

    public String horaFormateada() {
        return String.format("%02d:%02d", hora, minuto);
    }
}

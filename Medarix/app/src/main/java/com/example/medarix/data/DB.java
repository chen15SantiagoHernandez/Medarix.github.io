package com.example.medarix.data;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.medarix.models.Comentario;
import com.example.medarix.models.Comunidad;
import com.example.medarix.models.Enfermedad;
import com.example.medarix.models.Noticia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DB {

    private static final String URL = "jdbc:postgresql://10.0.2.2:5432/Enfermedades";
    private static final String USUARIO = "postgres";
    private static final String PASSWORD = "Admin1234";

    public interface  OnQueryResultListener<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }

    // Sirve para crear hilos que se encarga de realizar las consultas en la BBDD
    private final Executor executor = Executors.newSingleThreadExecutor();
    // Para mandar notificar de que algo ha ocurrido durante los hilos
    private final Handler handler = new Handler(Looper.getMainLooper());

    // NO se cierra la conexión de bases de datos (postgresql) con onDestry()
    // porque la conexión se cierra solo al acabar try
    // OnQueryResultListener es para saber a qu clase habría que notificarlo
    public void obtenerUsuario(String correo, String password, OnQueryResultListener listener) {
        executor.execute(() -> {
            List<String> datos = new ArrayList<String>();
            // SOlo captura excepciones
            try {
                // Para cargar el driver
                Class.forName("org.postgresql.Driver");
                // Este es el que cierra la conexión
                try (Connection connection = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                     Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT nombre, id_usuario FROM usuarios WHERE correo = '" + correo + "' and contraseña = '" + password + "'")) {
                    if (rs.next()) {
                        datos.add(rs.getString("nombre"));
                        datos.add(rs.getString("id_usuario"));
                    }
                }
                // para indicar que ha hecho correctamente
                handler.post(() -> listener.onSuccess(datos));
            }
            catch (Exception e) {
                Log.e("Base datos", "Error en la conexion", e);
                // Para indicar que ha habido un error
                handler.post(() -> listener.onError(e));
            }
        });
    }

    public void comprobarUsuario(String correo, OnQueryResultListener listener) {
        executor.execute(() -> {
            List<String> datos = new ArrayList<String>();
            try {
                Class.forName("org.postgresql.Driver");
                try (Connection connection = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                     Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT nombre, id_usuario FROM usuarios WHERE correo = '" + correo + "'")) {
                    if (rs.next()) {
                        datos.add(rs.getString("nombre"));
                        datos.add(rs.getString("id_usuario"));
                    }
                }

                handler.post(() -> listener.onSuccess(datos));
            }
            catch (Exception e) {
                Log.e("Base datos", "Error en la conexion", e);
                handler.post(() -> listener.onError(e));
            }
        });
    }

    // Inserta usuario
    public void insertarUsuario(String nombre, String correo, String contraseña, String tipo, String ubicacion, OnQueryResultListener listener) {
        executor.execute(() -> {
            String sql = "INSERT INTO usuarios(nombre, correo, contraseña, tipo_usuario, ubicacion, fecha_registro) VALUES(?, ?, ?, ?, ?, ?)";
            try (Connection conn = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, nombre);
                pstmt.setString(2, correo);
                pstmt.setString(3, contraseña);
                pstmt.setString(4, tipo);
                pstmt.setString(5, ubicacion);
                pstmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
                int affectedRows = pstmt.executeUpdate();
                handler.post(() -> listener.onSuccess(affectedRows));

            } catch (Exception e) {
                handler.post(() -> listener.onError(e));
            }
        });
    }

    public void obtenerNoticias(OnQueryResultListener listener) {
        executor.execute(() -> {
            List<Noticia> noticias = new ArrayList<Noticia>();
            try {
                Class.forName("org.postgresql.Driver");
                try (Connection connection = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                     Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT id_material, titulo, fuente, cast(descripcion as varchar(50)) || '...' descripcion, fecha, url FROM material_educativo;")) {
                    while (rs.next()) {
                        Noticia noticia = new Noticia(rs.getInt("id_material"), rs.getString("titulo"), rs.getString("fuente"), rs.getString("descripcion"), rs.getString("fecha"), rs.getString("url"), android.R.drawable.ic_dialog_info);
                        noticias.add(noticia);
                    }
                }

                handler.post(() -> listener.onSuccess(noticias));
            }
            catch (Exception e) {
                Log.e("Base datos", "Error en la conexion", e);
                handler.post(() -> listener.onError(e));
            }
        });
    }

    public void obtenerEnfermedades(OnQueryResultListener listener) {
        executor.execute(() -> {
            List<Enfermedad> enfermedades = new ArrayList<Enfermedad>();
            try {
                Class.forName("org.postgresql.Driver");
                try (Connection connection = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                     Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT id_enfermedad, nombre, descripcion, sintomas, tratamientos FROM enfermedades")) {
                    while (rs.next()) {
                        Enfermedad enfermedad = new Enfermedad(rs.getInt("id_enfermedad"), rs.getString("nombre"), rs.getString("descripcion"), rs.getString("sintomas"), rs.getString("tratamientos"));
                        enfermedades.add(enfermedad);
                    }
                }

                handler.post(() -> listener.onSuccess(enfermedades));
            }
            catch (Exception e) {
                Log.e("Base datos", "Error en la conexion", e);
                handler.post(() -> listener.onError(e));
            }
        });
    }

    public void obtenerEnfermedadesFiltro(String filtro, OnQueryResultListener listener) {
        executor.execute(() -> {
            List<Enfermedad> enfermedades = new ArrayList<Enfermedad>();
            try {
                Class.forName("org.postgresql.Driver");
                try (Connection connection = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                     Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT id_enfermedad, nombre, descripcion, sintomas, tratamientos FROM enfermedades where nombre LIKE('%" + filtro + "%')")) {
                    while (rs.next()) {
                        Enfermedad enfermedad = new Enfermedad(rs.getInt("id_enfermedad"), rs.getString("nombre"), rs.getString("descripcion"), rs.getString("sintomas"), rs.getString("tratamientos"));
                        enfermedades.add(enfermedad);
                    }
                }

                handler.post(() -> listener.onSuccess(enfermedades));
            }
            catch (Exception e) {
                Log.e("Base datos", "Error en la conexion", e);
                handler.post(() -> listener.onError(e));
            }
        });
    }

    public void obtenerComunidades(OnQueryResultListener listener) {
        executor.execute(() -> {
            List<Comunidad> comunidades = new ArrayList<Comunidad>();
            try {
                Class.forName("org.postgresql.Driver");
                try (Connection connection = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                     Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT a1.id_enfermedad, a1.nombre, a1.descripcion, count(a2.id_mensaje) respuestas FROM public.enfermedades as a1 left outer join public.comunidad as a2 on a1.id_enfermedad = a2.id_enfermedad group by a1.id_enfermedad, a1.nombre, a1.descripcion")) {
                    while (rs.next()) {
                        Comunidad comunidad = new Comunidad(rs.getInt("id_enfermedad"), rs.getString("nombre"), rs.getString("descripcion"), rs.getInt("respuestas"));
                        comunidades.add(comunidad);
                    }
                }

                handler.post(() -> listener.onSuccess(comunidades));
            }
            catch (Exception e) {
                Log.e("Base datos", "Error en la conexion", e);
                handler.post(() -> listener.onError(e));
            }
        });
    }

    public void obtenerComunidadesFiltro(String filtro, OnQueryResultListener listener) {
        executor.execute(() -> {
            List<Comunidad> comunidades = new ArrayList<Comunidad>();
            try {
                Class.forName("org.postgresql.Driver");
                try (Connection connection = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                     Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT a1.id_enfermedad, a1.nombre, a1.descripcion, count(a2.id_mensaje) respuestas FROM public.enfermedades as a1 left outer join public.comunidad as a2 on a1.id_enfermedad = a2.id_enfermedad where a1.nombre like ('%" + filtro + "%') group by a1.id_enfermedad, a1.nombre, a1.descripcion")) {
                    while (rs.next()) {
                        Comunidad comunidad = new Comunidad(rs.getInt("id_enfermedad"), rs.getString("nombre"), rs.getString("descripcion"), rs.getInt("respuestas"));
                        comunidades.add(comunidad);
                    }
                }

                handler.post(() -> listener.onSuccess(comunidades));
            }
            catch (Exception e) {
                Log.e("Base datos", "Error en la conexion", e);
                handler.post(() -> listener.onError(e));
            }
        });
    }

    public void obtenerComentarios(String query, OnQueryResultListener listener) {
        executor.execute(() -> {
            List<Comentario> comentarios = new ArrayList<Comentario>();
            try {
                Class.forName("org.postgresql.Driver");
                try (Connection connection = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                     Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT a2.nombre, a1.contenido, a1.fecha_publicado FROM public.comunidad as a1 inner join public.usuarios as a2 on a1.id_usuario = a2.id_usuario where a1.id_enfermedad = " + query + " order by a1.fecha_publicado asc")) {
                    while (rs.next()) {
                        Comentario comentario = new Comentario(rs.getString("nombre"), rs.getString("contenido"), rs.getString("fecha_publicado"));
                        comentarios.add(comentario);
                    }
                }

                handler.post(() -> listener.onSuccess(comentarios));
            }
            catch (Exception e) {
                Log.e("Base datos", "Error en la conexion", e);
                handler.post(() -> listener.onError(e));
            }
        });
    }

    public void insertarComentario(String id, String id_enfermedad, String texto, OnQueryResultListener listener) {
        executor.execute(() -> {
            String sql = "INSERT INTO comunidad(id_usuario, id_enfermedad, contenido, fecha_publicado) VALUES(?, ?, ?, ?)";
            try (Connection conn = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, Integer.parseInt(id));
                pstmt.setInt(2, Integer.parseInt(id_enfermedad));
                pstmt.setString(3, texto);
                pstmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
                int affectedRows = pstmt.executeUpdate();
                handler.post(() -> listener.onSuccess(affectedRows));

            } catch (Exception e) {
                handler.post(() -> listener.onError(e));
            }
        });
    }
}

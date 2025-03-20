/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homeSweetHome.model;

import com.mysql.cj.jdbc.Blob;
import java.util.Date;

/**
 *
 * @author Usuario
 */
public class Task {

    

    private int id; // Identificador único de la tarea
    private String nombreTarea; // Nombre de la tarea
    private Date fechaLimite; // Fecha límite de la tarea
    private String asignadoANombre; // Nombre de la persona asignada a la tarea
    private int asignadoAId; // ID de la persona asignada
    private String estado; // Estado de la tarea (Pendiente o Completada)

    // Constructor vacío
    public Task() {
    }

    // Constructor con parámetros
    public Task(int id, String nombreTarea, Date fechaLimite, String asignadoANombre, int asignadoAId, String estado) {
        this.id = id;
        this.nombreTarea = nombreTarea;
        this.fechaLimite = fechaLimite;
        this.asignadoANombre = asignadoANombre;
        this.asignadoAId = asignadoAId;
        this.estado = estado;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreTarea() {
        return nombreTarea;
    }

    public void setNombreTarea(String nombreTarea) {
        this.nombreTarea = nombreTarea;
    }

    public Date getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public String getAsignadoANombre() {
        return asignadoANombre;
    }

    public void setAsignadoANombre(String asignadoANombre) {
        this.asignadoANombre = asignadoANombre;
    }

    public int getAsignadoAId() {
        return asignadoAId;
    }

    public void setAsignadoAId(int asignadoAId) {
        this.asignadoAId = asignadoAId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Método toString para depuración o visualización
    @Override
    public String toString() {
        return "Task{"
                + "id=" + id
                + ", nombreTarea='" + nombreTarea + '\''
                + ", fechaLimite=" + fechaLimite
                + ", asignadoANombre='" + asignadoANombre + '\''
                + ", asignadoAId=" + asignadoAId
                + ", estado='" + estado + '\''
                + '}';
    }

}

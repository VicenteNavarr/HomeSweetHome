package homeSweetHome.dataPersistence;

import homeSweetHome.model.Task;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    private final Connection connection;

    // Constructor para inicializar la conexión
    public TaskDAO(Connection connection) {
        this.connection = connection;
    }

    // Método para añadir una tarea
    public boolean addTask(Task task) {
        String sql = "INSERT INTO Tareas (nombre_tarea, fecha_limite, asignado_a_nombre, asignado_a_id, estado) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, task.getNombreTarea());
            stmt.setDate(2, task.getFechaLimite() != null ? new Date(task.getFechaLimite().getTime()) : null);
            stmt.setString(3, task.getAsignadoANombre());
            stmt.setInt(4, task.getAsignadoAId());
            stmt.setString(5, task.getEstado());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para listar todas las tareas
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM Tareas";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setNombreTarea(rs.getString("nombre_tarea"));
                task.setFechaLimite(rs.getDate("fecha_limite"));
                task.setAsignadoANombre(rs.getString("asignado_a_nombre"));
                task.setAsignadoAId(rs.getInt("asignado_a_id"));
                task.setEstado(rs.getString("estado"));
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // Método para modificar una tarea
    public boolean updateTask(Task task) {
        String sql = "UPDATE Tareas SET nombre_tarea = ?, fecha_limite = ?, asignado_a_nombre = ?, asignado_a_id = ?, estado = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, task.getNombreTarea());
            stmt.setDate(2, task.getFechaLimite() != null ? new Date(task.getFechaLimite().getTime()) : null);
            stmt.setString(3, task.getAsignadoANombre());
            stmt.setInt(4, task.getAsignadoAId());
            stmt.setString(5, task.getEstado());
            stmt.setInt(6, task.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para eliminar una tarea por ID
    public boolean deleteTask(int id) {
        String sql = "DELETE FROM Tareas WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

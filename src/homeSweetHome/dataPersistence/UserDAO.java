package homeSweetHome.dataPersistence;

import homeSweetHome.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión de Usuarios con soporte para Grupos (id_grupo).
 */
public class UserDAO {

    /**
     * Método para añadir el primer usuario al sistema. Este usuario será un
     * administrador y se asociará con un nuevo grupo.
     *
     * @param user - Usuario a registrar
     * @return
     */
    public boolean addFirstUser(User user) {

        String sqlGroup = "INSERT INTO Grupos (nombre_grupo) VALUES (?)";
        String sqlUser = "INSERT INTO Usuarios (nombre, apellidos, correo_electronico, contrasenia, id_rol, id_grupo) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = MySQLConnection.getConnection()) {

            conn.setAutoCommit(false); // Iniciar transacción

            // Crear un nuevo grupo
            PreparedStatement groupStmt = conn.prepareStatement(sqlGroup, PreparedStatement.RETURN_GENERATED_KEYS);
            groupStmt.setString(1, "Grupo de " + user.getNombre());
            groupStmt.executeUpdate();

            // Obtener el ID del grupo recién creado
            ResultSet rs = groupStmt.getGeneratedKeys();
            int groupId = 0;
            if (rs.next()) {
                groupId = rs.getInt(1);
            }

            // Insertar el primer usuario y asociarlo al grupo
            PreparedStatement userStmt = conn.prepareStatement(sqlUser);
            userStmt.setString(1, user.getNombre());
            userStmt.setString(2, user.getApellidos());
            userStmt.setString(3, user.getCorreoElectronico());
            userStmt.setString(4, user.getContrasenia());
            userStmt.setInt(5, 1); // ID del rol (Administrador)
            userStmt.setInt(6, groupId);

            int rowsAffected = userStmt.executeUpdate();

            conn.commit(); // Confirmar transacción
            return rowsAffected > 0;

        } catch (SQLException e) {

            System.err.println("Error al registrar el primer usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método para añadir un nuevo usuario (registro o agregado por un
     * administrador) y asociarlo al grupo del administrador.
     *
     * @param user - Usuario a registrar
     * @return
     */
    public boolean addUser(User user) {

        System.out.println("ID del grupo del usuario: " + user.getIdGrupo());

        String sql = "INSERT INTO Usuarios (nombre, apellidos, correo_electronico, contrasenia, id_rol, foto_Perfil, id_grupo) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getNombre()); // Nombre
            pstmt.setString(2, user.getApellidos()); // Apellidos
            pstmt.setString(3, user.getCorreoElectronico()); // Correo electrónico
            pstmt.setString(4, user.getContrasenia()); // Contraseña
            pstmt.setInt(5, user.getIdRol()); // ID del rol
            pstmt.setBlob(6, user.getFotoPerfil()); // Imagen (Blob)
            pstmt.setInt(7, user.getIdGrupo()); // ID del grupo

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {

            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Metodo para eliminar usuario por id
     *
     * @param userId - int
     * @return Usuario eliminado
     */
    public boolean deleteUserById(int userId) {
        String sql = "DELETE FROM Usuarios WHERE id = ?";

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0; // Retorna true si se eliminó al menos una fila

        } catch (SQLException e) {

            System.err.println("Error al eliminar el usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método para actualizar los datos de un usuario en la base de datos.
     *
     * @param user - Objeto User con los datos actualizados.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    /*public boolean updateUser(User user) {
        String sql = "UPDATE Usuarios SET nombre = ?, apellidos = ?, correo_electronico = ?, contrasenia = ?, id_rol = ?, foto_perfil = ? WHERE id = ?";

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getNombre());
            pstmt.setString(2, user.getApellidos());
            pstmt.setString(3, user.getCorreoElectronico());
            pstmt.setString(4, user.getContrasenia());
            pstmt.setInt(5, user.getIdRol());
            pstmt.setBlob(6, user.getFotoPerfil());
            pstmt.setInt(7, user.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Retorna true si al menos una fila fue actualizada.
        } catch (SQLException e) {
            System.err.println("Error al actualizar el usuario: " + e.getMessage());
            return false;
        }
    }*/
    public boolean updateUser(User user) {
        String sqlCheck = "SELECT id FROM Usuarios WHERE correo_electronico = ? AND id != ?";
        String sqlUpdate = "UPDATE Usuarios SET nombre = ?, apellidos = ?, correo_electronico = ?, contrasenia = ?, id_rol = ?, foto_perfil = ? WHERE id = ?";

        try (Connection conn = MySQLConnection.getConnection()) {
            // Recuperar el correo actual desde la base de datos
            String sqlCurrentEmail = "SELECT correo_electronico FROM Usuarios WHERE id = ?";
            PreparedStatement currentEmailStmt = conn.prepareStatement(sqlCurrentEmail);
            currentEmailStmt.setInt(1, user.getId());
            ResultSet currentEmailRs = currentEmailStmt.executeQuery();

            if (currentEmailRs.next()) {
                String currentEmail = currentEmailRs.getString("correo_electronico").trim();
                String newEmail = user.getCorreoElectronico().trim();

                System.out.println("Correo actual en la base de datos: " + currentEmail);
                System.out.println("Correo ingresado: " + newEmail);

                if (!currentEmail.equalsIgnoreCase(newEmail)) {
                    // Verificar si el correo ya está en uso
                    PreparedStatement checkStmt = conn.prepareStatement(sqlCheck);
                    checkStmt.setString(1, newEmail);
                    checkStmt.setInt(2, user.getId());
                    ResultSet rs = checkStmt.executeQuery();

                    if (rs.next()) {
                        // El correo ya está en uso; avisa al administrador
                        System.err.println("El correo ya está en uso por otro usuario.");
                        return false; // Aquí podría decidir si dejarlo como un mensaje informativo
                    }
                }
            }

            // Proceder con la actualización si no hay conflictos
            PreparedStatement updateStmt = conn.prepareStatement(sqlUpdate);
            updateStmt.setString(1, user.getNombre());
            updateStmt.setString(2, user.getApellidos());
            updateStmt.setString(3, user.getCorreoElectronico());
            updateStmt.setString(4, user.getContrasenia());
            updateStmt.setInt(5, user.getIdRol());
            updateStmt.setBlob(6, user.getFotoPerfil());
            updateStmt.setInt(7, user.getId());

            int rowsAffected = updateStmt.executeUpdate();
            System.out.println("Filas afectadas en la actualización: " + rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar el usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método para autenticar un usuario y recuperar su ID de grupo.
     *
     * @param username - Correo electrónico
     * @param password - Contraseña
     * @return Usuario autenticado o null si no existe
     */
    public User authenticateUser(String username, String password) {

        String sql = "SELECT * FROM Usuarios WHERE correo_electronico = ? AND contrasenia = ?";

        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setNombre(resultSet.getString("nombre"));
                user.setApellidos(resultSet.getString("apellidos"));
                user.setCorreoElectronico(resultSet.getString("correo_electronico"));
                user.setIdRol(resultSet.getInt("id_rol"));
                user.setIdGrupo(resultSet.getInt("id_grupo"));
                return user; // Usuario autenticado
            }

        } catch (SQLException e) {

            System.err.println("Error al autenticar usuario: " + e.getMessage());
        }

        return null; // Usuario no encontrado
    }

    /**
     * Método para obtener todos los usuarios de un grupo (incluyendo su rol).
     *
     * @param groupId - ID del grupo
     * @return Lista de usuarios
     */
    public List<User> getUsersByGroup(int groupId) {

        String sql = "SELECT u.id, u.nombre, u.apellidos, u.correo_electronico, u.id_rol, u.foto_perfil FROM Usuarios u WHERE u.id_grupo = ?";

        List<User> users = new ArrayList<>();

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, groupId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNombre(rs.getString("nombre"));
                user.setApellidos(rs.getString("apellidos"));
                user.setCorreoElectronico(rs.getString("correo_electronico"));
                user.setIdRol(rs.getInt("id_rol"));
                user.setFotoPerfil(rs.getBlob("foto_perfil")); // Asignar el BLOB al objeto User

                users.add(user);
            }

        } catch (SQLException e) {

            System.err.println("Error al obtener usuarios por grupo: " + e.getMessage());
        }

        return users;
    }

    /**
     * Metodo para comprobar existencia de usuario por mail
     *
     * @param correoElectronico - String
     * @return booleano
     */
    public static boolean userExists(String correoElectronico) {
        String sql = "SELECT COUNT(*) FROM Usuarios WHERE correo_electronico = ?";

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, correoElectronico);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // Si el conteo es mayor que 0, el usuario existe
            }

        } catch (SQLException e) {
            System.err.println("Error al comprobar si el usuario existe: " + e.getMessage());
        }

        return false; // Por defecto, asumir que no existe
    }

    /**
     * Metodo para obtener el nombre del rol a partir de la id de rol - por si
     * lo quiero utilizar a futuro
     *
     * @param idRol
     * @return
     */
    public String getRoleNameById(int idRol) {

        String sql = "SELECT nombre_rol FROM Roles WHERE id = ?";

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idRol);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                return rs.getString("nombre_rol");

            }

        } catch (SQLException e) {

            System.err.println("Error al obtener el nombre del rol: " + e.getMessage());
        }

        return "Desconocido"; // Valor por defecto si no se encuentra el rol
    }

    /**
     * Método para obtener un usuario de la base de datos por su ID.
     *
     * @param userId - ID del usuario a buscar.
     * @return User - Objeto con los datos del usuario, o null si no se
     * encuentra.
     */
    public User getUserById(int userId) {

        String sql = "SELECT id, nombre, apellidos, correo_electronico, contrasenia, id_rol, foto_perfil FROM Usuarios WHERE id = ?";

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId); // Establecer el ID como parámetro
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                // Crear y llenar un objeto User con los datos obtenidos
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNombre(rs.getString("nombre"));
                user.setApellidos(rs.getString("apellidos"));
                user.setCorreoElectronico(rs.getString("correo_electronico"));
                user.setContrasenia(rs.getString("contrasenia"));
                // Imprime la contraseña recuperada para verificar
                System.out.println("Contraseña recuperada: " + rs.getString("contrasenia"));
                user.setIdRol(rs.getInt("id_rol"));
                user.setFotoPerfil(rs.getBlob("foto_perfil")); // Imagen en formato Blob
                return user;
            }

        } catch (SQLException e) {

            System.err.println("Error al obtener el usuario por ID: " + e.getMessage());
        }

        return null; // Retornar null si no se encuentra al usuario
    }

}

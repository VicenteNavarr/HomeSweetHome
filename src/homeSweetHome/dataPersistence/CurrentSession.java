package homeSweetHome.dataPersistence;

/**
 * Clase que gestiona la sesión actual usando el patrón Singleton.
 * Mantiene el ID del usuario y del grupo al que pertenece.
 */
public class CurrentSession {

    private static CurrentSession instancia;
    private int userId;     // ID del usuario logueado
    private int userGroupId; // ID del grupo al que pertenece el usuario

    // Constructor privado para el patrón Singleton
    private CurrentSession() {}

    public static CurrentSession getInstance() {
        if (instancia == null) {
            instancia = new CurrentSession();
        }
        return instancia;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(int userGroupId) {
        this.userGroupId = userGroupId;
    }

    public void cerrarSesion() {
        instancia = null; // Limpiar la sesión al cerrar sesión
    }
}

    


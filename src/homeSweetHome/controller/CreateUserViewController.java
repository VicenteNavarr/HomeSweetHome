package homeSweetHome.controller;

import homeSweetHome.dataPersistence.CurrentSession;
import homeSweetHome.dataPersistence.UserDAO;
import homeSweetHome.model.User;
import homeSweetHome.utils.ImageUtils;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.sql.Blob;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class CreateUserViewController implements Initializable {

    @FXML
    private TextField fieldName;
    @FXML
    private TextField fieldSurname;
    @FXML
    private TextField fieldMail;
    @FXML
    private ComboBox<String> cmbRol;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnCancel;
    @FXML
    private ImageView imgUser;
    @FXML
    private Button btnLoadImage;

    private UserViewController userViewController;
    @FXML
    private TextField fieldPassword;

    public void setUserViewController(UserViewController userViewController) {
        this.userViewController = userViewController;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbRol.getItems().addAll("Administrador", "Consultor"); // Añadir los roles al ComboBox
    }

    /**
     * Método para mostrar alertas.
     *
     * @param alertType - AlertType
     * @param title - String
     * @param message - String
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Metodo para crear un nuevo usuario(desde el usuario que es admin) -
     * btncreate
     *
     * @param event
     */
    @FXML
    private void createNewUser(ActionEvent event) {

        // Obtener los datos de los campos de texto
        String nombre = fieldName.getText();
        String apellidos = fieldSurname.getText();
        String correoElectronico = fieldMail.getText();
        String contrasenia = fieldPassword.getText(); // Puedes definir una lógica para la contraseña
        int idRol = cmbRol.getSelectionModel().getSelectedIndex() + 1; // Index del rol seleccionado (+1 para ID real)

        // Validar campos vacíos
        if (nombre.isEmpty() || apellidos.isEmpty() || correoElectronico.isEmpty() || contrasenia.isEmpty() || imgUser.getImage() == null) {
            showAlert(Alert.AlertType.WARNING, "Campos incompletos", "Por favor, completa todos los campos e incluye una imagen.");
            return;
        }

        //comprobar que el usuario no existe(para real, utilizo mail, porque puede haber personas con el mismo nombre)
        if (UserDAO.userExists(correoElectronico)) {

            showAlert(Alert.AlertType.ERROR, "Error de Registro", "El usuario ya existe.");
            System.out.println("Error: Ya existe un usuario con el correo electrónico, inténtalo de nuevo: " + correoElectronico);
            //si usuario existe, limpio campos
            fieldName.clear();
            fieldSurname.clear();
            fieldMail.clear();
            imgUser.setImage(null);
            return;
        }

        // Validar que los campos no estén vacíos
        if (nombre.isEmpty() || apellidos.isEmpty() || correoElectronico.isEmpty() || imgUser.getImage() == null) {
            System.out.println("Por favor, completa todos los campos e incluye una imagen.");
            return;
        }

        //Convierte img de perfil(desde imageUtils)
        Blob fotoPerfil;
        try {

            fotoPerfil = new javax.sql.rowset.serial.SerialBlob(ImageUtils.convertImageToBlob(imgUser.getImage()));

        } catch (Exception e) {

            System.err.println("Error al convertir la imagen: " + e.getMessage());
            return;
        }

        // Obtener el ID del grupo del usuario logueado desde CurrentSession
        int userGroupId = CurrentSession.getInstance().getUserGroupId();

        // Crear un objeto User con el ID del grupo
        User newUser = new User(0, nombre, apellidos, correoElectronico, contrasenia, idRol, fotoPerfil, userGroupId);

        // Usar el DAO para guardar el usuario en la base de datos
        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.addUser(newUser);

        if (success) {

            System.out.println("Usuario creado exitosamente.");

            // Limpiar los campos del formulario
            fieldName.clear();
            fieldSurname.clear();
            fieldMail.clear();
            imgUser.setImage(null);

            // Refrescar la lista de usuarios en el controlador principal
            if (userViewController != null) {

                System.out.println("userViewController no es null, llamando a loadUsers...");
                userViewController.loadUsers(); // Refrescar la lista de usuarios

            } else {

                System.out.println("userViewController es null, no se puede llamar a loadUsers.");
            }

            // Cerrar la ventana
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } else {
            System.out.println("Hubo un error al crear el usuario.");
        }
    }

    /**
     * Metodo para cancelar y salir de la vista - btncancel
     *
     * @param event
     */
    @FXML
    private void cancel(ActionEvent event) {

        // Obtener la ventana actual (Stage) a través del botón
        ((Button) event.getSource()).getScene().getWindow().hide();
        System.out.println("Ventana cerrada.");
    }

    /**
     * Metodo para cargar la imagen de perfil - btnloadimage
     *
     * @param event
     */
    @FXML
    private void loadImage(ActionEvent event) {

        // Abrir el selector de archivos
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.jpeg"));//determino los formatos
        File file = fileChooser.showOpenDialog(btnLoadImage.getScene().getWindow());

        if (file != null) {

            // Crear un objeto Image a partir del archivo seleccionado
            Image image = new Image(file.toURI().toString());
            // Establecer la imagen en el ImageView
            imgUser.setImage(image);
            System.out.println("Imagen cargada correctamente: " + file.getName());

        } else {

            System.out.println("No se seleccionó ningún archivo.");
        }

    }
}

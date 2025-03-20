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
public class UpdateUserViewController implements Initializable {

    @FXML
    private TextField fieldName;
    @FXML
    private TextField fieldSurname;
    @FXML
    private TextField fieldMail;
    @FXML
    private ComboBox<String> cmbRol;
    @FXML
    private Button btnCancel;
    @FXML
    private ImageView imgUser;
    @FXML
    private Button btnLoadImage;

    private UserViewController userViewController;
    @FXML
    private TextField fieldPassword;
    @FXML
    private Button btnSave;
    private User currentUser; // Variable para almacenar el usuario actual

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
     * Guarda las modificaciones hechas en el usuario seleccionado(por id usuario)
     * 
     * @param event 
     */
    @FXML
    private void saveChanges(ActionEvent event) {
        // Validar campos vacíos
        String nombre = fieldName.getText();
        String apellidos = fieldSurname.getText();
        String correoElectronico = fieldMail.getText();
        String contrasenia = fieldPassword.getText();
        int idRol = cmbRol.getSelectionModel().getSelectedIndex() + 1;

        if (nombre.isEmpty() || apellidos.isEmpty() || correoElectronico.isEmpty() || contrasenia.isEmpty() || imgUser.getImage() == null) {
            showAlert(Alert.AlertType.WARNING, "Campos incompletos", "Por favor, completa todos los campos e incluye una imagen.");
            return;
        }

        // Convertir la imagen a Blob
        Blob fotoPerfil;
        try {
            fotoPerfil = new javax.sql.rowset.serial.SerialBlob(ImageUtils.convertImageToBlob(imgUser.getImage()));
        } catch (Exception e) {
            System.err.println("Error al convertir la imagen: " + e.getMessage());
            return;
        }

        // Usar el ID del usuario recibido en setUserData
        User updatedUser = new User(currentUser.getId(), nombre, apellidos, correoElectronico, contrasenia, idRol, fotoPerfil, currentUser.getIdGrupo());

        // Llamar al método del DAO para actualizar el usuario
        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.updateUser(updatedUser);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Usuario actualizado correctamente.");
            System.out.println("Usuario actualizado exitosamente.");

            if (userViewController != null) {
                userViewController.loadUsers(); // Refrescar la lista de usuarios
            }

            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo actualizar el usuario. Inténtalo de nuevo.");
            System.err.println("Error al actualizar el usuario.");
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

    /**
     * Metodo que recibe los datos de usuario y los setea en la vista(viene de
     * userItemVIewController)
     *
     * @param user
     */
    public void setUserData(User user) {
        this.currentUser = user; // Almacena el usuario recibido
        System.out.println("ID recibido en setUserData: " + user.getId());

        fieldName.setText(user.getNombre());
        fieldSurname.setText(user.getApellidos());
        fieldMail.setText(user.getCorreoElectronico());
        System.out.println("Contraseña asignada al campo: " + user.getContrasenia());
        fieldPassword.setText(user.getContrasenia());
        cmbRol.getSelectionModel().select(user.getIdRol() - 1);

        if (user.getFotoPerfil() != null) {
            try {
                Image image = new Image(user.getFotoPerfil().getBinaryStream());
                imgUser.setImage(image);
            } catch (Exception e) {
                System.err.println("Error al cargar la imagen del usuario: " + e.getMessage());
            }
        }
    }

}

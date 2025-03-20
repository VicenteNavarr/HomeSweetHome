/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package homeSweetHome.controller;

import homeSweetHome.dataPersistence.UserDAO;
import homeSweetHome.model.User;
import homeSweetHome.utils.ImageUtils;
import java.io.File;
import java.net.URL;
import java.sql.Blob;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class de vista Usuario
 *
 * @author Usuario
 */
public class CurrentUserSettingsViewController implements Initializable {

    @FXML
    private TextField fieldName;
    @FXML
    private TextField fieldSurname;
    @FXML
    private TextField fieldMail;
    @FXML
    private TextField fieldPassword;
    private ComboBox<?> cmbRol; // ScrollPane que envuelve el contenedor
    @FXML
    private ImageView imgUser;
    @FXML
    private Button btnLoadImage;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnCancel;

    private UserViewController userViewController;

    private User currentUser; // Variable para almacenar el usuario actual

    public void setUserViewController(UserViewController userViewController) {
        this.userViewController = userViewController;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void loadImage(ActionEvent event) {

        // Abrir el selector de archivos
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.jpeg")); // Determinar formatos permitidos

        File file = fileChooser.showOpenDialog(btnLoadImage.getScene().getWindow());

        if (file != null) {
            try {
                // Crear un objeto Image a partir del archivo seleccionado
                Image image = new Image(file.toURI().toString());

                // Establecer la imagen en el ImageView
                imgUser.setImage(image);

                // Convertir la imagen a Blob para guardarla en currentUser
                Blob fotoPerfil = new javax.sql.rowset.serial.SerialBlob(ImageUtils.convertImageToBlob(image));
                currentUser.setFotoPerfil(fotoPerfil); // Actualizar la imagen en currentUser

                System.out.println("Imagen cargada correctamente: " + file.getName());
            } catch (Exception e) {
                System.err.println("Error al cargar la imagen: " + e.getMessage());
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar la imagen seleccionada. Inténtalo de nuevo.");
            }
        } else {
            // Si no se seleccionó una imagen, usar la imagen predeterminada
            System.out.println("No se seleccionó ningún archivo. Usando imagen predeterminada.");
            imgUser.setImage(new Image(getClass().getResourceAsStream("/images/default-profile.png"))); // Imagen predeterminada

            try {
                // Convertir la imagen predeterminada a Blob y guardarla en currentUser
                Blob defaultFotoPerfil = new javax.sql.rowset.serial.SerialBlob(ImageUtils.convertImageToBlob(
                        new Image(getClass().getResourceAsStream("/images/add-image.png"))
                ));
                currentUser.setFotoPerfil(defaultFotoPerfil);
            } catch (Exception e) {
                System.err.println("Error al cargar la imagen predeterminada: " + e.getMessage());
            }
        }
    }

    /**
     * Guarda las modificaciones hechas en el usuario seleccionado(por id
     * usuario)
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

        if (nombre.isEmpty() || apellidos.isEmpty() || correoElectronico.isEmpty() || contrasenia.isEmpty() || imgUser.getImage() == null) {
            showAlert(Alert.AlertType.WARNING, "Campos incompletos", "Por favor, completa todos los campos e incluye una imagen.");
            return;
        }

        // Convertir imagen a Blob
        Blob fotoPerfil;
        try {
            fotoPerfil = new javax.sql.rowset.serial.SerialBlob(ImageUtils.convertImageToBlob(imgUser.getImage()));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error en imagen", "No se pudo procesar la imagen.");
            return;
        }

        // Actualizar los datos del usuario actual
        currentUser.setNombre(nombre);
        currentUser.setApellidos(apellidos);
        currentUser.setCorreoElectronico(correoElectronico);
        currentUser.setContrasenia(contrasenia);

        // Establecer idRol directamente como Administrador (ejemplo: 1 para Administrador)
        currentUser.setIdRol(1);
        currentUser.setFotoPerfil(fotoPerfil);

        // Guardar cambios en la base de datos
        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.updateUser(currentUser);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Los datos del usuario han sido actualizados correctamente.");

        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudieron guardar los cambios. Verifica los datos e inténtalo nuevamente.");
        }
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
     * Metodo que recibe los datos del current usuario desde la bbdd
     * seleccionado y los setea en la vista. Los datos los toma de la bbdd
     * directamente
     *
     *
     * @param user - ID del usuario actual
     */
    public void setUserData(int userId) {

        UserDAO userDAO = new UserDAO(); // Crear una instancia de UserDAO
        User user = userDAO.getUserById(userId); // Llamar al método para obtener el usuario

        if (user != null) {
            this.currentUser = user; // Guardar el usuario en la variable currentUser

            // Configurar los campos de texto con los datos del usuario
            fieldName.setText(user.getNombre());
            fieldSurname.setText(user.getApellidos());
            fieldMail.setText(user.getCorreoElectronico());
            fieldPassword.setText(user.getContrasenia());
            //cmbRol.getSelectionModel().select(user.getIdRol() - 1);

            // Cargar la imagen del usuario en el ImageView
            if (user.getFotoPerfil() != null) {
                try {
                    Image image = new Image(user.getFotoPerfil().getBinaryStream());
                    imgUser.setImage(image); // Mostrar la imagen en el componente
                } catch (Exception e) {
                    System.err.println("Error al cargar la imagen del usuario: " + e.getMessage());
                }
            } else {
                imgUser.setImage(null); // O establece una imagen predeterminada si es necesario
            }
        } else {
            System.err.println("El usuario con ID " + userId + " no fue encontrado en la base de datos.");
        }

    }
}

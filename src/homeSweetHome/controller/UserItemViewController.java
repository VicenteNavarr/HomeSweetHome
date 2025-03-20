package homeSweetHome.controller;

import homeSweetHome.dataPersistence.UserDAO;
import homeSweetHome.model.User;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UserItemViewController {

    @FXML
    private ImageView userImage; // Imagen del usuario
    @FXML
    private Label lblUserName; // Nombre del usuario
    @FXML
    private Label lblRol; // Rol del usuario
    @FXML
    private Button btnUpdate; // Botón para modificar
    @FXML
    private Button btnDelete; // Botón para eliminar

    private User user; // Referencia al usuario actual

    private UserViewController userViewController;

    public void setUserViewController(UserViewController userViewController) {

        this.userViewController = userViewController;
    }

    /**
     * Metodo que setea los datos de la vista para mostrar usuario
     *
     * @param user - User
     */
    public void setUserData(User user) {

        this.user = user;

        // Configurar el nombre del usuario
        lblUserName.setText(user.getNombre());
        // Configurar el rol del usuario
        lblRol.setText(user.getNombreRol());

        // Intentar cargar la imagen de perfil
        if (user.getFotoPerfil() != null) {

            try {

                InputStream inputStream = user.getFotoPerfil().getBinaryStream();
                System.out.println("Cargando imagen para el usuario: " + user.getNombre());
                Image userImg = new Image(inputStream);
                userImage.setImage(userImg); // Mostrar la imagen

            } catch (Exception e) {

                System.err.println("Error al cargar la imagen de perfil: " + e.getMessage());
                userImage.setImage(new Image(getClass().getResourceAsStream("/images/add-image.png")));
            }

        } else {

            System.out.println("Sin imagen de perfil para el usuario: " + user.getNombre());
            userImage.setImage(new Image(getClass().getResourceAsStream("/images/add-image.png")));
        }

        // Aplicar el recorte circular a la imagen
        setClipToCircle(userImage);
    }

    /**
     * Metodo para darle forma circular a las imagenes
     *
     * @param imageView - ImageView
     */
    private void setClipToCircle(ImageView imageView) {

        javafx.scene.shape.Circle clip = new javafx.scene.shape.Circle();
        clip.setRadius(Math.min(imageView.getFitWidth(), imageView.getFitHeight()) / 2); // Radio del círculo
        clip.setCenterX(imageView.getFitWidth() / 2); // Centrar en X
        clip.setCenterY(imageView.getFitHeight() / 2); // Centrar en Y
        imageView.setClip(clip); // Aplicar el clip al ImageView
    }

    /**
     * Metodo para modificar datos de usuario abre la vistaupdateuserview y pasa
     * comunica con ella los datos
     *
     * @param event
     */
    @FXML
    private void updateUser(ActionEvent event) {
        try {
            UserDAO userDAO = new UserDAO();
            // Recuperar datos completos del usuario por su ID
            User updatedUser = userDAO.getUserById(user.getId());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homeSweetHome/view/UpdateUserView.fxml"));
            Parent root = loader.load();

            // Obtener el controlador asociado a la vista UpdateUserView
            UpdateUserViewController updateUserController = loader.getController();

            // Pasar el controlador principal y el usuario actualizado
            if (userViewController != null) {
                updateUserController.setUserViewController(userViewController);
            }
            updateUserController.setUserData(updatedUser);

            // Crear un Stage (ventana) para el popup
            Stage stage = new Stage();
            stage.setTitle("Actualizar Usuario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnUpdate.getScene().getWindow());

            // Mostrar la ventana y esperar a que se cierre
            stage.showAndWait();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista UpdateUserView: " + e.getMessage());
        }
    }

    /**
     * Metodo para eliminar usuario
     *
     * @param event
     */
    @FXML
    private void deleteUser(ActionEvent event) {

        if (user == null) {

            System.out.println("Error: No hay usuario seleccionado para eliminar.");
            return;
        }

        // Confirmar la eliminación del usuario 
        System.out.println("Eliminando usuario: " + user.getNombre());

        // Llamar al DAO para eliminar el usuario
        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.deleteUserById(user.getId());

        if (success) {

            System.out.println("Usuario eliminado exitosamente.");

            // Refrescar la lista en el UserViewController() para ver los cambios en tiempo real)
            if (userViewController != null) {

                System.out.println("Llamando a loadUsers desde deleteUser...");
                userViewController.loadUsers();

            } else {

                System.out.println("Error: userViewController es null.");
            }

        } else {

            System.out.println("Error al eliminar el usuario.");
        }
    }

}

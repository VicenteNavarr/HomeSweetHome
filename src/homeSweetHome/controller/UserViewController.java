/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package homeSweetHome.controller;

import homeSweetHome.dataPersistence.UserDAO;
import homeSweetHome.model.User;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class de vista Usuario
 *
 * @author Usuario
 */
public class UserViewController implements Initializable {

    @FXML
    private VBox UserContainer;
    @FXML
    private Button btnCreateUser;
    @FXML
    private Button btnUpdateUser;
    @FXML
    private Button btnDeleteUser;
    @FXML
    private ScrollPane scrollPane; // ScrollPane que envuelve el contenedor

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadUsers();//Carga el metodo de listar usuarios y mostrar en el scrollpane
    }

    
    /**
     * Metodo para crear un nuevo usuario - abre el pop up CreateUserView
     * 
     * @param event 
     */
    @FXML
    private void createUser(ActionEvent event) {

        try {
            
            // Cargar el archivo FXML para la vista CreateUserView
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homeSweetHome/view/CreateUserView.fxml"));
            Parent root = loader.load();

            // Obtén el controlador asociado a la ventana CreateUserView
            CreateUserViewController createUserController = loader.getController();

            // Pasa la referencia del controlador actual (UserViewController)
            //Sireve para que funcione el metodo load()
            createUserController.setUserViewController(this);

            // Crear un nuevo Stage (ventana)
            Stage stage = new Stage();
            stage.setTitle("Crear Usuario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL); // Ventana modal
            stage.initOwner(btnCreateUser.getScene().getWindow()); // Establece el dueño de la ventana

            // Mostrar el popup
            stage.showAndWait();
            
        } catch (IOException e) {
            
            System.err.println("Error al cargar la vista CreateUserView: " + e.getMessage());
        }
    }

    
    /**
     * Metodo para cargar la lista de usuarios por id de grupo(toma de DAO)
     */
    public void loadUsers() {
        
    UserDAO userDAO = new UserDAO();

    // Obtener todos los usuarios del grupo actual
    int userGroupId = homeSweetHome.dataPersistence.CurrentSession.getInstance().getUserGroupId();
    List<User> users = userDAO.getUsersByGroup(userGroupId);

    // Obtener el ID del usuario actual
    int currentUserId = homeSweetHome.dataPersistence.CurrentSession.getInstance().getUserId();

    UserContainer.getChildren().clear(); // Limpia el contenedor antes de poblarlo

    //Recorremos la lista usuarios sacada de la bbdd
    for (User user : users) {
        
        // Excluir al usuario actual(no queremos que se muestre a si mismo)
        if (user.getId() == currentUserId) {
            
            continue;
        }

        try {
            
            // Cargar el archivo FXML de UserItemView(esta vista muestra usuario)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homeSweetHome/view/UserItemView.fxml"));
            Node userItemNode = loader.load();

            // Pasar los datos al controlador de UserItemView
            UserItemViewController controller = loader.getController();
            controller.setUserData(user);

            // Pasa la referencia del controlador principal
            controller.setUserViewController(this);

            // Añadir el nodo al contenedor
            UserContainer.getChildren().add(userItemNode);

        } catch (IOException e) {
            
            e.printStackTrace();
        }
    }
}


}

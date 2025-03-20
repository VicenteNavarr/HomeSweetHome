/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package homeSweetHome.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class TaskViewController implements Initializable {

    @FXML
    private Button btnNuevaTarea;
    @FXML
    private ScrollPane scrollPane; // Contenedor con scroll
    @FXML
    private VBox taskContainer; // Contenedor de las tareas dinámicas

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Ajusta el ScrollPane para que el VBox se ajuste al ancho del ScrollPane
        scrollPane.setFitToWidth(true);
    }

    /**
     * Maneja el evento del botón para añadir nueva tarea.
     *
     * @param event
     */
    @FXML
    private void nuevaTarea(ActionEvent event) {
        try {
            // Carga la vista del ítem de tarea desde el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homeSweetHome/view/TaskItemView.fxml"));
            Parent taskEntryNode = loader.load();

            // Configura el ítem si es necesario
            TaskItemViewController controller = loader.getController();
            controller.setNombreTarea("Nueva Tarea " + (taskContainer.getChildren().size() + 1)); // Nombre único

            // Añade el nodo de la tarea al VBox
            taskContainer.getChildren().add(taskEntryNode);

            // Ajusta el scroll para desplazarse automáticamente al final
            scrollPane.setVvalue(1.0);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("No se pudo cargar el ítem de tarea.");
        }
    }
}

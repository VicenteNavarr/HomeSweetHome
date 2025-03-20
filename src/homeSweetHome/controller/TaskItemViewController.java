/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package homeSweetHome.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class for TaskEntryView
 */
public class TaskItemViewController implements Initializable {

    @FXML
    private Label lblNombreTarea;
    @FXML
    private Label lblFechaLimite;
    @FXML
    private Button btnCompletada;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;

    // Método para inicializar la vista
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configuración inicial si es necesaria
    }

    /**
     * Método para asignar el nombre de la tarea al Label.
     *
     * @param nombreTarea Nombre de la tarea
     */
    public void setNombreTarea(String nombreTarea) {
        lblNombreTarea.setText(nombreTarea);
    }

    /**
     * Método para asignar la fecha límite al Label.
     *
     * @param fecha Fecha límite de la tarea
     */
    public void setFechaLimite(String fecha) {
        lblFechaLimite.setText("Fecha límite: " + fecha);
    }

    // Método para manejar el botón "Completado"
    @FXML
    private void marcarCompletada(ActionEvent event) {
        lblNombreTarea.setStyle("-fx-text-fill: green; -fx-strikethrough: true;"); // Cambia el estilo del texto
        System.out.println("Tarea marcada como completada: " + lblNombreTarea.getText());
        // Aquí puedes añadir lógica para actualizar el estado en la base de datos
    }

    // Método para manejar el botón "Modificar"
    @FXML
    private void modificarTarea(ActionEvent event) {
        System.out.println("Modificar tarea: " + lblNombreTarea.getText());
        // Aquí puedes abrir una ventana emergente o formulario para editar la tarea
    }

    // Método para manejar el botón "Eliminar"
    @FXML
    private void eliminarTarea(ActionEvent event) {
        System.out.println("Tarea eliminada: " + lblNombreTarea.getText());
        // Aquí puedes añadir lógica para eliminar la tarea del contenedor y la base de datos
    }
}

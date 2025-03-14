/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package homeSweetHome.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class MainViewController implements Initializable {

    @FXML
    private AnchorPane viewContainer;
    @FXML
    private BorderPane rootPane;
    @FXML
    private Button btnLoadControlPanelView;
    @FXML
    private Button btnLoadUserView;
    @FXML
    private Button btnLoadMealView;
    @FXML
    private Button btnLoadPurchaseView;
    @FXML
    private Button btnLoadTaskView;
    @FXML
    private Button btnLoadEventView;
    @FXML
    private Button btnLoadBudgetView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

///////////////CARGA DE VISTAS -> ACCIÓN BOTONES/////////////////////////////////////////////
    
    
    
    
    /**
     * Método llamado cuando se hace clic en el botón "btnLoadControlPanelView". 
     *Carga y muestra la vista ControlPanelView.fxml en el centro del BorderPane.
     *
     * @param event 
     */
    @FXML
    private void LoadControlPanelView(ActionEvent event) {
        
         try {

            AnchorPane root = FXMLLoader.load(getClass().getResource("/homeSweetHome/view/ControlPanelView.fxml"));
            viewContainer.getChildren().setAll(root);

            // Establece la vista cargada en el centro del BorderPane
            rootPane.setCenter(root);

        } catch (IOException ex) {

            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);

        }
        
    }

    /**
     * Método llamado cuando se hace clic en el botón "btnLoadUserView". 
     * Carga y muestra la vista UserView.fxml en el centro del BorderPane.
     *
     * @param event
     *
     */
    @FXML
    private void loadUserView(ActionEvent event) {

        try {

            AnchorPane root = FXMLLoader.load(getClass().getResource("/homeSweetHome/view/UserView.fxml"));
            viewContainer.getChildren().setAll(root);

            // Establece la vista cargada en el centro del BorderPane
            rootPane.setCenter(root);

        } catch (IOException ex) {

            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    /**
     * Método llamado cuando se hace clic en el botón "btnLoadMealView".
     * Carga y muestra la vista MealView.fxml en el centro del BorderPane.
     *
     * @param event
     */
    @FXML
    private void LoadMealView(ActionEvent event) {

        try {

            AnchorPane root = FXMLLoader.load(getClass().getResource("/homeSweetHome/view/MealView.fxml"));
            viewContainer.getChildren().setAll(root);

            // Establece la vista cargada en el centro del BorderPane
            rootPane.setCenter(root);

        } catch (IOException ex) {

            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    /**
     * Método llamado cuando se hace clic en el botón "btnLoadPurchaseView". 
     * Carga y muestra la vista PurchaseView.fxml en el centro del BorderPane.
     *
     * @param event
     */
    @FXML
    private void LoadPurchaseView(ActionEvent event) {

        try {

            AnchorPane root = FXMLLoader.load(getClass().getResource("/homeSweetHome/view/PurchaseView.fxml"));
            viewContainer.getChildren().setAll(root);

            // Establece la vista cargada en el centro del BorderPane
            rootPane.setCenter(root);

        } catch (IOException ex) {

            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    
    /**
     * Método llamado cuando se hace clic en el botón "btnLoadTaskView". 
     * Carga y muestra la vista TaskView.fxml en el centro del BorderPane.
     * 
     * @param event 
     */
    @FXML
    private void LoadTaskView(ActionEvent event) {
        
         try {

            AnchorPane root = FXMLLoader.load(getClass().getResource("/homeSweetHome/view/TaskView.fxml"));
            viewContainer.getChildren().setAll(root);

            // Establece la vista cargada en el centro del BorderPane
            rootPane.setCenter(root);

        } catch (IOException ex) {

            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);

        }
        
    }

    
    /**
     * Método llamado cuando se hace clic en el botón "btnLoadEventView". 
     * Carga y muestra la vista EventView.fxml en el centro del BorderPane.
     * 
     * @param event 
     */
    @FXML
    private void LoadEventView(ActionEvent event) {
        
        try {

            AnchorPane root = FXMLLoader.load(getClass().getResource("/homeSweetHome/view/EventView.fxml"));
            viewContainer.getChildren().setAll(root);

            // Establece la vista cargada en el centro del BorderPane
            rootPane.setCenter(root);

        } catch (IOException ex) {

            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);

        }
        
    }

    
    /**
     * Método llamado cuando se hace clic en el botón "btnLoadBudgetView". Carga y
     * muestra la vista BudgetView.fxml en el centro del BorderPane.
     *  
     * @param event 
     */
    @FXML
    private void LoadBudgetView(ActionEvent event) {
        
         try {

            AnchorPane root = FXMLLoader.load(getClass().getResource("/homeSweetHome/view/BudgetView.fxml"));
            viewContainer.getChildren().setAll(root);

            // Establece la vista cargada en el centro del BorderPane
            rootPane.setCenter(root);

        } catch (IOException ex) {

            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);

        }
        
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homeSweetHome;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Usuario
 */
public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("/homeSweetHome/view/PanelDeControl.fxml"));
        
        primaryStage.setTitle("Prueba");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
    
    
    public static void main(String [] args){
    
        launch(args);
    
    }
    
}

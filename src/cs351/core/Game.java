package cs351.core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/*
 *   Game Class - contains main entry point
 *   here we direct traffic for the rest of
 *   Zombie House
 */
public class Game extends Application{

  public static void main(String[] args) {
    Application.launch(Game.class, args);
  }
/*
 * Implementing required method for extending Application
 * @see javafx.application.Application#start(javafx.stage.Stage)
 * 
 * Load JavaFX Scene and show
 */
  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("this_doesnt_exist_yet.fxml"));
    stage.setTitle("Zombie House");
    stage.setScene(new Scene(root, 900, 750));
    stage.show();
  }
}

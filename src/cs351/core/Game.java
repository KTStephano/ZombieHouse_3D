package cs351.core;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/*
 *   Game Class - contains main entry point
 *   here we direct traffic for the rest of
 *   Zombie House
 */
public class Game extends Application{

  @FXML private Button playButton;
  @FXML private  AnchorPane ZombieHouseAnchorPane; 
  @FXML private ScrollPane ZombieHouseScrollPane;
  @FXML private Text gameTitle;
  @FXML private Text gameTitle2;
  private boolean started;


  //play button handler - run continually (Until Pause)
  @FXML protected void handlePlay(ActionEvent event)  {

    started = !started;
    if (started)
    {
      playButton.setText("Pause");
    } else
    {
      playButton.setText("Play");

    }
  }  

  // clear 
  @FXML protected void handleClear(ActionEvent event) {
    // TODO  -- handle this
  }


  // Quit handler
  @FXML protected void handleSubmitButtonAction(ActionEvent event) {
    System.exit(0);
  }  



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
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ZombieHouse.fxml"));
    stage.setTitle("Zombie House");
    stage.setScene(new Scene(root, 900, 750));
    initGameLoop();
    stage.show();
  }


  private void initGameLoop()
  {

    /*
     * AnimationTimer() - loops forever
     * @param started
     */
    new AnimationTimer() {
      @Override
      public void handle(long now) {

        if (started)
        {

          // all the updates at 60fps
          // this is one frame

        }

      }
    }.start();
  }
}









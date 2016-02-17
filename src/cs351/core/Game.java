package cs351.core;

import cs351.project1.NotTheRealWorld;
import cs351.project1.ZombieHouseEngine;
import cs351.project1.ZombieHouseRenderer;
import cs351.project1.ZombieHouseSoundEngine;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;

/*
 *   Game Class - contains main entry point
 *   here we direct traffic for the rest of
 *   Zombie House
 */
public class Game extends Application {


  @FXML private Button playButton;
  @FXML private  AnchorPane ZombieHouseAnchorPane;
  @FXML private StackPane zombieHouseScrollPane;
  @FXML private Text gameTitle;
  @FXML private Text gameTitle2;
  private AnimationTimer timer = new MyTimer();
  private boolean started=true;
  private ZombieHouseRenderer renderer;
  private static Engine engine = new ZombieHouseEngine();


  //play button handler - run continually (Until Pause)
  @FXML protected void handlePlay(ActionEvent event)  {
    engine.togglePause(started); // lets the engine know what's going on
    started = !started;
    if (started)
    {
      timer.start();
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


  private void initEngine(Stage stage)
  {
    engine.init(stage, new NotTheRealWorld(), new ZombieHouseSoundEngine(), renderer);
  }

  private void initGameLoop()
  {
    timer.start();
  }



  @Override
  public void start(Stage stage) throws IOException {
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ZombieHouse.fxml"));
    stage.setTitle("Zombie House");
    stage.setScene(new Scene(root, 900, 750));
    stage.show();
    for (Node node : root.getChildrenUnmodifiable())
    {
      if (node instanceof StackPane)
      {
        StackPane pane = (StackPane)node;
        renderer = new ZombieHouseRenderer(stage, pane, (int)pane.getWidth(), (int)pane.getHeight());
      }
    }

    // if the above for loop does not find a StackPane, throw an exception
    if (renderer == null) throw new RuntimeException("Could not find a StackPane for the renderer to use");

    initEngine(stage);
    initGameLoop();
  }


  private class MyTimer extends AnimationTimer {
    private int timerCt = 300;
    private ZombieHouseSoundEngine sounds = new ZombieHouseSoundEngine();

    // private Point pt = new Point();
    // sounds.setCentralPoint(pt);
    @Override
    public void handle(long now)
    {
      engine.frame();
    }
  }


  public static void main(String[] args) {
    launch(args);
  }
}
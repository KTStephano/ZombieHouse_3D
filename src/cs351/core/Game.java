package cs351.core;

import cs351.entities.Player;
import cs351.entities.Zombie;
import cs351.project1.NotTheRealEngine;
import cs351.project1.ZombieHouseRenderer;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.Random;

/*
 *   Game Class - contains main entry point
 *   here we direct traffic for the rest of
 *   Zombie House
 */
public class Game extends Application{

  @FXML private Button playButton;
  @FXML private  AnchorPane ZombieHouseAnchorPane; 
  @FXML private StackPane zombieHouseScrollPane;
  @FXML private Text gameTitle;
  @FXML private Text gameTitle2;
  private boolean started;
  private ZombieHouseRenderer renderer;
  private Engine pretendEngine = new NotTheRealEngine();
  private LinkedList<Actor> actors = new LinkedList<Actor>();


  //play button handler - run continually (Until Pause)
  @FXML protected void handlePlay(ActionEvent event)  {

    started = !started;
    System.out.println(started);
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
    initPlayer();
    initZombies();
    initGameLoop();
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

        started = true; // TODO make this work - had to add this so the renderer would run
        if (started)
        {
          // all the updates at 60fps
          // this is one frame
          renderer.render(DrawMode.FILL);
          for (Actor actor : actors) actor.update(pretendEngine, 0.0);
        }

      }
    }.start();
  }

  private void initPlayer()
  {
    Player player = new Player(100.0, 0.0);
    renderer.registerPlayer(player, 90.0);
    actors.add(player);
  }

  private void initZombies()
  {
    Random rand = new Random();
    Color[] colors = { Color.RED, Color.ORANGE, Color.BLACK, Color.BLUE, Color.BEIGE, Color.AZURE, Color.BROWN };
    int currColor = 0;

    for (int i = 0; i < 100; i++)
    {
      Zombie wall = new Zombie(rand.nextInt(100), rand.nextInt(100), 5, 5, 5);
      renderer.registerActor(wall, new Box(wall.getWidth(), wall.getHeight(), wall.getDepth()),
              colors[currColor], colors[currColor], Color.WHITE);
      currColor++;
      if (currColor >= colors.length) currColor = 0;
      actors.add(wall);
    }
  }
}









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
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;


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
  private AnimationTimer timer;
  private volatile boolean started=true;
  private ZombieHouseRenderer renderer;
  private Engine pretendEngine = new NotTheRealEngine();
  private LinkedList<Actor> actors = new LinkedList<Actor>();


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



  private void initGameLoop()
  {
    timer = new MyTimer();
    timer.start();
  }


  private void initPlayer()
  {
    Player player = new Player(100.0, 0.0);
    renderer.registerPlayer(player, 90.0);
    actors.add(player);
  }

  //TODO LOOK HERE FOR REFERENCE
  
  private void initZombies()
  {
    Random rand = new Random();
    Color[] colors = { Color.BEIGE };
    String[] textures = { "textures/block_texture_dark.jpg", "textures/brick_texture.jpg", "textures/brick_texture2.jpg",
            "textures/crate_texture.jpg", "textures/metal_texture.jpg", "textures/rock_texture.jpg",
            "textures/ice_texture.jpg", "textures/stone_texture.jpg" };
    int currColor = 0;
    int currTexture = 0;

    int numZombies = 100;
    for (int i = 0; i < numZombies; i++)
    {
      Zombie wall = new Zombie(rand.nextInt(numZombies), rand.nextInt(numZombies), 5, 5, 5);
      // register the actor with the renderer
      renderer.registerActor(wall, new Box(wall.getWidth(), wall.getHeight(), wall.getDepth()),
              colors[currColor], colors[currColor], Color.WHITE);
      // associate the texture with the actor
      renderer.associateDiffuseTextureWithActor(wall, textures[currTexture]);
      currColor++;
      currTexture++;
      if (currColor >= colors.length) currColor = 0;
      if (currTexture >= textures.length) currTexture = 0;
      actors.add(wall);
    }
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


    initPlayer();
    initZombies();
    initGameLoop();
  }



  private class MyTimer extends AnimationTimer {

    @Override
    public void handle(long now) {

      doHandle();
    }

    private void doHandle() {


      if (!started)
      {
        stop();
        System.out.println("Animation stopped");
      } else
      {
        renderer.render(DrawMode.FILL);
        for (Actor actor : actors) actor.update(pretendEngine, 0.0);
      }
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
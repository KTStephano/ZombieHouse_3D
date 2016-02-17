package cs351.project1;

import cs351.core.Actor;
import cs351.core.Engine;
import cs351.core.Game;
import cs351.entities.Player;
import cs351.entities.Zombie;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.Random;

/**
 * Basic class with main and stuff.
 *
 * EDITED: Now all this does is create a Game object and call its start method.
 */
public class RendererTest extends Application
{
  private ZombieHouseRenderer renderer;
  // real Game class won't have this
  private LinkedList<Actor> actors = new LinkedList<Actor>();
  private Engine pretendEngine = new NotTheRealEngine(); // need this to update the actors

  @Override
  public void start(Stage primaryStage)
  {
    try
    {
      new Game().start(primaryStage);
    }
    catch (Exception e)
    {
      System.exit(-1);
    }
    /**
    int widthHeight = 700;
    renderer = new ZombieHouseRenderer(primaryStage, widthHeight, widthHeight);
    initPlayer();
    initZombies();
    primaryStage.show();

    // stole this from Scott's code
    new AnimationTimer()
    {
      @Override
      public void handle(long now)
      {
        // you can switch this from DrawMode.FILL to DrawMode.LINE so that it
        // draws wire-frame objects
        renderer.render(DrawMode.FILL);
        for (Actor actor : actors) actor.update(pretendEngine, 0.0);
      }
    }.start();
     */
  }

  /**
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
   */

  public static void main(String[] args)
  {
    launch(args);
  }
}

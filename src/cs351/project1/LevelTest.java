package cs351.project1;


import cs351.core.Game;
import cs351.core.World;
import javafx.application.Application;
import javafx.stage.Stage;

import cs351.core.Actor;
import cs351.core.Engine;
import cs351.entities.Player;
import cs351.entities.Zombie;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;

import java.util.LinkedList;
import java.util.Random;

//All of this will go into Game.java, LevelTest.java is only to create an environment

public class LevelTest extends Application
{
  
  private ZombieHouseRenderer renderer;
  // real Game class won't have this
  private LinkedList<Actor> actors = new LinkedList<Actor>();
  private Engine engine = new ZombieHouseEngine(); // need this to update the actors
  private World world;

  @Override
  public void start(Stage primaryStage) throws Exception
  {
    // TODO Auto-generated method stub
    try
    {
     
      // System.out.println("level test");
      new Game().start(primaryStage);
    }
    catch (Exception e)
    {
      System.exit(-1);
    }
    
    
    int widthHeight = 700;
    renderer = new ZombieHouseRenderer(primaryStage, widthHeight, widthHeight);

    /****************/
      initWorld();
    /***************/
    
    initPlayer();
    initZombies();
    primaryStage.show();

    // stole this from Scott's code
    new AnimationTimer()
    {
      @Override
      public void handle(long now)
      {
        engine.frame(); // run the next frame
      }
    }.start();

  
  }

  //TODO THIS INITIALIZES THE WORLD
  private void initWorld()
  {
    //calls constructor of Zombieworld
   // ZombieLevel l = new ZombieLevel(2, 3, 3, 2);
    ZombieLevel z = new ZombieLevel(0, 0, 0, 0);
  //  z.initWorld(world);
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
  
    
    
    
  public static void main(String[] args)
  {
    launch(args);
  }
}

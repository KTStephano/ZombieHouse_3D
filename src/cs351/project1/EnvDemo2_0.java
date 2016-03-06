/*
 =======================================
 Game.java line 70 must be changed to 
 EnvDemo2_0.java to use this class
 =======================================
 */

package cs351.project1;

import cs351.core.Actor;
import cs351.core.Engine;
import cs351.core.Level;
import cs351.core.World;
import cs351.entities.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

public class EnvDemo2_0 implements World
{
  private Random rand = new Random();
  private HashSet<Actor> actors = new HashSet<>(250);
  private int worldPixelWidth = 100;
  private int worldPixelHeight = 100;
  private int tileWidthHeight = 1; // measured in pixels
  private Actor player;
  
  int index = 0;
  
  
  /* ===============MY STUFF TODO===================================*/
   
  private int [][] testArray = new int [100][100];
 
  /*======================================================*/
  @Override
  public boolean contains(Actor actor)
  {
    return actors.contains(actor);
  }

  @Override
  public void remove(Actor actor) throws RuntimeException
  {
    throw new RuntimeException("I'm not a real world so most of my stuff doesn't work");
  }

  @Override
  public void add(Actor actor)
  {
    actors.add(actor);
  }

  @Override
  public void add(Level level)
  {
    throw new RuntimeException("I'm not a real world so most of my stuff doesn't work");
  }

  @Override
  public int getWorldPixelWidth()
  {
    return worldPixelWidth;
  }

  @Override
  public int getWorldPixelHeight()
  {
    return worldPixelHeight;
  }

  @Override
  public void setPixelWidthHeight(int pixelWidth, int pixelHeight)
  {
    throw new RuntimeException("I'm not a real world so most of my stuff doesn't work");
  }

  @Override
  public int getTilePixelWidth()
  {
    return tileWidthHeight;
  }

  @Override
  public int getTilePixelHeight()
  {
    return tileWidthHeight;
  }

  @Override
  public void setTilePixelWidthHeight(int pixelWidth, int pixelHeight)
  {
    throw new RuntimeException("I'm not a real world so most of my stuff doesn't work");
  }

  @Override
  public Actor getPlayer()
  {
    return player;
  }

  @Override
  public void setPlayer(Actor player) throws RuntimeException
  {
    throw new RuntimeException("I'm not a real world so most of my stuff doesn't work");
  }

  @Override
  public Collection<Actor> getChangeList(boolean clearChangeList)
  {
    if (clearChangeList)
    {
      HashSet<Actor> retVal = new HashSet<>(actors);
      actors.clear(); // delete my only actor list :( not a real world though so it's ok
      return retVal;
    }
    return actors;
  }

  @Override
  public void initializeLevels(int numberOfLevels)
  {
    throw new RuntimeException("I'm not a real world so most of my stuff doesn't work");
  }

  @Override
  public boolean hasNextLevel()
  {
    return true; // does the same thing every time
  }

  @Override
  public void nextLevel(Engine engine)
  {
    System.out.println("nextLevel is called");
    actors.clear();
    //initPlayer();
    
    /* ========================================================= */
    ProceduralRoomTestThingy thingy = new ProceduralRoomTestThingy();
    
    thingy.initializeBoard();  //TODO ADDED THIS STUFF HERE
    testArray = thingy.getArray();
    
    /*========================================================== */

    initWallsFloorAndCeiling();
    initZombies();
    initPlayASoundThingy(); // can't forget about this

    for (Actor actor : actors)
    {
      // register the actor with the renderer so it can render it each frame
      if (actor.getRenderEntity() != null)
      {
        engine.getRenderer().registerActor(actor,
                                           actor.getRenderEntity(),
                                           Color.BEIGE, // diffuse
                                           Color.BEIGE, // specular
                                           Color.WHITE); // ambient
      }
      else
      {
        engine.getRenderer().registerActor(actor,
                                           new Box(actor.getWidth(), actor.getHeight(), actor.getDepth()),
                                           Color.BEIGE, // diffuse
                                           Color.BEIGE, // specular
                                           Color.WHITE); // ambient
      }

      // sets the actor's texture so the renderer knows to load it and use it
      engine.getRenderer().mapTextureToActor(actor.getTexture(), actor);
    }
  }

  @Override
  public void restartLevel(Engine engine)
  {
    throw new RuntimeException("I'm not a real world so most of my stuff doesn't work");
  }

  private void initPlayer(double x, double y)
  {
    // the getWorldPixelWidth() / 2.0 and getWorldPixelHeight() / 2.0 make it so the player is
    // in the middle of the map
    //
    // the 2 * tileWidthHeight is because each tile is 10 pixels and I want
    // the player to be 2 tiles high
    //player = new Player(getWorldPixelWidth() / 2.0, getWorldPixelHeight() / 2.0, 2 * tileWidthHeight);
    player = new Player(x, y, 3 * tileWidthHeight);
    actors.add(player);
  }

  private void initWallsFloorAndCeiling()
  {
    // the + 1 is for when the pixelWidth/Height and tilePixelWidth/Height don't divide evenly
    //TODO I TEMPORARILY REMOVED THE + 1 because it was going out of bounds
    //TODO 
    //TODO
    int numTilesWidth = getWorldPixelWidth() / getTilePixelWidth()  ; // convert pixels to number of tiles
    int numTilesHeight = getWorldPixelHeight() / getTilePixelHeight() ; // convert pixels to number of tiles
    Random rand = new Random();


    // TODO remove this when we have a better way to not let the player get stuck in a wall during random generation
    boolean cheapHackSolutionToPlayerGettingBuriedInAWall = true;
    
    
    for (int x = 0; x < numTilesWidth; x++)
    {
      for (int y = 0; y < numTilesHeight; y++)
      {
        if (x == 0 || y == 0 || x == numTilesWidth - 1 || y == numTilesHeight - 1)
        {
          Actor wall = new Wall("textures/block_texture_dark.jpg",
              x * getTilePixelWidth(), // offset - when x = 0, this = 0, when x = 1, this = the tile width in pixels
              y * getTilePixelHeight(), // same as above but for y
              getTilePixelWidth(), // sets the width to be 1 tile
              2 * getTilePixelHeight(), // sets the height to be 2 tiles
              getTilePixelHeight()); // sets the depth to be 1 tile
          actors.add(wall);
        }
      
        /*================================================================================================*/  
          if( (x < 100) && (y < 100)){
           if (testArray[x][y] == 1) 
           {
             if(index < 50)
             {
               Actor wall = new Wall("textures/wall_dirty.jpg",
               x * getTilePixelWidth(), // offset - when x = 0, this = 0, when x = 1, this = the tile width in pixels
               y * getTilePixelHeight(), // same as above but for y
               getTilePixelWidth(), // sets the width to be 1 tile
               2 * getTilePixelHeight(), // sets the height to be 2 tiles
               getTilePixelHeight()); // sets the depth to be 1 tile
               actors.add(wall);
             }
             else
             {
               Actor wall = new Wall("textures/ice_texture.jpg",
                                     x * getTilePixelWidth(), 
                                     y * getTilePixelHeight(), 
                                     getTilePixelWidth(), 
                                     2 * getTilePixelHeight(), 
                                     getTilePixelHeight()); 
               actors.add(wall);
             } index++;
           }
           // TODO remove this when cheapHackSolutionToPlayerGettingBuriedInAWall isn't needed
            else if (cheapHackSolutionToPlayerGettingBuriedInAWall)
           {
             cheapHackSolutionToPlayerGettingBuriedInAWall = false;
             initPlayer(x, y);
           }
          }
        

        /*============================================================================================================*/
         
        //  System.out.print(testArray[x][y]);
         
        FloorCeilingTile floor;
        
//        if (rand.nextInt(50)<10 )
//        {
//        // add a ZombieSpawningFloorTile and ceiling tile
//        floor = new ZombieSpawningFloorTile("textures/red_zombie.jpg",//block_texture_dark.jpg",
//            x * getTilePixelWidth(), // offset - when x = 0, this = 0, when x = 1, this = the tile width in pixels
//            y * getTilePixelHeight(), // same as above but for y
//            getTilePixelWidth(), // sets the width to be 1 tile
//            1, // sets the height to be 1 pixel - this is a good idea to do for all floor and ceiling tiles
//            getTilePixelHeight()); // sets the depth to be 1 tile
//        }
        if(testArray[x][y] == 2){
          floor = new FloorCeilingTile("textures/brick_texture.jpg",
              true, // is part of floor
              false, // is not part of ceiling
              x * getTilePixelWidth(), // offset - when x = 0, this = 0, when x = 1, this = the tile width in pixels
              y * getTilePixelHeight(), // same as above but for y
              getTilePixelWidth(), // sets the width to be 1 tile
              1, // sets the height to be 1 pixel - this is a good idea to do for all floor and ceiling tiles
              getTilePixelHeight()); // sets the depth to be 1 tile
        }
        else if(testArray[x][y] == 3)
        {
        // add a floor and ceiling tile
        floor = new FloorCeilingTile("textures/rock_texture.jpg",
            true, // is part of floor
            false, // is not part of ceiling
            x * getTilePixelWidth(), // offset - when x = 0, this = 0, when x = 1, this = the tile width in pixels
            y * getTilePixelHeight(), // same as above but for y
            getTilePixelWidth(), // sets the width to be 1 tile
            1, // sets the height to be 1 pixel - this is a good idea to do for all floor and ceiling tiles
            getTilePixelHeight()); // sets the depth to be 1 tile
        }
        
        else if(testArray[x][y] == 4)
        {
        // add a floor and ceiling tile
        floor = new FloorCeilingTile("textures/stone_texture.jpg",
            true, // is part of floor
            false, // is not part of ceiling
            x * getTilePixelWidth(), // offset - when x = 0, this = 0, when x = 1, this = the tile width in pixels
            y * getTilePixelHeight(), // same as above but for y
            getTilePixelWidth(), // sets the width to be 1 tile
            1, // sets the height to be 1 pixel - this is a good idea to do for all floor and ceiling tiles
            getTilePixelHeight()); // sets the depth to be 1 tile
        }
        else{
          floor = new FloorCeilingTile("textures/brick_texture2.jpg",
              true, // is part of floor
              false, // is not part of ceiling
              x * getTilePixelWidth(), // offset - when x = 0, this = 0, when x = 1, this = the tile width in pixels
              y * getTilePixelHeight(), // same as above but for y
              getTilePixelWidth(), // sets the width to be 1 tile
              1, // sets the height to be 1 pixel - this is a good idea to do for all floor and ceiling tiles
              getTilePixelHeight()); // sets the depth to be 1 tile
        }
        

        
        
        FloorCeilingTile ceiling = new FloorCeilingTile("textures/block_texture_dark.jpg",
            false, // is not part of floor
            true, // is part of ceiling
            x * getTilePixelWidth(), // offset - when x = 0, this = 0, when x = 1, this = the tile width in pixels
            y * getTilePixelHeight(), // sets the width to be 1 tile
            getTilePixelWidth(), // sets the width to be the whole width of the map (covers the entire space)
            1, // sets the height to be 1 pixel - this is a good idea to do for all floor and ceiling tiles
            getTilePixelHeight()); // sets the depth to be 1 tile

        actors.add(floor);
        actors.add(ceiling);
      }
    }
  }

  private void initZombies()
  {
    int numZombies = 10;
    int currTexture = 0;
    String[] textures = { "textures/block_texture_dark.jpg", "textures/brick_texture.jpg", "textures/brick_texture2.jpg",
        "textures/crate_texture.jpg", "textures/metal_texture.jpg", "textures/rock_texture.jpg",
        "textures/ice_texture.jpg", "textures/stone_texture.jpg" };
    String[] lineWalker = { "textures/zombie.jpg" };
    for (int i = 0; i < numZombies/2; i++)
    {
      Zombie wall1 = new RandomWalkZombie(textures[currTexture],
              "resources/Zombie1_Animated.txt",
          rand.nextInt(getWorldPixelWidth()), // random location (within the world bounds)
          rand.nextInt(getWorldPixelHeight()), // random location (within the world bounds)
          getTilePixelWidth(), // sets width to be 1 tile
          getTilePixelWidth(), // sets height to be 1 tile
          getTilePixelWidth()); // sets depth to be 1 tile
      actors.add(wall1);


      Zombie wall2 = new LineWalkZombie(lineWalker[0],
          rand.nextInt(getWorldPixelWidth()), // random location (within the world bounds)
          rand.nextInt(getWorldPixelHeight()), // random location (within the world bounds)
          getTilePixelWidth(), // sets width to be 1 tile
          getTilePixelWidth(), // sets height to be 1 tile
          getTilePixelWidth()); // sets depth to be 1 tile
      actors.add(wall2);
      currTexture++;
      if (currTexture >= textures.length) currTexture = 0;
    }
  }

  private void initPlayASoundThingy()
  {
    actors.add(new PlayASoundThingy());
  }
  
 
  
  
}
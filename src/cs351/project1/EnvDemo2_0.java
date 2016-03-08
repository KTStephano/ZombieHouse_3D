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

public class EnvDemo2_0 implements World
{
  private HashSet<Actor> actors = new HashSet<>(250);
  private int worldPixelWidth = 50;
  private int worldPixelHeight = 50;
  private int tileWidthHeight = 1; // measured in pixels
  private Actor player;
  
  int index = 0;
  
  
  /* =================================================*/
   
  private int [][] testArray = new int [100][100];
 
  /*==================================================*/
  
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

  public Actor getMasterZombie()
  {
    throw new RuntimeException("I'm not a real world so most of my stuff doesn't work");
  }

  public void setMasterZombie(Actor masterZombie)
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
    
    /* ========================================================= */
    ProceduralRoomTestThingy thingy = new ProceduralRoomTestThingy();
    
    thingy.initializeBoard();  
    testArray = thingy.getArray();
    
    /*========================================================== */

    initWallsFloorAndCeiling();
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
    nextLevel(engine);
    //throw new RuntimeException("I'm not a real world so most of my stuff doesn't work");
  }

  private void initPlayer(double x, double y)
  {
    // the getWorldPixelWidth() / 2.0 and getWorldPixelHeight() / 2.0 make it so the player is
    // in the middle of the map
    //
    // the 2 * tileWidthHeight is because each tile is 10 pixels and I want
    // the player to be 2 tiles high
    player = new Player(x, y, 3 * tileWidthHeight);
    actors.add(player);
  }

  private void initWallsFloorAndCeiling()
  {
    int numTilesWidth = getWorldPixelWidth() / getTilePixelWidth()  ; // convert pixels to number of tiles
    int numTilesHeight = getWorldPixelHeight() / getTilePixelHeight() ; // convert pixels to number of tiles


    boolean cheapHackSolutionToPlayerGettingBuriedInAWall = true;
    
    
    for (int x = 0; x < numTilesWidth; x++)
    {
      for (int y = 0; y < numTilesHeight; y++)
      {
          if( (x < 50) && (y < 50)){
            
           if (testArray[x][y] == 1) 
           {
               Actor wall = new Wall("textures/block_texture_dark.jpg",
                                     x * getTilePixelWidth(), 
                                     y * getTilePixelHeight(), 
                                     getTilePixelWidth(), 
                                     2 * getTilePixelHeight(), 
                                     getTilePixelHeight()); 
               actors.add(wall);
              index++;
           }
           
           if ( testArray[x][y] == 69){
             Actor wall = new Wall("textures/oldbikiniBabe.jpg",
             x * getTilePixelWidth(), // offset - when x = 0, this = 0, when x = 1, this = the tile width in pixels
             y * getTilePixelHeight(), // same as above but for y
             getTilePixelWidth(), // sets the width to be 1 tile
             2 * getTilePixelHeight(), // sets the height to be 2 tiles
             getTilePixelHeight()); // sets the depth to be 1 tile
             actors.add(wall);
           }
           
            else if (cheapHackSolutionToPlayerGettingBuriedInAWall)
           {

              cheapHackSolutionToPlayerGettingBuriedInAWall = false;

             int xSpawn = ProceduralRoomTestThingy.getXSpawnPoint();
             int ySpawn = ProceduralRoomTestThingy.getYSpawnPoint();
             
             initPlayer(xSpawn, ySpawn);
           }
          }
        

        /*============================================================================================================*/
         
         
        FloorCeilingTile floor;

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
        
        else if(testArray[x][y] == 6){
          floor = new FloorCeilingTile("textures/oldbikiniBabe.jpg",
              true, 
              false,
              x * getTilePixelWidth(), 
              y * getTilePixelHeight(),
              getTilePixelWidth(), 
              1, 
              getTilePixelHeight()); 
        }
        else if(testArray[x][y] == 3)
        {
        // add a floor and ceiling tile
        floor = new FloorCeilingTile("textures/rock_texture.jpg",
            true, 
            false, 
            x * getTilePixelWidth(), 
            y * getTilePixelHeight(), 
            getTilePixelWidth(), 
            1,
            getTilePixelHeight()); 
        }
        
        else if(testArray[x][y] == 4)
        {
        // add a floor and ceiling tile
        floor = new FloorCeilingTile("textures/stone_texture.jpg",
            true, 
            false, 
            x * getTilePixelWidth(), 
            y * getTilePixelHeight(), 
            getTilePixelWidth(), 
            1, 
            getTilePixelHeight()); 
        }
        else{
          floor = new FloorCeilingTile("textures/brick_texture2.jpg",
              true, 
              false, 
              x * getTilePixelWidth(),
              y * getTilePixelHeight(),
              getTilePixelWidth(), 
              1, 
              getTilePixelHeight()); 
        }
        

        
        
        FloorCeilingTile ceiling = new FloorCeilingTile("textures/block_texture_dark.jpg",
            false, 
            true, 
            x * getTilePixelWidth(), 
            y * getTilePixelHeight(), 
            getTilePixelWidth(), 
            1, 
            getTilePixelHeight()); 

        actors.add(floor);
        actors.add(ceiling);
      }
    } //END for loop
  }

 

  private void initPlayASoundThingy()
  {
    actors.add(new PlayASoundThingy());
  }
  
 
  
  
}
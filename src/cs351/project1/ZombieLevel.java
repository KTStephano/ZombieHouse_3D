package cs351.project1;
import java.util.LinkedList;

import cs351.core.*;

public class ZombieLevel implements Level
{
  private static World world;
  private int pixelWidth;
  private int pixelHeight;
  private int tileWidth;
  private int tileHeight;
  private Actor player;
  
  private LinkedList<Actor> actors;
  
  /**
   * This should clear out the contents of the given World and then
   * reinitialize it to represent the starting point of whatever
   * Level is being created.
   *
   * @param world World object to initialize/reinitialize
   */
  public ZombieLevel(){}
  public ZombieLevel(int pixelWidth, int pixelHeight,int tileWidth, int tileHeight)
  {
    this.pixelWidth = pixelWidth;
    this.pixelHeight = pixelHeight;
    
    this.tileHeight = tileHeight;
    this.tileWidth = tileWidth;
    
  }
  
  /**
   * This is a callback method to the Zombie world. It 
   * initializes everything once. These values won't change
   */
  public void initWorld(World world)
  {
    world.setPixelWidthHeight(pixelWidth, pixelHeight);
    world.setTilePixelWidthHeight(pixelWidth, pixelHeight);
    world.setPlayer(player);
    world.initializeLevels(6);
    
//    // init the starting actors for the world
//    for (Actor actor : actors) world.add(actor);
//    for (Block block : block) world.add(block);
//    for (Tile  tile  : tile) world.add(tile);
    
    
  }

}

package cs351.project1;
import java.util.LinkedList;

import cs351.core.*;

public class ZombieLevel implements Level
{
  private int pixelWidth;
  private int pixelHeight;
  private int tileWidth;
  private int tileHeight;
  
  private LinkedList<Actor> actors;
  private LinkedList<Block> block;
  private LinkedList<Tile>  tile;
  
  /**
   * This should clear out the contents of the given World and then
   * reinitialize it to represent the starting point of whatever
   * Level is being created.
   *
   * @param world World object to initialize/reinitialize
   */
  public ZombieLevel(int pixelWidth, int pixelHeight,int tileWidth, int tileHeight)
  {
    this.pixelWidth = pixelWidth;
    this.pixelHeight = pixelHeight;
    
    this.tileHeight = tileHeight;
    this.tileWidth = tileWidth;
    
    //initialize actors and player
    //store in list
  }
  
  public void initWorld(World world)
  {
    //callback zombie world
    //iterate through each collection then add to world
    //ex world .set pixel width hetight(pixel width
    
  }
}

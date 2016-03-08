package cs351.project1;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import cs351.core.*;
import cs351.entities.ProceduralRoomTestThingy;

public class ZombieLevel implements Level
{
  private static World world;
  private int pixelWidth;
  private int pixelHeight;
  private int tileWidth;
  private int tileHeight;
  private int zombieSpeedIncrease;
  private int playerHealthIncrease;
  private int playerStaminaIncrease;
  private Actor player;
  private boolean hasInitialized = false;
  private int[][] levelData;
  private final HashSet<Vector3> STATIC_GEOMETRY_LOCATIONS = new HashSet<>();
  private final HashSet<Vector3> DYNAMIC_ACTOR_LOCATIONS = new HashSet<>();
  private final HashMap<Vector3, String> TEXTURE_DATA = new HashMap<>();
  private final Vector3 PLAYER_LOCATION = new Vector3(0.0);
  private final Vector3 MASTER_ZOMBIE_LOCATION = new Vector3(0.0);
  
  private LinkedList<Actor> actors;
  
  /**
   * This should clear out the contents of the given World and then
   * reinitialize it to represent the starting point of whatever
   * Level is being created.
   *
   * @param world World object to initialize/reinitialize
   */
  public ZombieLevel(int zombieSpeedIncrease, int playerHealthIncrease, int playerStaminaIncrease)
  {
    this.pixelWidth = 50;
    this.pixelHeight = 50;
    this.tileWidth = this.tileHeight = 1;
    this.zombieSpeedIncrease = zombieSpeedIncrease;
    this.playerHealthIncrease = playerHealthIncrease;
    this.playerStaminaIncrease = playerStaminaIncrease;
  }
  
  /**
   * This is a callback method to the Zombie world. It 
   * initializes everything once. These values won't change
   */
  public void initWorld(World world, Engine engine)
  {
    if (!hasInitialized)
    {
      ProceduralRoomTestThingy levelGenerator = new ProceduralRoomTestThingy();
      levelGenerator.initializeBoard();
      levelData = levelGenerator.getArray();
    }
    world.setPixelWidthHeight(pixelWidth, pixelHeight);
    world.setTilePixelWidthHeight(tileWidth, tileHeight);
    int numTilesWidth = pixelWidth / tileWidth; // convert pixels to number of tiles
    int numTilesHeight = pixelHeight / tileHeight; // convert pixels to number of tiles

  }

  @Override
  public void destroy()
  {

  }

}

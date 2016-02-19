package cs351.entities;

import cs351.core.Engine;
import cs351.core.GlobalConstants;
import cs351.core.Level;
import cs351.core.World;
import cs351.project1.EnvironmentDemo;

import java.util.Collection;
import java.util.Random;

import cs351.core.Actor;
import cs351.core.Actor.UpdateResult;

public class ZombieSpawningFloorTile extends FloorCeilingTile  {

  public ZombieSpawningFloorTile(String textureFile, double x, double y, int width,
      int height, int depth) {

    super(textureFile, true, false, x, y, width, height, depth);
  }
  
  /*  be aware -- this hard coding needs to be replaced */
  /*  waiting on collision detection                    */ 
  private boolean tileIsEmpty()
  {
    return true;
  }
  @Override
  public UpdateResult update(Engine engine, double deltaSeconds)
  {
    /*

    Attribute: Zombie Spawn, Default = 0.010
        1) While generating a level, each empty room floor tile has a
        change of spawning a zombie. On a given empty room floor
        tile, a zombie is spawned if a uniformly distributed random
        number on the interval [0.0, 1.0) is less than zombieSpawn

     */
    Random rand = new Random();
   
    if ((this.tileIsEmpty())&&(rand.nextInt(1000)/1000<GlobalConstants.zombieSpawn))
    {
      spawnZombie();
    }
    
    return UpdateResult.UPDATE_COMPLETED;
  }



  private void spawnZombie()
  {
    Random rand = new Random();

    int currTexture = rand.nextInt(7);
    String[] textures = { "textures/block_texture_dark.jpg", "textures/brick_texture.jpg", "textures/brick_texture2.jpg",
        "textures/crate_texture.jpg", "textures/metal_texture.jpg", "textures/rock_texture.jpg",
        "textures/ice_texture.jpg", "textures/stone_texture.jpg" };
    String[] lineWalker = { "textures/zombie.jpg" };
    Zombie wall;
    if (rand.nextInt(1)==1)
    {
      wall = new RandomWalkZombie(textures[currTexture],
          this.getLocation().getX(),
          this.getLocation().getY(),   
          GlobalConstants.tileWidthHeight,
          GlobalConstants.tileWidthHeight,
          GlobalConstants.tileWidthHeight);

    } else
    {
      wall = new LineWalkZombie(lineWalker[0],
          this.getLocation().getX(),
          this.getLocation().getY(),   
          GlobalConstants.tileWidthHeight,
          GlobalConstants.tileWidthHeight,
          GlobalConstants.tileWidthHeight);



    }
    // this needs to be resolved
    // need a reference to actors
    // not quite sure the best way
   // actors.add(wall);
  }
}

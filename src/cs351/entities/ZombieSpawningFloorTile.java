package cs351.entities;

import cs351.core.Engine;
import cs351.core.Game;
import cs351.core.GlobalConstants;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;


import java.util.Random;

import cs351.core.Actor;


public class ZombieSpawningFloorTile extends FloorCeilingTile  {
  private double elapsedTime;
  private double lastCollisionTime = 0;
  private Random rand;
  public ZombieSpawningFloorTile(String textureFile, double x, double y, int width,
      int height, int depth) {

    super(textureFile, true, false, x, y, width, height, depth);
    shouldUpdate = true; // engine will perform update calls
    elapsedTime = 0.0;
    rand = new Random(); 
  }


  private boolean tileIsEmpty()
  {
    return lastCollisionTime + 1000 < System.currentTimeMillis();
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
    elapsedTime += deltaSeconds;
    if ( (elapsedTime > 2.0)  && (this.tileIsEmpty())&&(rand.nextInt(1000000)/1000.0< GlobalConstants.zombieSpawn))
    {
      elapsedTime = 0;
      //spawnZombie();
    }

    return UpdateResult.UPDATE_COMPLETED;
  }



  private void spawnZombie()
  {
    Random rand = new Random();

    String[] lineWalker = { "textures/red_zombie.jpg" };
    Zombie wall;
    if (rand.nextInt(2)==1)
    {
      wall = new RandomWalkZombie(lineWalker[0],
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
    addActor(Game.getEngine(), wall);
  }

  private void addActor(Engine engine, Actor actor)
  {
    engine.getWorld().add(actor);
    // register the actor with the renderer so it can render it each frame
    engine.getRenderer().registerActor(actor,
        new Box(actor.getWidth(), actor.getHeight(), actor.getDepth()),
        //new Box(1,1,1),
        Color.BEIGE, // diffuse
        Color.BEIGE, // specular
        Color.WHITE); // ambient
    // sets the actor's texture so the renderer knows to load it and use it
    engine.getRenderer().mapTextureToActor(actor.getTexture(), actor);

  }

  @Override
  public void collided(Engine engine, Actor actor) {
    lastCollisionTime = System.currentTimeMillis();
  }
}

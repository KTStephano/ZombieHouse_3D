package cs351.entities;

import java.util.Random;

import cs351.core.Actor;
import cs351.core.Engine;


public class LineWalkZombie extends Zombie {
  //initialize to something we set
  private double elapsedSeconds=10;
  private Random rand = new Random();
  private double xDirection = -1000;
  private double yDirection = -1000;
  private boolean setNewDirection = true;

  @Override
  public void collided(Engine engine, Actor actor) {
    setNewDirection = true;
  }

  public LineWalkZombie(String textureFile, double x, double y, int width, int height, int depth)
  {
    super(textureFile, x, y, width, height, depth);
  }

  public UpdateResult update(Engine engine, double deltaSeconds)
  { 

    if (setNewDirection == true)
    {
      setNewDirection = false;
      // choose random X direction
      xDirection = (100-rand.nextInt(200))/20000.0;
      // choose random Y direction
      yDirection = (100-rand.nextInt(200))/20000.0;
    }
    setLocation(getLocation().getX()+xDirection, getLocation().getY() +yDirection);


    checkPlaySound(engine, deltaSeconds);
    return UpdateResult.UPDATE_COMPLETED;

  }
}

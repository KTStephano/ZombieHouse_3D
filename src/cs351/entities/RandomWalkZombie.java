package cs351.entities;

import cs351.core.Engine;
import cs351.entities.Zombie;

import java.util.Random;

import cs351.core.Actor.UpdateResult;

public class RandomWalkZombie extends Zombie {
  // initialize to something we set
  double elapsedSeconds=10;
  Random rand = new Random();
  double xDirection = 1;
  double yDirection = 1;
  double zombieDecisionRate = 2.0;

  public RandomWalkZombie(String textureFile, double x, double y, int width, int height, int depth)
  {
    super(textureFile, x, y, width, height, depth);
  }

  public UpdateResult update(Engine engine, double deltaSeconds)
  { 
    // totalSpeed represents the movement speed offset in tiles per second
    elapsedSeconds += deltaSeconds;
    // every 5 seconds, switch direction
    if (elapsedSeconds > zombieDecisionRate)
    {
      elapsedSeconds = 0.0;
      // -5.0 to 5.0
      xDirection = (100-rand.nextInt(200))/20000.0;
      // -5.0 to 5.0
      yDirection = (100-rand.nextInt(200))/20000.0;
    }
    setLocation(getLocation().getX()+xDirection, getLocation().getY() +yDirection);
    

    
    checkPlaySound(engine, deltaSeconds);
    return UpdateResult.UPDATE_COMPLETED;
    
  }

  
}

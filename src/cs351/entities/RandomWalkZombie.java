package cs351.entities;

import cs351.core.Engine;
import cs351.core.GlobalConstants;
import javafx.geometry.Point2D;

import java.util.Random;


public class RandomWalkZombie extends Zombie {
  // initialize to something we set
  private double elapsedSeconds=0;
  private Random rand = new Random();
  private double xDirection = 0;
  private double yDirection = 0;



  public RandomWalkZombie(String textureFile, double x, double y, int width, int height, int depth)
  {
    super(textureFile, x, y, width, height, depth);
  }

  public RandomWalkZombie(String textureFile, String modelFile, double x, double y, int width, int height, int depth)
  {
    super(textureFile, modelFile, x, y, width, height, depth);
  }

  public UpdateResult update(Engine engine, double deltaSeconds)
  {

    // totalSpeed represents the movement speed offset in tiles per second
    elapsedSeconds += deltaSeconds;

    // every zombieDecisionRate seconds, switch direction
    if (elapsedSeconds > GlobalConstants.zombieDecisionRate)
    {
   
      elapsedSeconds = 0.0;
      if (!canSmellPlayer(engine))
      {
        // -100 to 100 / 20000.0
        xDirection = (100-rand.nextInt(200))/20000.0;
        // -100 to 100 / 20000.0
        yDirection = (100-rand.nextInt(200))/20000.0;
      } 
      else
      {
          Point2D pt = super.PathfindToThePlayer(engine);
          xDirection = pt.getX();
          yDirection = pt.getY();        
      }

    }


    setLocation(getLocation().getX()+xDirection, getLocation().getY() +yDirection);



    checkPlaySound(engine, deltaSeconds);
    return UpdateResult.UPDATE_COMPLETED;



  }
}

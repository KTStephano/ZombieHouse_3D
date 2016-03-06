package cs351.entities;

import cs351.core.Engine;
import cs351.core.GlobalConstants;
import cs351.core.Vector3;
import javafx.geometry.Point2D;

import java.util.Random;


public class RandomWalkZombie extends Zombie {
  // initialize to something we set
  private double elapsedSeconds=0;
  private Random rand = new Random();
  private double xDirection = 0;
  private double yDirection = 0;
  private Vector3 directionXY = new Vector3(0.0);



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
    double zombieSpeed = Double.parseDouble(engine.getSettings().getValue("zombie_speed"));
    // every zombieDecisionRate seconds, switch direction
    if (elapsedSeconds > GlobalConstants.zombieDecisionRate)
    {
   
      elapsedSeconds = 0.0;
      if (!canSmellPlayer(engine))
      {
        xDirection = rand.nextDouble();
        yDirection = 1.0 - xDirection;
        directionXY.set(xDirection, yDirection, 0.0);
      } 
      else
      {
        Point2D pt = super.PathfindToThePlayer(engine);
        xDirection = pt.getX();
        yDirection = pt.getY();
        if (yDirection == 0.0 && xDirection != 0.0) xDirection = xDirection < 0.0 ? -1.0 : 1.0;
        else if (xDirection != 0.0) xDirection = xDirection < 0.0 ? -0.5 : 0.5;
        if (xDirection == 0.0 && yDirection != 0.0) yDirection = yDirection < 0.0 ? -1.0 : 1.0;
        else if (yDirection != 0.0) yDirection = yDirection < 0.0 ? -0.5 : 0.5;
        directionXY.set(xDirection, yDirection, 0.0);
      }

    }

    double totalSpeed = zombieSpeed * deltaSeconds;
    setLocation(getLocation().getX()+directionXY.getX() * totalSpeed,
                getLocation().getY() +directionXY.getY() * totalSpeed);



    checkPlaySound(engine, deltaSeconds);
    return UpdateResult.UPDATE_COMPLETED;



  }
}

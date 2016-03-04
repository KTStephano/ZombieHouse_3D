package cs351.entities;

import java.util.Random;

import cs351.core.Actor;
import cs351.core.Engine;
import cs351.core.GlobalConstants;
import javafx.geometry.Point2D;


public class LineWalkZombie extends Zombie {
  //initialize to something we set
  private double elapsedSeconds=0;
  private Random rand = new Random();
  private double xDirection = 0;
  private double yDirection = 0;
  private boolean setNewDirection = true;

  @Override
  public void collided(Engine engine, Actor actor) {
    // direction should be maintained if floor or if we hit player
    if (!actor.isPartOfFloor()&&!actor.isPlayer())
    {
      setNewDirection = true;
      //elapsedSeconds = 0;
    }    // direction should be maintained if floor or if we hit player
    if (!actor.isPartOfFloor()&&!actor.isPlayer())
    {
      setNewDirection = true;
      //elapsedSeconds = 0;
    }
  }

  public LineWalkZombie(String textureFile, double x, double y, int width, int height, int depth)
  {
    super(textureFile, x, y, width, height, depth);
  }

  public UpdateResult update(Engine engine, double deltaSeconds)
  { 

    //   System.out.println("--fps: "+1/deltaSeconds);

    // totalSpeed represents the movement speed offset in tiles per second
    elapsedSeconds += deltaSeconds;
   
    // every zombieDecisionRate seconds, switch direction
    if (elapsedSeconds > GlobalConstants.zombieDecisionRate)
    {
   

      elapsedSeconds = 0.0;
      if (!canSmellPlayer(engine)  && setNewDirection)
      {

        setNewDirection = false;
        // left or right random
        xDirection = (100-rand.nextInt(200))/20000.0;

        // forward or back random
        yDirection = (100-rand.nextInt(200))/20000.0;
      } 
      else if (canSmellPlayer(engine))
      {
          setNewDirection = false;
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






/*
 * 
 *    

          double currX = getLocation().getX();
          double currY = getLocation().getY();

          double targetX = engine.getWorld().getPlayer().getLocation().getX();
          double targetY = engine.getWorld().getPlayer().getLocation().getY();

          if (currX<targetX)
          {
            currX = currX+1;
          } else
          {
            currX = currX-1; 
          }

          if (currY<targetY)
          {
            currY = currY+1;
          } else
          {
            currY = currY - 1;
          }



          Point2D pt = engine.getDijkstra().getNextLocation((int)currX,(int)currY,(int)targetX,(int)targetY);


          // if we have a path to player and can smell him
          if (pt!=null)
          {

            if ( pt.getX() > currX+0.02) 
            {
              xDirection = 0.02;
            } else if ( pt.getX() < currX-0.02) 
            {
              xDirection = -0.02; 
            }else
              xDirection = 0;



            if ( pt.getY() > currY+0.02) 
            {
              yDirection = 0.02;
            } else if ( pt.getY() < currY-0.02) 
            {
              yDirection = -0.02; 
            } else
              yDirection = 0;         
          }
        } 

 */


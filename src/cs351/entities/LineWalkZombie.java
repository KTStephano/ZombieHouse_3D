package cs351.entities;

import java.util.Random;

import cs351.DijkstraAlgorithm.TestDijkstraAlgorithm;
import cs351.core.Actor;
import cs351.core.Engine;
import cs351.core.GlobalConstants;
import javafx.geometry.Point2D;


public class LineWalkZombie extends Zombie {
  //initialize to something we set
  private double elapsedSeconds=10;
  private Random rand = new Random();
  private double xDirection = -1000;
  private double yDirection = -1000;
  private boolean setNewDirection = true;
  private double elapsedTime=0;
  private int timerCt = 0;
  @Override
  public void collided(Engine engine, Actor actor) {
    setNewDirection = !actor.isPartOfFloor();
  }

  public LineWalkZombie(String textureFile, double x, double y, int width, int height, int depth)
  {
    super(textureFile, x, y, width, height, depth);
  }

  public UpdateResult update(Engine engine, double deltaSeconds)
  { 
    int currX = (int)this.getLocation().getX();
    int currY = (int)this.getLocation().getY();
    
    if (setNewDirection == true)
    {
      setNewDirection = false;
      // choose random X direction
      xDirection = (100-rand.nextInt(200))/20000.0;
      // choose random Y direction
      yDirection = (100-rand.nextInt(200))/20000.0;
    }
    
    Point2D pt = null;
    elapsedTime += deltaSeconds;

    if ( elapsedTime > GlobalConstants.zombieDecisionRate)
    {
      elapsedTime = 0;
      timerCt++;
      int targetX = (int)engine.getWorld().getPlayer().getLocation().getX();
      int targetY = (int)engine.getWorld().getPlayer().getLocation().getY();

      if ((timerCt == 20)&&canSmellPlayer(engine))
      {           
        timerCt = 0;
        double worldWidth = engine.getWorld().getWorldPixelWidth() / engine.getWorld().getTilePixelWidth();
        double worldHeight = engine.getWorld().getWorldPixelHeight() / engine.getWorld().getTilePixelHeight();
        
        engine.getDijkstra().initGraph(engine.getPathingData(), (int)worldWidth, (int)worldHeight);
        engine.getDijkstra().getNextLocation(currX,currY,targetX,targetY);
      }
      
     // if we have a path to player and can smell him
      if (pt!=null)
      {
        xDirection = (currX - pt.getX())/2000000;
        yDirection = (currY - pt.getY())/2000000;
      }
      // no path to player or can't smell player 
      else    
      {

      }
    }
    
    
    setLocation(currX+xDirection, currY+yDirection);







    checkPlaySound(engine, deltaSeconds);
    return UpdateResult.UPDATE_COMPLETED;


  }
}

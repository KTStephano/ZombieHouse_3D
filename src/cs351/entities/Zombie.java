package cs351.entities;



import cs351.core.Actor;
import cs351.core.Engine;
import cs351.core.GlobalConstants;
import cs351.AStar.Node;
import cs351.AStar.Pathfinder;
import cs351.DijkstraAlgorithm.TestDijkstraAlgorithm;
import javafx.geometry.Point2D;
import java.net.URL;
import java.util.List;
import java.util.Random;



public class Zombie extends Actor
{
  private Random rand = new Random();
  private double elapsedSeconds=0.0;
  private double xDirection = 0;
  private double yDirection = 0;

  public Zombie(String textureFile, double x, double y, int width, int height, int depth)
  {
    super(textureFile);
    setLocation(x, y);
    setWidthHeightDepth(width, height, depth);
  }

  public Zombie(String textureFile, String modelFile, double x, double y, int width, int height, int depth)
  {
    super(textureFile, modelFile);
    setLocation(x, y);
    setWidthHeightDepth(width, height, depth);
  }

  protected Point2D PathfindToThePlayer(Engine engine)
  {
    Point2D result = null;
    double currX = getLocation().getX();
    double currY = getLocation().getY();

    double targetX = engine.getWorld().getPlayer().getLocation().getX();
    double targetY = engine.getWorld().getPlayer().getLocation().getY();

    boolean[][] map = engine.getPathingData();
   
    /* for (int i=0;i<map.length;i++)
      for (int j=0;j<map.length;j++)
    {
      map[i][j] = false;
    }
   */
    List<Node> aNodeList=null;
    currX = (int)currX;
    currY = (int)currY;
    
    if ((currX > 0)&&(currY > 0)&&(targetX>0)&&(targetY>0))
    {
      if ((currX < map.length)&&(currY < map.length)&&(targetX < map.length)&&(targetY < map.length))
      {
       // System.out.println("Current:"+currX+","+currY+" Player: "+targetX+","+targetY);
        aNodeList =  Pathfinder.generate((int)currX,(int)currY,(int)targetX,(int)targetY, map);
      }
    }
    
    
    //List<Node> aNodeList =  Pathfinder.generate(3,1,10,2, map);
     //  System.out.println("check point 1");
    Node pt = null;
    if ((aNodeList!=null)&&(aNodeList.size() > 1))
    //{
      try
      {
      //System.out.println("check point 2");
      aNodeList.remove(0);
      pt = aNodeList.get(0);
      for (Node nod: aNodeList)
      {
       // System.out.println("x: "+nod.x  + "y: "+nod.y);
      } 
     // System.out.println("check point 3");     
      }
      catch (Exception e)
      {
      
      }
 //   }
    
    //Point2D pt = engine.getDijkstra().getNextLocation();
    

    // if we have a path to player and can smell him
    if (pt!=null)
    {
      //System.out.println("I'm at: "+currX+","+currY);     
      //System.out.println("My next step is: "+pt.x+","+pt.y);     

      if ( (int)pt.x > (int)currX) 
      {
        xDirection = 0.02;
      } else if ( (int)pt.x < (int)currX) 
      {
        xDirection = -0.02;
      } else
      {
        xDirection = 0;        
      }

      if ( (int)pt.y > (int)currY) 
      {
        yDirection = 0.02;
      } else if ( (int)pt.y < (int)currY) 
      {
        yDirection = -0.02;
      } else
      {
        yDirection = 0;        
      }



 

      result=new Point2D(xDirection,yDirection);
    } else
    {
      result = new Point2D(0,0);
    }
   
    return result;
  } 



  public UpdateResult update(Engine engine, double deltaSeconds)
  {

    // totalSpeed represents the movement speed offset in tiles per second
    elapsedSeconds += deltaSeconds;
    // every 5 seconds, switch direction
    if (elapsedSeconds > GlobalConstants.zombieDecisionRate)
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

  protected void checkPlaySound(Engine engine, double deltaSeconds)
  {
    elapsedSeconds+= deltaSeconds;

    if (elapsedSeconds >= 4.0)
    {
      elapsedSeconds = 0.0;

      String filename = "sound/zombie.wav";
      URL url = Zombie.class.getResource(filename);
      engine.getSoundEngine().queueSoundAtLocation(url, getLocation().getX(), getLocation().getY());


    }
  }
  public void collided(Engine engine, Actor actor)
  {

  }

  /*
  1) If a zombie's distance from the player is  zombieSmell,
  then the zombie can smell the player. This distance is the the
  shortest-path distance (NOT the Euclidean distance ignoring
  objects and walls as is the player hearing).
  2) If a zombie can smell a player, then the zombie "knows" the
  player's exact location and the shortest path, avoiding all
  obstacles, to the player.
  3) If either a Random Walk or Line Walk zombies smells a
  player, then on the next decision update, the zombie will
  calculate the shortest path and adjust its heading to match.


   */

  protected boolean canSmellPlayer(Engine engine)
  {

    int playerX = (int)engine.getWorld().getPlayer().getLocation().getX();
    int playerY = (int)engine.getWorld().getPlayer().getLocation().getY();

    double dx = playerX - getLocation().getX();
    double dy = playerY - getLocation().getY();
    int distanceToPlayer = (int)(Math.sqrt(dx*dx + dy*dy));

    if (distanceToPlayer <= GlobalConstants.zombieSmell)
    {
      return true;
    }
    else
    {
      return false;
    }   
    
  }



  protected Point2D getNextLocation(Engine engine, boolean canSmellPlayer, int [][] gameBoard)
  {

    double x=-1;
    double y=-1;
    Point2D pt;
    try
    {
      int playerX = (int)engine.getWorld().getPlayer().getLocation().getX();
      int playerY = (int)engine.getWorld().getPlayer().getLocation().getY();
      int zombieX = (int)this.getLocation().getX();
      int zombieY = (int)this.getLocation().getY();
      TestDijkstraAlgorithm dijkstras=engine.getDijkstra();
      Point2D nextZombieLocation=null;
      if (canSmellPlayer)
      {
        nextZombieLocation = dijkstras.getNextLocation(zombieX, zombieY, playerX, playerY);
      } 

      if (nextZombieLocation!=null)
      {
        x = nextZombieLocation.getX();
        y = nextZombieLocation.getY();     
      }
      else
      {
        Random rand = new Random();
        // direction to add to x
        double xDirection = (int) ((100-rand.nextInt(200))/20000.0);
        // direction to add to y
        double yDirection = (int) ((100-rand.nextInt(200))/20000.0);
        x = (getLocation().getX()+xDirection);
        y = (getLocation().getY()+yDirection);
      }
    }
    finally
    {
      pt =  new Point2D(x, y);
    }
    return pt;

  }


}

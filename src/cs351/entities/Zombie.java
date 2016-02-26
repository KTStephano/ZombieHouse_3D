package cs351.entities;

import cs351.core.Actor;
import cs351.core.Engine;
import cs351.core.GlobalConstants;
import cs351.DijkstraAlgorithm.TestDijkstraAlgorithm;
import javafx.geometry.Point2D;
import java.net.URL;
import java.util.Random;


public class Zombie extends Actor
{
  private Random rand = new Random();
  private final double BASE_SPEED = 2.0; // for x and y movement - measured in tiles per second
  private final double DIRECTION = 1.0;
  private double speedX = BASE_SPEED; // not moving at first
  private double speedY = 0.0; // not moving at first
  private double directionX = DIRECTION;
  private double directionY = DIRECTION;
  private double elapsedSeconds=0.0;

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

  public UpdateResult update(Engine engine, double deltaSeconds)
  {
    // totalSpeed represents the movement speed offset in tiles per second
    double totalSpeed = deltaSeconds * engine.getWorld().getTilePixelWidth();
    setLocation(getLocation().getX() + totalSpeed * speedX * directionX,
        getLocation().getY() + totalSpeed * speedY * directionY);
    if (rand.nextInt(1000) > 970) directionX = -directionX;
    if (rand.nextInt(1000) > 970) directionY = -directionY;
    if (rand.nextInt(1000) > 970)
    {
      double temp = speedX;
      speedX = speedY;
      speedY = temp;
    }
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
      //ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
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
 
    
    return  new Point2D(x, y);

  }


}

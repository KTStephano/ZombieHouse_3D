package cs351.entities;

import cs351.core.Actor;
import cs351.core.Engine;
import cs351.core.Vector3;

/**
 * TODO using this as a test for the renderer - need to flesh this out later when the engine is up
 */
public class Player extends Actor
{
  protected boolean isPlayer=true; // true -- this is the Player
  private double baseSpeed = 2.0; // for x and y movement - measured in tiles per second
  private double forwardX = 0.0; // not moving at first
  private double forwardY = 0.0; // not moving at first
  private double rightX = 0.0;
  private double rightY = 0.0;
  private Vector3 forwardDirection = new Vector3(0.0);
  private Vector3 rightDirection = new Vector3(forwardDirection);
  private double stepSoundTimer = 0.0;
  private boolean rightFoot = false;

  public Player(double x, double y, int height)
  {
    super(""); // player does not need a texture
    setLocation(x, y);
    setWidthHeightDepth(1, height, 1);
    noClip = false; // if this is true the player can run through everything but the floor/ceiling
  }

  public UpdateResult update(Engine engine, double deltaSeconds)
  {
    baseSpeed = Double.parseDouble(engine.getSettings().getValue("player_speed"));
    //System.out.println(1 / deltaSeconds);
    // totalSpeed represents the total speed per second in pixels
    //System.out.println(forwardX);
    stepSoundTimer += baseSpeed * forwardX * deltaSeconds;
    if (stepSoundTimer > 1.0)
    {
      stepSoundTimer = 0.0;
      double stepLocX, stepLocY;
      double multiplier;
      if (rightFoot) multiplier = 5;
      else multiplier = -5;
      rightFoot = !rightFoot;

      stepLocX = getLocation().getX() + multiplier * rightDirection.getX();
      stepLocY = getLocation().getY() + multiplier * rightDirection.getY();
      engine.getSoundEngine().queueSoundAtLocation("sound/player_step.mp3", stepLocX, stepLocY);
      //engine.getSoundEngine().queueSoundAtLocation("sound/zombie_low.wav", getLocation().getX(), getLocation().getY());
    }
    double totalSpeed = baseSpeed * deltaSeconds * engine.getWorld().getTilePixelWidth();
    setLocation(getLocation().getX() + totalSpeed * forwardX * forwardDirection.getX(),
                getLocation().getY() + totalSpeed * forwardY * forwardDirection.getY());
    setLocation(getLocation().getX() + totalSpeed * rightX * rightDirection.getX(),
                getLocation().getY() + totalSpeed * rightY * rightDirection.getY());
    return UpdateResult.UPDATE_COMPLETED;
  }

  public void collided(Engine engine, Actor actor)
  {

  }

  public void setForwardSpeedX(double speedX)
  {
    forwardX = speedX;
  }

  public void setForwardSpeedY(double speedY)
  {
    forwardY = speedY;
  }

  public void setRightSpeedX(double speedX)
  {
    rightX = speedX;
  }

  public void setRightSpeedY(double speedY)
  {
    rightY = speedY;
  }

  public void setForwardDirection(Vector3 direction)
  {
    forwardDirection.set(direction);
  }

  public void setRightDirection(Vector3 direction)
  {
    rightDirection.set(direction);
  }

  public Vector3 getForwardVector()
  {
    return forwardDirection;
  }

  public Vector3 getRightVector()
  {
    return rightDirection;
  }
}

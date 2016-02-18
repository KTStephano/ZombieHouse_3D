package cs351.entities;

import cs351.core.Actor;
import cs351.core.Engine;

import java.util.Random;

public class Zombie extends Actor
{
  private Random rand = new Random();
  private final double SPEED = 0.1; // for x and y movement
  private final double DIRECTION = 1.0;
  private double speedX = SPEED; // not moving at first
  private double speedY = 0.0; // not moving at first
  private double directionX = DIRECTION;
  private double directionY = DIRECTION;

  public Zombie(String textureFile, double x, double y, int width, int height, int depth)
  {
    super(textureFile);
    setLocation(x, y);
    setWidthHeightDepth(width, height, depth);
  }

  public UpdateResult update(Engine engine, double deltaSeconds)
  {
    setLocation(getLocation().getX() + speedX * directionX, getLocation().getY() + speedY * directionY);
    if (rand.nextInt(1000) > 970) directionX = -directionX;
    if (rand.nextInt(1000) > 970) directionY = -directionY;
    if (rand.nextInt(1000) > 970)
    {
      double temp = speedX;
      speedX = speedY;
      speedY = temp;
      //engine.getSoundEngine().queueSoundAtLocation(null\, (int)getLocation().getX(), (int)getLocation().getY());
    }
    return UpdateResult.UPDATE_COMPLETED;
  }

  public void collided(Engine engine, Actor actor)
  {

  }
}

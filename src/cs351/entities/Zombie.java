package cs351.entities;

import cs351.core.Actor;
import cs351.core.Engine;
import javafx.scene.media.AudioClip;

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
  
    if (elapsedSeconds >= 3.0)
    {
      elapsedSeconds = 0.0;
      final URL resource = getClass().getClassLoader().getResource("cs351/entities/sound/zombie.mp3");
      final AudioClip media = new AudioClip(resource.toString());
      engine.getSoundEngine().queueSoundAtLocation(media, getLocation().getX(), getLocation().getY());
    }
    
  }
  public void collided(Engine engine, Actor actor)
  {

  }
}

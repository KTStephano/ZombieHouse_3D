package cs351.entities;

import cs351.core.Actor;
import cs351.core.Engine;
import cs351.core.Vector3;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyEvent;

/**
 * TODO using this as a test for the renderer - need to flesh this out later when the engine is up
 */
public class Player extends Actor
{
  private final double BASE_SPEED = 5.0; // for x and y movement - measured in tiles per second
  private double forwardX = 0.0; // not moving at first
  private double forwardY = 0.0; // not moving at first
  private double rightX = 0.0;
  private double rightY = 0.0;
  private Vector3 forwardDirection = new Vector3(0.0);
  private Vector3 rightDirection = new Vector3(forwardDirection);

  public Player(double x, double y, int height)
  {
    super(""); // player does not need a texture
    setLocation(x, y);
    setWidthHeightDepth(1, height, 1);
    noClip = false; // if this is true the player can run through everything but the floor/ceiling
  }

  public UpdateResult update(Engine engine, double deltaSeconds)
  {
    // totalSpeed represents the total speed per second in pixels
    double totalSpeed = BASE_SPEED * deltaSeconds * engine.getWorld().getTilePixelWidth();
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

  public void keyPressed(KeyEvent event)
  {
    /**
    if (event.getText().equals("w")) forwardY = SPEED;
    else if (event.getText().equals("s")) forwardY = -SPEED;
    else if (event.getText().equals("a")) forwardX = -SPEED;
    else if (event.getText().equals("d")) forwardX = SPEED;
     */
  }

  public void keyReleased(KeyEvent event)
  {
    forwardX = 0.0;
    forwardY = 0.0;
  }
}

package cs351.entities;

import cs351.core.Actor;
import cs351.core.Engine;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyEvent;

/**
 * TODO using this as a test for the renderer - need to flesh this out later when the engine is up
 */
public class Player extends Actor
{
  private final double BASE_SPEED = 10.0; // for x and y movement - measured in tiles per second
  private double forwardX = 0.0; // not moving at first
  private double forwardY = 0.0; // not moving at first
  private double rightX = 0.0;
  private double rightY = 0.0;
  private Point2D forwardDirection;
  private Point2D rightDirection;

  public Player(double x, double y, int height)
  {
    super(""); // player does not need a texture
    setLocation(x, y);
    setWidthHeightDepth(1, height, 1);
  }

  public UpdateResult update(Engine engine, double deltaSeconds)
  {
    setLocation(getLocation().getX() + BASE_SPEED * forwardX * deltaSeconds * forwardDirection.getX(),
                getLocation().getY() + BASE_SPEED * forwardY * deltaSeconds * forwardDirection.getY());
    setLocation(getLocation().getX() + BASE_SPEED * rightX * deltaSeconds * rightDirection.getX(),
                getLocation().getY() + BASE_SPEED * rightY * deltaSeconds * rightDirection.getY());
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

  public void setForwardDirection(Point2D direction)
  {
    forwardDirection = direction;
  }

  public void setRightDirection(Point2D direction)
  {
    rightDirection = direction;
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

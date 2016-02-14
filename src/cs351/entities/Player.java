package cs351.entities;

import cs351.core.Actor;
import cs351.core.Engine;
import cs351.core.Renderer;
import javafx.scene.input.KeyEvent;

/**
 * TODO using this as a test for the renderer - need to flesh this out later when the engine is up
 */
public class Player extends Actor
{
  private final double SPEED = 1.0; // for x and y movement
  private final double DIRECTION = 1.0;
  private double speedX = 0.0; // not moving at first
  private double speedY = 0.0; // not moving at first
  private double directionX = DIRECTION;
  private double directionY = DIRECTION;

  public Player(double x, double y)
  {
    setLocation(x, y);
  }

  public UpdateResult update(Engine engine, double deltaSeconds)
  {
    setLocation(getLocation().getX() + speedX * directionX, getLocation().getY() + speedY * directionY);
    return UpdateResult.UPDATE_COMPLETED;
  }

  public void collided(Engine engine, Actor actor)
  {

  }

  public void keyPressed(KeyEvent event)
  {
    if (event.getText().equals("w")) speedY = SPEED;
    else if (event.getText().equals("s")) speedY = -SPEED;
    else if (event.getText().equals("a")) speedX = -SPEED;
    else if (event.getText().equals("d")) speedX = SPEED;
  }

  public void keyReleased(KeyEvent event)
  {
    speedX = 0.0;
    speedY = 0.0;
  }
}

package cs351.core;

import javafx.geometry.Point2D;

import java.util.LinkedList;

/**
 * The Actor class represents any object that can be added to the game
 * and updated each frame. It can be used to represent both moving
 * and non-moving objects.
 *
 * The Engine will guarantee that the update function for each Actor will
 * be called at most once per frame unless the shouldUpdate boolean is
 * set to false (this can be done for something like a wall that never
 * needs to move/be updated but still needs to be drawn).
 */
// @todo Need to finish this - haven't figured out all of the stuff this needs yet, especially for Rendering
public abstract class Actor
{
  protected int width, height; // width and height are measured in terms of tiles, not pixels
  protected Point2D location = new Point2D(0.0, 0.0); // location of the object in 2D space
  protected boolean shouldUpdate = true; // if false the Engine will ignore this Actor (it will still be drawn and collide with stuff, though)
  protected boolean isStatic = false; // if true, collision events won't cause the Actor to move at all
  protected boolean noClip = false; // if true the object will not collide with anything (phase through walls, zombies, etc.)

  /**
   * UpdateResult contains a few different enum values that each Actor can use
   * to tell the Engine how the update went.
   */
  public enum UpdateResult
  {
    UPDATE_COMPLETED, // Engine interprets this as normal and does nothing
    PLAYER_VICTORY, // Engine will now ask the current world to load the next level
    PLAYER_DEFEAT // Engine will now ask the current world to restart the same level
  }

  /**
   * Uses the Point2D hashCode function.
   *
   * @return hash code
   */
  @Override
  public int hashCode()
  {
    return location.hashCode();
  }

  /**
   * Updates the Actor. The Engine guarantees that this function will be called
   * at most once per frame for all existing Actors.
   *
   * This is also where the object should perform animation steps.
   *
   * @param engine reference to the Engine that is performing the frame update - can be used for Engine callbacks
   * @param deltaSeconds the number of seconds that have gone by since the last frame (can be used for animation, movement, etc.)
   * @return result of the update - tells the Engine if it needs to do anything special (end the game, etc.)
   */
  public abstract UpdateResult update(Engine engine, double deltaSeconds);

  /**
   * When the Engine detects a collision between two actors (player and wall,
   * wall and zombie, etc.) it will generate collision events for all Actors
   * involved. For example, if a wall and a zombie collided, the wall object
   * would get a reference to the zombie, and the zombie would get a reference to the
   * wall.
   *
   * The only thing that Actors don't have to deal with is offsetting themselves
   * because of a collision. If two objects collide the Engine will push each of
   * them (unless one of the Actors is static) away from each other.
   * @param engine reference to the Engine that is performing the frame update - can be used for Engine callbacks
   * @param actor reference to the Actor object that collided with this object
   */
  public abstract void collided(Engine engine, Actor actor);

  /**
   * Allows for the Actor's width and height to be set.
   *
   * @param width width (in tiles)
   * @param height height (in tiles)
   */
  public abstract void setWidthHeight(int width, int height);

  /**
   * Gets the width of the Actor.
   *
   * @return width in tiles
   */
  public int getWidth()
  {
    return width;
  }

  /**
   * Gets the height of the Actor.
   *
   * @return height in tiles
   */
  public int getHeight()
  {
    return height;
  }

  /**
   * Overwrites the Actor's previous location with a new location.
   *
   * @param x x-coordinate
   * @param y y-coordinate
   */
  public void setLocation(double x, double y)
  {
    location = new Point2D(x, y);
  }

  /**
   * Gets the current 2D location of the player.
   *
   * @return player location
   */
  public Point2D getLocation()
  {
    return location;
  }

  /**
   * Checks to see if the given Actor needs to be updated every
   * frame.
   *
   * @return true if it should update every frame and false if not
   */
  public boolean shouldUpdate()
  {
    return shouldUpdate;
  }

  /**
   * The Engine uses this to figure out how to offset objects when they
   * collide with each other. If one of the objects returns true
   * when isStatic() is called, that object won't be pushed away from the other
   * object at all.
   *
   * @return true if the object should not move and false if it should
   */
  public boolean isStatic()
  {
    return isStatic;
  }

  /**
   * This is used as a flag during the Engine's collision detection routine.
   *
   * If no-clip is active it will let the Actor walk through anything but the floor.
   *
   * @return true if no-clip is active and false if not
   */
  public boolean noClipActive()
  {
    return noClip;
  }
}
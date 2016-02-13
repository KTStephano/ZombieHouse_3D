package cs351.core;

/**
 * A World object maintains the current state of all active
 * objects that an Engine is working with. It does not need to
 * deal with collision detection and the Engine will let it know
 * when an object has moved from one location to another.
 */
public interface World
{
  /**
   * Checks to see if the World currently has the given Actor object.
   *
   * @param actor Actor object to check for
   * @return true if it exists in the World and false if not
   */
  boolean contains(Actor actor);

  /**
   * Takes a reference to an Actor object and tries to remove it from
   * the World.
   *
   * @param actor Actor object to remove
   * @throws RuntimeException if the given Actor does not exist in the World
   */
  void remove(Actor actor) throws RuntimeException;

  /**
   * Tries to add an Actor object to the existing World.
   *
   * @param actor Actor object to add
   */
  void add(Actor actor);

  /**
   * Adds a level to the end of the current list of levels.
   *
   * @param level Level to add
   */
  void add(Level level);

  /**
   * This should return the width of the world in terms of pixels.
   *
   * @return pixel world width
   */
  int getPixelWidth();

  /**
   * This should return the width of the world in terms of pixels.
   *
   * @return pixel world height
   */
  int getPixelHeight();

  /**
   * Sets the width and height of the world in terms of pixels. This will probably only
   * ever be called by a Level object.
   *
   * @param pixelWidth width of the world in pixels
   * @param pixelHeight height of the world in pixels
   */
  void setPixelWidthHeight(int pixelWidth, int pixelHeight);

  /**
   * This is used to figure out which Actor object is the player. The current player object
   * should be set by a Level.
   *
   * @return reference to player
   */
  Actor getPlayer();

  /**
   * Lets the World know which object is serving as the player.
   *
   * @throws RuntimeException if the given Actor does not exist in the World
   */
  void setPlayer(Actor player) throws RuntimeException;

  /**
   * Checks to see if there is another Level that can be loaded.
   *
   * @return true if there is and false if not
   */
  boolean hasNextLevel();

  /**
   * If there is another Level
   */
  void nextLevel();
}

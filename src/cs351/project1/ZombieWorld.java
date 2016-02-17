package cs351.project1;
import cs351.core.*;
import cs351.entities.Tiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;


/**
 * A World object maintains the current state of all active
 * objects that an Engine is working with. It does not need to
 * deal with things like collision detection as that will
 * be handled at the Engine level.
 */
public class ZombieWorld implements World
{
  private int pixelWidth;
  private int pixelHeight;
  private Actor player;
  private HashSet<Actor> changeList = new HashSet<Actor>(50);
  private ArrayList<Actor> actors = new ArrayList<>();
  private ArrayList<Level> levels = new ArrayList<>();
  
  public ZombieWorld(){}

  public ZombieWorld(int pixelWidth, int pixelHeight)
  {
    this.pixelWidth  = pixelWidth;
    this.pixelHeight = pixelHeight;
  }
  
  /**
   * Checks to see if the World currently has the given Actor object.
   *
   * @param actor object to check for
   * @return true if it exists in the World and false if not
   */
  public boolean contains(Actor actor)
  {
    return actors.contains(actor);
  }
  
  /**
   * Takes a reference to an Actor object and tries to remove it from
   * the World.
   *
   * @param actor object to remove
   * @throws RuntimeException if the given Actor does not exist in the World
   */
  public void remove(Actor actor) throws RuntimeException
  {
    if (!contains(actor)) throw new RuntimeException();
    else
    {
      // remove actor from list
      actors.remove(actor);
      if (changeList.contains(actor)) changeList.remove(actor);
    }
  }

  /**
   ============================================
   This method passes an actor as an argument
   and adds to the respective ArrayList. It's
   supposed to be more consolidated instead of
   having multiple add() methods
   ============================================
   */
  public void add(Actor actor)
  {
    //Add actor object to generic collection
    actors.add(actor);
    changeList.add(actor);
  }

  /**
   * Adds a level to the end of the current list of levels.
   *
   * @param level Level to add
   */
  public void add(Level level)
  {
    levels.add(level);
  }

  /**
   * This should return the width of the world in terms of pixels.
   *
   * @return pixel world width
   */
  public int getWorldPixelWidth()
  {
    return pixelWidth;
  }

  /**
   * This should return the width of the world in terms of pixels.
   *
   * @return pixel world height
   */
  public int getWorldPixelHeight()
  {
    return pixelHeight;
  }

  /**
   * Sets the width and height of the world in terms of pixels. This will probably only
   * ever be called by a Level object.
   *
   * @param pixelWidth width of the world in pixels
   * @param pixelHeight height of the world in pixels
   */
  public void setPixelWidthHeight(int pixelWidth, int pixelHeight)
  {
    this.pixelWidth = pixelWidth;
    this. pixelHeight = pixelHeight;
  }

  /**
   * This should return the width of the tiles in terms of pixels.
   *
   * @return width of each tile in pixels
   */
  public int getTilePixelWidth()
  {
    return pixelWidth;
  }

  /**
   * This should return the height of the tiles in terms of pixels.
   *
   * @return height of each tile in pixels
   */
  public int getTilePixelHeight()
  {
    return pixelHeight;
  }

  /**
   * Sets the default size of the world tiles in terms of pixels.
   *
   * @param pixelWidth tile width in pixels
   * @param pixelHeight tile height in pixels
   */
  public void setTilePixelWidthHeight(int pixelWidth, int pixelHeight)
  {
    this.pixelWidth = pixelWidth;
    this.pixelHeight = pixelHeight;
  }

  /**
   * This is used to figure out which Actor object is the player. The current player object
   * should be set by a Level.
   *
   * @return reference to player
   */
  public Actor getPlayer()
  {
    return player;
  }

  /**
   * Lets the World know which object is serving as the player.
   *
   * @throws RuntimeException if the given Actor does not exist in the World
   */
  public void setPlayer(Actor player) throws RuntimeException
  {
//    if( player == null){
//      System.out.println("setPlayer ex2, player doesn't exist");
//      throw new RuntimeException();
//    }
    this.player = player;
  }

  public Collection<Actor> getChangeList(boolean clearChangeList)
  {
    if (clearChangeList) {
      HashSet<Actor> returnVal = new HashSet<Actor>(changeList);
      changeList.clear();
      return returnVal;
    }
    return changeList;
  }

  /**
   * Checks to see if there is another Level that can be loaded. The Engine
   * will call this whenever the previous Level has ended.
   *
   * @return true if there is and false if not
   */
  public boolean hasNextLevel()
  {
    //TODO add more here
    return true;
  }

  /**
   * This function is called when the Engine is first started up and it wants
   * to begin the game as well as each time the previous level has ended.
   *
   * If there is another Level, the World should load it, and then tell the
   * next Level to initialize the starting state of the game.
   *
   * @param engine reference to the Engine object which is calling this function -
   *               can be used for callbacks, especially to tell the Engine about
   *               each new Actor that is being added so it can start building
   *               separate lists to improve its performance/the renderer's performance
   */
  public void nextLevel(Engine engine)
  {
    if (hasNextLevel() == true)
    {
      // There is another level, the world will load it
      // initialize starting state of game
    }
  }

  /**
   * This function is called when the Engine wants to restart the same level over
   * again.
   *
   * @param engine reference to the Engine object which is calling this function -
   *               can be used for callbacks, especially to tell the Engine about
   *               each new Actor that is being added so it can start building
   *               separate lists to improve its performance/the renderer's performance
   */
  public void restartLevel(Engine engine)
  {
    //Will work on this later once engine has more implementations
  }
}


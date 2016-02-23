package cs351.core;

import javafx.stage.Stage;

import java.util.Collection;

import cs351.DijkstraAlgorithm.TestDijkstraAlgorithm;


/**
 * The Engine is the driving force behind a game. It ties together all of
 * the critical systems that need to work together (World, SoundEngine, Renderer)
 * and provides an easy interface for accessing them and for updating the game
 * each frame.
 */
public interface Engine
{
  /**
   * Returns the game world associated with the engine.
   *
   * @return reference to the game world
   */
  World getWorld();
  
  /**
   * Returns Dijkstra's path finding class
   *
   * @return dijkstra's class
   */
  TestDijkstraAlgorithm getDijkstra();
 
  /**
   * Returns the sound engine associated with the engine.
   *
   * @return reference to the sound engine
   */
  SoundEngine getSoundEngine();

  /**
   * Returns the renderer associated with the engine.
   *
   * @return reference to the renderer
   */
  Renderer getRenderer();

  /**
   * Gets the world width in terms of tiles.
   *
   * @return width of the world in tiles
   */
  int getWorldWidth();

  /**
   * Gets the world height in terms of tiles.
   *
   * @return height of the world in tiles
   */
  int getWorldHeight();

  /**
   * This should be called before each call to frame - if the engine hit the end
   * of the world's level list during the previous frame it will have shut itself down.
   *
   * @return true if it is (game is over) and false if not
   */
  boolean isEnginePendingShutdown();

  /**
   * Returns all neighboring actors to the given actor that are tileDistance
   * away. This includes all objects that extend Actor (including Block and Tile).
   *
   * @param actor actor whose neighbors are requested
   * @param tileDistance distance in tiles around the actor to check
   * @return list of neighbors
   */
  Collection<Actor> getNeighboringActors(Actor actor, int tileDistance);

  /**
   * When this is called, an array of booleans is returned representing all the areas
   * in the map that zombies can walk over. If the value in the board is true, that
   * area is a valid floor tile with no wall obstacle covering it. If it is false then
   * there is a wall sitting on top of it and that spot can't be reached.
   *
   * @return boolean array representing the world
   */
  boolean[][] getPathingData();

  /**
   * Initializes the engine and prepares it for the first call to the
   * frame function.
   *
   * @param stage used to setup any callbacks (such as window exit)
   * @param world World to initialize the engine with
   * @param soundEngine SoundEngine to initialize the engine with
   * @param renderer Renderer to initialize the engine with
   */
  void init(Stage stage, World world, SoundEngine soundEngine, Renderer renderer);

  /**
   * Kills the engine. Any future calls to any engine methods (unless init is
   * called again) will result in exceptions.
   */
  void shutdown();

  /**
   * Allows for pausing and un-pausing of the engine. When paused, calls to
   * frame will not do anything.
   *
   * @param value true if the engine should pause and false if not
   */
  void togglePause(boolean value);

  /**
   * Runs the next frame. A frame consists of the following:
   *    - Check for collisions between actors
   *    - Push collision events to appropriate actors
   *    - Call the update function for every actor who signaled the need to update
   *    - Update the sound engine
   *    - Update the renderer
   *    - Ask the world for a level reset/a new level if needed
   */
  void frame();
}

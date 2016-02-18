package cs351.project1;

import cs351.core.Actor;
import cs351.core.Engine;
import cs351.core.Level;
import cs351.core.World;
import cs351.entities.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

/**
 * This world class is more of a tutorial showing you guys how to use
 * the renderer in order to build a basic level with some walls and zombies.
 *
 * Once Chris gets his world class up we can convert this demo to a Level class
 * that we can add to his world.
 */
public class EnvironmentDemo implements World
{
  private Random rand = new Random();
  private HashSet<Actor> actors = new HashSet<>(250);
  private int worldPixelWidth = 30; // measured in pixels
  private int worldPixelHeight = 30; // measured in pixels
  private int tileWidthHeight = 1; // measured in pixels
  private Actor player;

  @Override
  public boolean contains(Actor actor)
  {
    return actors.contains(actor);
  }

  @Override
  public void remove(Actor actor) throws RuntimeException
  {
    throw new RuntimeException("I'm not a real world so most of my stuff doesn't work");
  }

  @Override
  public void add(Actor actor)
  {
    actors.add(actor);
  }

  @Override
  public void add(Level level)
  {
    throw new RuntimeException("I'm not a real world so most of my stuff doesn't work");
  }

  @Override
  public int getWorldPixelWidth()
  {
    return worldPixelWidth;
  }

  @Override
  public int getWorldPixelHeight()
  {
    return worldPixelHeight;
  }

  @Override
  public void setPixelWidthHeight(int pixelWidth, int pixelHeight)
  {
    throw new RuntimeException("I'm not a real world so most of my stuff doesn't work");
  }

  @Override
  public int getTilePixelWidth()
  {
    return tileWidthHeight;
  }

  @Override
  public int getTilePixelHeight()
  {
    return tileWidthHeight;
  }

  @Override
  public void setTilePixelWidthHeight(int pixelWidth, int pixelHeight)
  {
    throw new RuntimeException("I'm not a real world so most of my stuff doesn't work");
  }

  @Override
  public Actor getPlayer()
  {
    return player;
  }

  @Override
  public void setPlayer(Actor player) throws RuntimeException
  {
    throw new RuntimeException("I'm not a real world so most of my stuff doesn't work");
  }

  @Override
  public Collection<Actor> getChangeList(boolean clearChangeList)
  {
    if (clearChangeList)
    {
      HashSet<Actor> retVal = new HashSet<>(actors);
      actors.clear(); // delete my only actor list :( not a real world though so it's ok
      return retVal;
    }
    return actors;
  }

  @Override
  public void initializeLevels(int numberOfLevels)
  {
    throw new RuntimeException("I'm not a real world so most of my stuff doesn't work");
  }

  @Override
  public boolean hasNextLevel()
  {
    return true; // does the same thing every time
  }

  @Override
  public void nextLevel(Engine engine)
  {
    actors.clear();
    initPlayer();
    initWallsFloorAndCeiling();
    initZombies();
    initPlayASoundThingy(); // can't forget about this
    for (Actor actor : actors)
    {
      // register the actor with the renderer so it can render it each frame
      engine.getRenderer().registerActor(actor,
                                         new Box(actor.getWidth(), actor.getHeight(), actor.getDepth()),
                                         Color.BEIGE, // diffuse
                                         Color.BEIGE, // specular
                                         Color.WHITE); // ambient
      // sets the actor's texture so the renderer knows to load it and use it
      engine.getRenderer().mapTextureToActor(actor.getTexture(), actor);
    }
  }

  @Override
  public void restartLevel(Engine engine)
  {
    throw new RuntimeException("I'm not a real world so most of my stuff doesn't work");
  }

  private void initPlayer()
  {
    // the getWorldPixelWidth() / 2.0 and getWorldPixelHeight() / 2.0 make it so the player is
    // in the middle of the map
    //
    // the 2 * tileWidthHeight is because each tile is 10 pixels and I want
    // the player to be 2 tiles high
    player = new Player(getWorldPixelWidth() / 2.0, getWorldPixelHeight() / 2.0, 2 * tileWidthHeight);
    actors.add(player);
  }

  private void initWallsFloorAndCeiling()
  {
    // the + 1 is for when the pixelWidth/Height and tilePixelWidth/Height don't divide evenly
    int numTilesWidth = getWorldPixelWidth() / getTilePixelWidth() + 1; // convert pixels to number of tiles
    int numTilesHeight = getWorldPixelHeight() / getTilePixelHeight() + 1; // convert pixels to number of tiles

    for (int x = 0; x < numTilesWidth; x++)
    {
      for (int y = 0; y < numTilesHeight; y++)
      {
        // this first if checks to see if the current tile is on the border - if it is
        // it adds a wall
        if (x == 0 || y == 0 || x == numTilesWidth - 1 || y == numTilesHeight - 1)
        {
          Actor wall = new Wall("textures/block_texture_dark.jpg",
                                x * getTilePixelWidth(), // offset - when x = 0, this = 0, when x = 1, this = the tile width in pixels
                                y * getTilePixelHeight(), // same as above but for y
                                getTilePixelWidth(), // sets the width to be 1 tile
                                2 * getTilePixelHeight(), // sets the height to be 2 tiles
                                getTilePixelHeight()); // sets the depth to be 1 tile
          actors.add(wall);
        }
        // add a floor and ceiling tile
        FloorCeilingTile floor = new FloorCeilingTile("textures/block_texture_dark.jpg",
                                                      true, // is part of floor
                                                      false, // is not part of ceiling
                                                      x * getTilePixelWidth(), // offset - when x = 0, this = 0, when x = 1, this = the tile width in pixels
                                                      y * getTilePixelHeight(), // same as above but for y
                                                      getTilePixelWidth(), // sets the width to be 1 tile
                                                      1, // sets the height to be 1 pixel - this is a good idea to do for all floor and ceiling tiles
                                                      getTilePixelHeight()); // sets the depth to be 1 tile

        FloorCeilingTile ceiling = new FloorCeilingTile("textures/block_texture_dark.jpg",
                                                        false, // is not part of floor
                                                        true, // is part of ceiling
                                                        x * getTilePixelWidth(), // offset - when x = 0, this = 0, when x = 1, this = the tile width in pixels
                                                        y * getTilePixelHeight(), // sets the width to be 1 tile
                                                        getTilePixelWidth(), // sets the width to be the whole width of the map (covers the entire space)
                                                        1, // sets the height to be 1 pixel - this is a good idea to do for all floor and ceiling tiles
                                                        getTilePixelHeight()); // sets the depth to be 1 tile

        actors.add(floor);
        actors.add(ceiling);
      }
    }
  }

  private void initZombies()
  {
    int numZombies = 100;
    int currTexture = 0;
    String[] textures = { "textures/block_texture_dark.jpg", "textures/brick_texture.jpg", "textures/brick_texture2.jpg",
                          "textures/crate_texture.jpg", "textures/metal_texture.jpg", "textures/rock_texture.jpg",
                          "textures/ice_texture.jpg", "textures/stone_texture.jpg" };
    for (int i = 0; i < numZombies; i++)
    {
      Zombie wall = new Zombie(textures[currTexture],
                               rand.nextInt(getWorldPixelWidth()), // random location (within the world bounds)
                               rand.nextInt(getWorldPixelHeight()), // random location (within the world bounds)
                               getTilePixelWidth(), // sets width to be 1 tile
                               getTilePixelWidth(), // sets height to be 1 tile
                               getTilePixelWidth()); // sets depth to be 1 tile
      actors.add(wall);
      currTexture++;
      if (currTexture >= textures.length) currTexture = 0;
    }
  }

  private void initPlayASoundThingy()
  {
    actors.add(new PlayASoundThingy());
  }
}
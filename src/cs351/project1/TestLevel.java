package cs351.project1;

import cs351.core.Actor;
import cs351.core.Level;
import cs351.core.World;
import cs351.core.Tile;
import cs351.core.Block;
import cs351.entities.Player;
import cs351.entities.Zombie;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;

import java.util.LinkedList;
import java.util.Random;

public class TestLevel implements Level
{
  private LinkedList<Actor> actors = new LinkedList<>();
  private LinkedList<Block> staticBlocks = new LinkedList<>();
  private LinkedList<Tile> floorCeilingTiles = new LinkedList<>();
  private Actor player;

  private int pixelWidth;
  private int pixelHeight;
  private int pixelTileWidthHeight;

  public TestLevel(int pixelWidth, int pixelHeight, int pixelTileWidthHeight)
  {
    this.pixelWidth = pixelWidth;
    this.pixelHeight = pixelHeight;
    this.pixelTileWidthHeight = pixelTileWidthHeight;

    // initialize all of the actors/geometry/player for the level and
    // never reinitialize it
    initPlayer();
    initZombies();
  }

  @Override
  public void initWorld(World world)
  {
    // init the world's pixel width and pixel height
    world.setPixelWidthHeight(pixelWidth, pixelHeight);
    // init the world's tile width and tile height
    world.setTilePixelWidthHeight(pixelTileWidthHeight, pixelTileWidthHeight);
    // set the player for the world
    world.setPlayer(player);
    // init the starting actors for the world
    for (Actor actor : actors) world.add(actor);
    for (Block block : staticBlocks) world.add(block);
    for (Tile tile : floorCeilingTiles) world.add(tile);
  }

  private void initPlayer()
  {
    Player player = new Player(100.0, 0.0);
    actors.add(player);
  }

  private void initZombies()
  {
    Random rand = new Random();
    int numZombies = 100;
    for (int i = 0; i < numZombies; i++)
    {
      Zombie wall = new Zombie(rand.nextInt(numZombies), rand.nextInt(numZombies), 5, 5, 5);
      actors.add(wall);
    }
  }
}

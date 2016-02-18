package cs351.project1;

import cs351.core.Actor;
import cs351.core.Level;
import cs351.core.World;
import cs351.core.Tile;
import cs351.core.Block;
import cs351.entities.Blocks;
import cs351.entities.Player;
import cs351.entities.Tiles;
import cs351.entities.Zombie;

import java.util.LinkedList;
import java.util.Random;

//implements interface Level
public class LevelTest implements Level
{
  
  private LinkedList<Actor> actors            = new LinkedList<>();
  private Actor player;

  private  int pixelTileWidthHeight;
  private  int pixelHeight;
  private  int pixelWidth;

 
  public LevelTest(int pixelWidth, int pixelHeight, int pixelTileWidthHeight)
  {
    this.pixelWidth           = pixelWidth;
    this.pixelHeight          = pixelHeight;
    this.pixelTileWidthHeight = pixelTileWidthHeight;

    
    // initialize all of the actors/geometry/player for the level and
    // never reinitialize it
    initPlayer();
    initZombies();
  }

  /*
   ==================================================
   callback zombie world
   iterate through each collection then add to world
   ==================================================
   */
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
  }

  private void initPlayer()
  {
<<<<<<< HEAD
    System.out.println("init player");
    player = new Player(100.0, 50.0);
=======
    Player player = new Player(100.0, 0.0, 5);
    renderer.registerPlayer(player, 90.0);
>>>>>>> df766e00ddee3e8f60d6bf064c3b5c39eec1247a
    actors.add(player);
  }

  private void initZombies()
  {
    System.out.println(" initZombies");
    Random rand = new Random();
<<<<<<< HEAD
    int numZombies = 100;
    for (int i = 0; i < numZombies; i++)
    {
      Zombie wall = new Zombie(rand.nextInt(numZombies), rand.nextInt(numZombies), 5, 5, 5);
=======
    Color[] colors = { Color.RED, Color.ORANGE, Color.BLACK, Color.BLUE, Color.BEIGE, Color.AZURE, Color.BROWN };
    int currColor = 0;
    int currTexture = 0;
    String[] textures = { "textures/block_texture_dark.jpg", "textures/brick_texture.jpg", "textures/brick_texture2.jpg",
            "textures/crate_texture.jpg", "textures/metal_texture.jpg", "textures/rock_texture.jpg",
            "textures/ice_texture.jpg", "textures/stone_texture.jpg" };

    for (int i = 0; i < 100; i++)
    {
      Zombie wall = new Zombie(textures[currTexture], rand.nextInt(100), rand.nextInt(100), 5, 5, 5);
      renderer.registerActor(wall, new Box(1, 1, 1),
                             colors[currColor], colors[currColor], Color.WHITE);
      currColor++;
      if (currColor >= colors.length) currColor = 0;
>>>>>>> df766e00ddee3e8f60d6bf064c3b5c39eec1247a
      actors.add(wall);
      currTexture++;
      if (currTexture >= textures.length) currTexture = 0;
    }
  }
  
  /*
   ================================================
   Addes these initializers as test cases
   
   ================================================
   */


}
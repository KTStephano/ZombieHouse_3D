package cs351.project1;

import cs351.core.Actor;
import cs351.core.Engine;
import cs351.core.Level;
import cs351.core.World;
import cs351.entities.PlayASoundThingy;
import cs351.entities.Player;
import cs351.entities.Zombie;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

/**
 * Testing the engine while Chris gets his version of the World up and running. My version
 * of the world is scary bad and this shouldn't be how the class works. xD
 */
public class NotTheRealWorld implements World
{
  private HashSet<Actor> changeList = new HashSet<>();
  private Player player;

  private void initPlayASoundThingy()
  {
    changeList.add(new PlayASoundThingy());
  }

  private void initPlayer()
  {
    player = new Player(100.0, 0.0);
    changeList.add(player);
  }

  private void initZombies()
  {
    Random rand = new Random();
    int numZombies = 100;
    for (int i = 0; i < numZombies; i++)
    {
      Zombie wall = new Zombie(rand.nextInt(numZombies), rand.nextInt(numZombies), 5, 5, 5);
      changeList.add(wall);
    }
  }

  @Override
  public boolean contains(Actor actor) {
    return false;
  }

  @Override
  public void remove(Actor actor) throws RuntimeException {

  }

  @Override
  public void add(Actor actor) {

  }

  @Override
  public void add(Level level) {

  }

  @Override
  public int getWorldPixelWidth() {
    return 1;
  }

  @Override
  public int getWorldPixelHeight() {
    return 1;
  }

  @Override
  public void setPixelWidthHeight(int pixelWidth, int pixelHeight) {

  }

  @Override
  public int getTilePixelWidth() {
    return 1;
  }

  @Override
  public int getTilePixelHeight() {
    return 1;
  }

  @Override
  public void setTilePixelWidthHeight(int pixelWidth, int pixelHeight) {

  }

  @Override
  public Actor getPlayer() {
    return player;
  }

  @Override
  public void setPlayer(Actor player) throws RuntimeException {

  }

  @Override
  public Collection<Actor> getChangeList(boolean clearChangeList) {
    if (clearChangeList)
    {
      HashSet<Actor> returnVal = new HashSet<Actor>(changeList);
      changeList.clear();
      return returnVal;
    }
    return changeList;
  }

  @Override
  public boolean hasNextLevel() {
    return true;
  }

  @Override
  public void nextLevel(Engine engine) {
    changeList.clear();
    Color[] colors = { Color.BEIGE };
    String[] textures = { "textures/block_texture_dark.jpg", "textures/brick_texture.jpg", "textures/brick_texture2.jpg",
                          "textures/crate_texture.jpg", "textures/metal_texture.jpg", "textures/rock_texture.jpg",
                          "textures/ice_texture.jpg", "textures/stone_texture.jpg" };
    int currColor = 0;
    int currTexture = 0;
    initZombies();
    for (Actor actor : changeList)
    {
      // register the actor with the renderer
      if (actor instanceof Player) continue;
      engine.getRenderer().registerActor(actor, new Box(actor.getWidth(), actor.getHeight(), actor.getDepth()),
                                         colors[currColor], colors[currColor], Color.WHITE);
      // associate the texture with the actor
      engine.getRenderer().associateDiffuseTextureWithActor(actor, textures[currTexture]);
      currColor++;
      currTexture++;
      if (currColor >= colors.length) currColor = 0;
      if (currTexture >= textures.length) currTexture = 0;
    }
    // set up the player with the renderer
    initPlayer();
    // sets up the thingy
    initPlayASoundThingy();
  }

  @Override
  public void restartLevel(Engine engine) {
    nextLevel(engine);
  }
}

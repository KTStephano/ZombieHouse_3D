package cs351.entities;

import cs351.core.Actor;
import cs351.core.Engine;

public class Exit extends Wall
{
  private boolean endGame = false;

  public Exit(String textureFile, double x, double y, int width, int height, int depth)
  {
    super(textureFile, x, y, width, height, depth);
    isStatic = true; // engine will interpret this in a special way
    shouldUpdate = true; // engine won't waste time calling its update function
  }

  @Override
  public UpdateResult update(Engine engine, double deltaSeconds)
  {
    if (endGame) return UpdateResult.PLAYER_VICTORY;
    return UpdateResult.UPDATE_COMPLETED;
  }

  @Override
  public void collided(Engine engine, Actor actor)
  {
    if (actor instanceof Player)
    {
      System.out.println("Victory!");
      endGame = true;
    }
  }
}

package cs351.entities;

import cs351.core.*;

/*
 * May or may not keep this class. So far just using it for testing purposes, so I can make a Tile
 * object and test it in the Level class. It extends the Actor class
 */
public class Tiles extends Actor
{
  private int pixelWidth;
  private int pixelHeight;
  
  public Tiles(){
    super("");
  }
  public Tiles(String textureFile, int pixelWidth, int pixelHeight){
    super(textureFile);
    this.pixelWidth = pixelWidth;
    this.pixelHeight = pixelHeight;
  }

  @Override
  public UpdateResult update(Engine engine, double deltaSeconds)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void collided(Engine engine, Actor actor)
  {
    // TODO Auto-generated method stub
    
  }

}

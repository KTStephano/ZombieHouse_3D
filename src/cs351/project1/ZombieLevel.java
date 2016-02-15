package cs351.project1;
import cs351.core.*;

public class ZombieLevel implements Level
{
  private int pixelWidth;
  private int pixelHeight;
  /**
   * This should clear out the contents of the given World and then
   * reinitialize it to represent the starting point of whatever
   * Level is being created.
   *
   * @param world World object to initialize/reinitialize
   */
  public ZombieLevel(int pixelWidth, int pixelHeight)
  {
    this.pixelWidth = pixelWidth;
    this.pixelHeight = pixelHeight;
  }
  public void initWorld(World world)
  {
    //calls constructor of Zombieworld
    new ZombieWorld(pixelWidth, pixelHeight);
  }
}

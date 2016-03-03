package cs351.project1;

import java.net.URL;


/**
 * @author Scott Cooper
 */
public class SoundStackItem {
  String soundFile;
  double x;
  double y;
  public SoundStackItem(String soundFile, double x, double y)
  {
    this.soundFile = soundFile;
    this.x = x;
    this.y = y;
  }

  @Override
  public int hashCode()
  {
    return soundFile.hashCode();
  }

  @Override
  public boolean equals(Object other)
  {
    return other instanceof SoundStackItem && soundFile.equals(((SoundStackItem)other).soundFile);
  }
}

package cs351.project1;

import javafx.scene.media.Media;

/**
 * @author Scott Cooper
 */
public class SoundStackItem {
  Media sound;
  int x;
  int y;
  public SoundStackItem(Media sound, int x, int y)
  {
    this.sound = sound;
    this.x = x;
    this.y = y;
  }
}

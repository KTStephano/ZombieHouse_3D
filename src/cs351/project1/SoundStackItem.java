package cs351.project1;

import javafx.scene.media.AudioClip;


/**
 * @author Scott Cooper
 */
public class SoundStackItem {
  AudioClip sound;
  int x;
  int y;
  public SoundStackItem(AudioClip sound, int x, int y)
  {
    this.sound = sound;
    this.x = x;
    this.y = y;
  }
}

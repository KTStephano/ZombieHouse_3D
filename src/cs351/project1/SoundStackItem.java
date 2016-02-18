package cs351.project1;

import javafx.scene.media.AudioClip;


/**
 * @author Scott Cooper
 */
public class SoundStackItem {
  AudioClip sound;
  double x;
  double y;
  public SoundStackItem(AudioClip sound, double x, double y)
  {
    this.sound = sound;
    this.x = x;
    this.y = y;
  }
}

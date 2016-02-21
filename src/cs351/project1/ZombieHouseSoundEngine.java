package cs351.project1;

import cs351.core.SoundEngine;
import java.util.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.scene.media.AudioClip;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author Scott Cooper
 */
public class ZombieHouseSoundEngine implements SoundEngine {
  private Point centralPoint = new Point();
  private Stack<SoundStackItem> soundStack = new Stack<>();
  static SoundStackItem  tmpSoundStackItem;
  private static final double MAX_VOLUME = 1.0;

  @Override
  public void setCentralPoint(double x, double y) 
  {
    centralPoint.x = (int)x;
    centralPoint.y = (int)y;
  }

  @Override
  public void queueSoundAtLocation(URL url, double x, double y) 
  {
    soundStack.push(new SoundStackItem(url,x,y));   
  }

  @Override
  public void update() {



    // NOTE: x, y distance to centralPoint = sqrt((cp.x - x)^2 + (cp.y-y)^2)
    // determines volume during playback

    while (!soundStack.isEmpty()) 
    {
      tmpSoundStackItem = soundStack.pop();

      float relativeDistance = ((float)tmpSoundStackItem.x-centralPoint.x)*((float)tmpSoundStackItem.x-centralPoint.x)+((float)tmpSoundStackItem.y-centralPoint.y)*((float)tmpSoundStackItem.y-centralPoint.y);      
      float soundVolume = 30/relativeDistance;
      if (soundVolume > 1) 
      {
        soundVolume = 1;
      }

      if ( (soundVolume > 0.5))
      {      
        try {
          ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
          URL url = classLoader.getResource("zombie.wav");
          AudioInputStream input = AudioSystem.getAudioInputStream(url);
          Clip clip = AudioSystem.getClip();
          clip.open(input);
          setVolume((int)(soundVolume*100), clip);
          clip.start();
        } catch (UnsupportedAudioFileException e1) {
        } catch (IOException e1) {
        } catch (LineUnavailableException e) {
        }
      }
    }


  }
  
  public void setVolume(int percent, Clip clip) {
    FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    float max = volume.getMaximum();
    float min = volume.getMinimum();
    float range = 0;
    if (max < 0) {
      range = Math.abs(min) - Math.abs(max);
    } else {
      if (min >= 0) {
        range = max - min;
      } else {
        range = Math.abs(min) + max;
      }
    }
    float value = percent * range / 100F;
    volume.setValue(min + value);
  }


}

package cs351.project1;

import cs351.core.SoundEngine;
import java.util.*;

import javafx.scene.media.AudioClip;
import java.awt.Point;

/**
 * @author Scott Cooper
 */
public class ZombieHouseSoundEngine implements SoundEngine {
  private Point centralPoint = new Point();
  private final int GREATEST_DISTANCE_ACROSS_ROOM = 50;
  private Stack<SoundStackItem> soundStack = new Stack<>();
  
  @Override
  public void setCentralPoint(int x, int y) 
  {
    centralPoint.x = x;
    centralPoint.y = y;
  }

  @Override
  public void queueSoundAtLocation(AudioClip sound, int x, int y) 
  {
    soundStack.push(new SoundStackItem(sound,x,y));   
  }

  @Override
  public void update() {
    SoundStackItem tmpSoundStackItem;


    // NOTE: x, y distance to centralPoint = sqrt((cp.x - x)^2 + (cp.y-y)^2)
    // determines volume during playback
    
    // Below is a coarse single threaded 
    // implementation of a sound engine 
    // media player.
    
    //  This was created as a test to see how
    //  well a very basic approach would work.
    while (!soundStack.isEmpty()) 
    {
      tmpSoundStackItem = soundStack.pop();
      /*
      tmpSoundStackItem.sound.setVolume(1-Math.sqrt(  (tmpSoundStackItem.x-centralPoint.x)*(tmpSoundStackItem.x-centralPoint.x)+(tmpSoundStackItem.y-centralPoint.y)*(tmpSoundStackItem.y-centralPoint.y) ) / GREATEST_DISTANCE_ACROSS_ROOM);
          
      if ((tmpSoundStackItem.x-centralPoint.x)!=0)
      {
        tmpSoundStackItem.sound.setBalance(  (tmpSoundStackItem.x-centralPoint.x) / (tmpSoundStackItem.x-centralPoint.x));       
      } else
      {
        tmpSoundStackItem.sound.setBalance(0);
      }
        */    
  
      
     tmpSoundStackItem.sound.play();
    }
    
    
  }

}

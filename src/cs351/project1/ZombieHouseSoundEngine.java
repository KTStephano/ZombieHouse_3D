package cs351.project1;

import cs351.core.SoundEngine;
import java.util.*;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.awt.Point;

public class ZombieHouseSoundEngine implements SoundEngine {
  
  private Point centralPoint;
  private Stack<SoundStackItem> soundStack = new Stack<>();
  
  @Override
  public void setCentralPoint(int x, int y) 
  {
    centralPoint.x = x;
    centralPoint.y = y;
  }

  @Override
  public void queueSoundAtLocation(Media sound, int x, int y) 
  {
    soundStack.push(new SoundStackItem(sound,x,y));   
  }

  @Override
  public void update() {
    SoundStackItem tmpSoundStackItem;
    MediaPlayer soundPlayer;
    // TODO Auto-generated method stub

    // NOTE: x, y distance to centralPoint = sqrt((cp.x - x)^2 + (cp.y-y)^2)
    // determines volume during playback
    
    // Below is a coarse single threaded 
    // implementation of a sound engine 
    // media player.
    
    //  This was created as a test to see how
    //  well a very basic approach would work.
    while (soundStack.isEmpty()) 
    {
      tmpSoundStackItem = soundStack.pop();
      soundPlayer = new MediaPlayer(tmpSoundStackItem.sound);
      soundPlayer.play(); 
     }
    
    
  }

}

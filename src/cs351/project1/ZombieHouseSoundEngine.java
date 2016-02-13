package cs351.project1;

import cs351.core.SoundEngine;
import java.util.*;

import javafx.scene.media.Media;
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
    // TODO Auto-generated method stub

    // NOTE: x, y distance to centralPoint = sqrt((cp.x - x)^2 + (cp.y-y)^2)
    // determines volumne during playback
    
    
  }

}

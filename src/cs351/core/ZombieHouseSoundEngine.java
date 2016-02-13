package cs351.core;

import javafx.scene.media.Media;
import java.awt.Point;
public class ZombieHouseSoundEngine implements SoundEngine {
  private Point centralPoint;
  @Override
  public void setCentralPoint(int x, int y) 
  {
    centralPoint.x = x;
    centralPoint.y = y;
  }

  @Override
  public void queueSoundAtLocation(Media sound, int x, int y) 
  {
    // TODO establish priority queue containing
    // sound and distance to centralPoint = sqrt((cp.x - x)^2 + (cp.y-y)^2)
    
  }

  @Override
  public void update() {
    // TODO Auto-generated method stub
    
  }

}

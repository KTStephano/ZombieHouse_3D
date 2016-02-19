package cs351.entities;

import cs351.core.Actor;
import cs351.core.Engine;
//import javafx.scene.media.AudioClip;
//import java.net.URL;


/**
 * All this does is play a sound randomly. Just a test.
 */
public class PlayASoundThingy extends Actor
{
//  private double elapsedSeconds = 0.0;

  public PlayASoundThingy()
  {
    // this thingy doesn't need a texture
    super("");
    noClip = true;
  }

  @Override
  public UpdateResult update(Engine engine, double deltaSeconds)
  {

  /*  elapsedSeconds+= deltaSeconds;
    if (elapsedSeconds >= 3.0)

    {
      elapsedSeconds = 0.0;
      final URL resource = getClass().getClassLoader().getResource("cs351/entities/sound/zombie.mp3");
      final AudioClip media = new AudioClip(resource.toString());

      engine.getSoundEngine().queueSoundAtLocation(media, this.getLocation().getX(), this.getLocation().getY());
    }
    */
    return UpdateResult.UPDATE_COMPLETED;
  }

  @Override
  public void collided(Engine engine, Actor actor)
  {
  }
}

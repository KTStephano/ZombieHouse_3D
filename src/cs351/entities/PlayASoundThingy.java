package cs351.entities;

import cs351.core.Actor;
import cs351.core.Engine;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

import java.net.URL;
import java.util.Random;

/**
 * All this does is play a sound randomly. Just a test.
 */
public class PlayASoundThingy extends Actor
{
  private Random rand = new Random();
  private double elapsedSeconds = 0.0;

  public PlayASoundThingy()
  {
    // this thingy doesn't need a texture
    super("");
  }

  @Override
  public UpdateResult update(Engine engine, double deltaSeconds)
  {
    elapsedSeconds += deltaSeconds;
    if (elapsedSeconds >= 5.0)
    {
      elapsedSeconds = 0.0;
      final URL resource = getClass().getClassLoader().getResource("cs351/entities/sound/zombie.mp3");
      final AudioClip media = new AudioClip(resource.toString());
      // TODO make soundengine use doubles for x and y
      engine.getSoundEngine().queueSoundAtLocation(media, (int)this.getLocation().getX(), (int)this.getLocation().getY());
    }
    return UpdateResult.UPDATE_COMPLETED;
  }

  @Override
  public void collided(Engine engine, Actor actor)
  {
  }
}

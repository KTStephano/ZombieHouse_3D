package cs351.entities;

import cs351.core.Actor;
import cs351.core.Engine;
import javafx.scene.media.Media;

import java.net.URL;
import java.util.Random;

/**
 * All this does is play a sound randomly. Just a test.
 */
public class PlayASoundThingy extends Actor
{
  private Random rand = new Random();
  @Override
  public UpdateResult update(Engine engine, double deltaSeconds)
  {
    if (rand.nextInt(200) >= 198)
    {
      final URL resource = getClass().getClassLoader().getResource("cs351/entities/sound/zombie.mp3");
      final Media media = new Media(resource.toString());
      engine.getSoundEngine().queueSoundAtLocation(media, 0, 0);
    }
    return UpdateResult.UPDATE_COMPLETED;
  }

  @Override
  public void collided(Engine engine, Actor actor) {

  }
}

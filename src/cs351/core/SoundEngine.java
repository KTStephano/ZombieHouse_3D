package cs351.core;

import javafx.scene.media.AudioClip;

/**
 * The idea behind a SoundEngine is that each frame a number of objects
 * can request that a sound be played relative to a central point (ex: a player location).
 *
 * Sounds that are closer to the specified central point should play at
 * the maximum default volume, while sounds further away from the central
 * point should decrease in volume in some way.
 */
public interface SoundEngine
{
  /**
   * When this function is called the old central point should be overridden
   * and all future requests to play sounds should use the new point
   * in their volume calculations.
   *
   * @param x center x-coordinate
   * @param y center y-coordinate
   */
  void setCentralPoint(double x, double y);

  /**
   * This function should not immediately play the sound at location
   * (x, y) but should instead add it to a queue. When the SoundEngine is told
   * to flush all sounds from its queue, that is the point when all sounds
   * should be played.
   *
   * The reason for this delay is so that the SoundEngine can blend/merge/etc. the
   * sounds it gets so that they sound better to the user.
   *
   * @param sound Media object to play
   * @param x x-coordinate where the sound started
   * @param y y-coordinate where the sound started
   */
  void queueSoundAtLocation(AudioClip sound, double x, double y);

  /**
   * When this function is called, all sounds that were added to the sound queue
   * through the queueSoundAtLocation function should be played appropriately.
   */
  void update();
}

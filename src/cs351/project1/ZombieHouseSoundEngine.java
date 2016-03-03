package cs351.project1;

import cs351.core.Engine;
import cs351.core.SoundEngine;
import cs351.core.Vector3;
import cs351.entities.Player;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.io.InputStream;
import java.util.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import java.awt.Point;
import java.net.URL;

/**
 * Citation: http://www.vttoth.com/CMS/index.php/technical-notes/68
 *
 * Citation: http://what-when-how.com/javafx-2/playing-audio-using-the-media-classes-javafx-2-part-1/
 *
 * @author Scott Cooper
 */
public class ZombieHouseSoundEngine implements SoundEngine {
  private Point centralPoint = new Point();
  private Stack<SoundStackItem> soundStack = new Stack<>();
  //private HashMap<Integer, HashMap<String, MediaPlayer>> availableSounds = new HashMap<>(60);
  private HashMap<Integer, HashMap<String, MediaPlayer>> availableSounds = new HashMap<>();
  private HashMap<Integer, HashSet<MediaPlayer>> activeSounds = new HashMap<>(3);
  private int currentActiveSoundList = 0;
  private int maxActiveSounds = 2;
  private HashSet<MediaPlayer> soundBackBuffer = new HashSet<>(); // sounds waiting to be pushed to activeSounds
  static SoundStackItem  tmpSoundStackItem;
  private float playerHearing = 1.0f;
  private Vector3 playerLookDir = new Vector3(0.0), playerRightDir = new Vector3(0.0);

  @Override
  public void setCentralPoint(double x, double y) 
  {
    centralPoint.x = (int)x;
    centralPoint.y = (int)y;
  }

  @Override
  public void queueSoundAtLocation(String sound, double x, double y)
  {
    soundStack.push(new SoundStackItem(sound,x,y));
  }

  @Override
  public void update(Engine engine) {

    // Get the look direction and right direction of the player for left-right headphone calculations
    playerLookDir.set(((Player)engine.getWorld().getPlayer()).getForwardVector());
    playerRightDir.set(((Player)engine.getWorld().getPlayer()).getRightVector());
    playerRightDir.normalize();
    // NOTE: x, y distance to centralPoint = sqrt((cp.x - x)^2 + (cp.y-y)^2)
    // determines volume during playback

    while (!soundStack.isEmpty()) 
    {
      tmpSoundStackItem = soundStack.pop();

      float relativeDistance = (float)Math.sqrt(
               ((float)tmpSoundStackItem.x-centralPoint.x)*((float)tmpSoundStackItem.x-centralPoint.x)
              +((float)tmpSoundStackItem.y-centralPoint.y) *((float)tmpSoundStackItem.y-centralPoint.y));
      //if (relativeDistance < playerHearing) System.out.println("DIST: " + relativeDistance);
      if (relativeDistance == 0.0) relativeDistance = 1.0f;
      float soundVolume = 1.0f - relativeDistance / playerHearing;
      //System.out.println("RAW VOL: " + soundVolume);
      if (soundVolume < 0.0f) continue;
      playSound(tmpSoundStackItem, soundVolume);
    }

    //HashMap<String, MediaPlayer> availableSounds = this.availableSounds.get(frameNumber);
    activeSounds.get(currentActiveSoundList).addAll(soundBackBuffer);
    final float VOL_DIVISION = 0.25f;
    for (MediaPlayer player : soundBackBuffer)
    {
      player.setVolume(player.getVolume() * VOL_DIVISION);
      player.play();
    }
    soundBackBuffer.clear();
    //activeSounds.clear();
    currentActiveSoundList++;
    if (currentActiveSoundList >= maxActiveSounds) currentActiveSoundList = 0;
  }

  @Override
  public void init(Engine engine)
  {
    // Ask the engine what the player hearing is (through the Settings class)
    playerHearing = Float.parseFloat(engine.getSettings().getValue("player_hearing"));
    for (int i = 0; i < maxActiveSounds; i++)
    {
      if (!activeSounds.containsKey(i)) activeSounds.put(i, new HashSet<>());
      else
      {
        for (MediaPlayer player : activeSounds.get(i)) player.dispose();
      }
      activeSounds.get(i).clear();
    }
    //System.out.println("HEARING " + playerHearing);
  }

  public void playSound(SoundStackItem item, double vol ) {

    try
    {
      // extract the available media players for this frame
      MediaPlayer player;
      // If the sound has been loaded before, don't reload it - just get the active media
      // player for the sound
      /**
       * Right now the SoundEngine supports playing 2 different streams of sounds where each stream
       * can contain a sound that is in another stream. This lets actors queue duplicate sounds
       * without us being overwhelmed by their audio.
       */
      if (!availableSounds.containsKey(currentActiveSoundList)) availableSounds.put(currentActiveSoundList, new HashMap<>());
      if (availableSounds.get(currentActiveSoundList).containsKey(item.soundFile))
      {
        player = availableSounds.get(currentActiveSoundList).get(item.soundFile);
      }
      // Otherwise load the file and store it in the hash map
      else
      {
        //AudioInputStream stream = AudioSystem.getAudioInputStream(ZombieHouseSoundEngine.class.getResource(item.soundFile));
        URL resource = ZombieHouseSoundEngine.class.getResource(item.soundFile);
        player = new MediaPlayer(new Media(resource.toString()));
        player.setVolume(0.0);
        final int CURR_ACTIVE_SOUNDS_LIST = currentActiveSoundList;
        player.setOnEndOfMedia(() ->
        {
          player.setVolume(0.0);
          player.seek(new Duration(0));
          player.stop();
          activeSounds.get(CURR_ACTIVE_SOUNDS_LIST).remove(player);
          //System.out.println("SIZE : " + activeSounds.size());
        });
        availableSounds.get(CURR_ACTIVE_SOUNDS_LIST).put(item.soundFile, player);
        player.balanceProperty().set(0.0);
      }
      if (activeSounds.get(currentActiveSoundList).contains(player)) return; // already playing, let it finish
      soundBackBuffer.add(player);
      float mergedVolume = (float)(player.getVolume() + vol - player.getVolume() * vol);
      //float leftRightBalance = (float)()
      //System.out.println("VOL: " + mergedVolume);
      //if (mergedVolume < 0.1) return;
      //final float MAX = 0.5f;
      //if (mergedVolume > MAX) mergedVolume = MAX;
      player.setVolume(mergedVolume);
      //player.play();
      /*
      AudioInputStream input = AudioSystem.getAudioInputStream(url);
      Clip clip = AudioSystem.getClip();
      clip.addLineListener(new LineListener() {
        public void update(LineEvent myLineEvent) {
          if (myLineEvent.getType() == LineEvent.Type.STOP)
            clip.close();
        }
      });
      clip.open(input);
      setVolume((int)(vol*100), clip);
      clip.start();
      */
     
    } catch (Exception e) {
      System.out.println("wat did u do");
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

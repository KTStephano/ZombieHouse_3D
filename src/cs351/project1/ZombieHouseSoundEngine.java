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

    // Below is a coarse single threaded 
    // implementation of a sound engine 
    // media player.

    //  This was created as a test to see how
    //  well a very basic approach would work.
    while (!soundStack.isEmpty()) 
    {
      tmpSoundStackItem = soundStack.pop();

      float relativeDistance = ((float)tmpSoundStackItem.x-centralPoint.x)*((float)tmpSoundStackItem.x-centralPoint.x)+((float)tmpSoundStackItem.y-centralPoint.y)*((float)tmpSoundStackItem.y-centralPoint.y);      
      float soundVolume = 30/relativeDistance;
      if (soundVolume > 1) 
      {
        soundVolume = 1;
      }
      /*
      tmpSoundStackItem.sound.setVolume(soundVolume);  

      if ((tmpSoundStackItem.x-centralPoint.x)!=0)
      {
        tmpSoundStackItem.sound.setBalance(  (tmpSoundStackItem.x-centralPoint.x) / (tmpSoundStackItem.x-centralPoint.x));       
      } else
      {
        tmpSoundStackItem.sound.setBalance(0);
      }
       */
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



        /*
        int BUFFER_SIZE = 128000;
        AudioInputStream audioStream;
        SourceDataLine sourceLine;
        /**
         * @param filename the name of the file that is going to be played



        try {
          audioStream = AudioSystem.getAudioInputStream(new File("cs351/entities/sound/zombie.mp3" ));

            sourceLine = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, audioStream.getFormat()));
            sourceLine.open(audioStream.getFormat());
            sourceLine.start();           

            int nBytesRead = 0;
            byte[] abData = new byte[BUFFER_SIZE];
            while (nBytesRead != -1) {
                try {
                    nBytesRead = audioStream.read(abData, 0, abData.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (nBytesRead >= 0) {
                    @SuppressWarnings("unused")
                    int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
                }
            }
            sourceLine.drain();
            sourceLine.close();

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


         */




        //      try {
        /*

          File soundFile = new File( "cs351/entities/sound/zombie.mp3" );
          AudioInputStream audioInputStream = AudioSystem.getAudioInputStream( soundFile );
          Clip clip = AudioSystem.getClip();
          clip.open(audioInputStream);
          clip.start();//This plays the audio
         */

        /*
          Clip clip = AudioSystem.getClip();
          AudioInputStream ais;
          ais = AudioSystem.getAudioInputStream( tmpSoundStackItem.url );
          clip.open( ais);

          FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
          volume.setValue(1.0f); // Reduce volume by 10 decibels.

          clip.start();
          //clip.loop(1);
         * 
         * 
         */
        // } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        // e.printStackTrace();
        //}

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

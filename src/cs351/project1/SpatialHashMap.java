package cs351.project1;

import cs351.core.Actor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Citation: http://www.gamedev.net/page/resources/_/technical/game-programming/spatial-hashing-r2697
 *
 * A spatial hash map is used to divide some space into a series of buckets in order to
 * better decide which objects are close to each other. For example, if you have a grid that
 * is 500 pixels by 500 pixels and a tile width/height of 50 pixels, this class will create
 * a spatial hash map that is 10 buckets by 10 buckets.
 *
 * In order to decide which bucket(s) an object goes in, the hashX and hashY functions are used.
 * These functions convert an (x, y) coordinate into a hashed pair representing an index
 * into a 2D array of buckets. From there the ending x and y are calculated by adding the
 * object's
 */
public class SpatialHashMap implements Iterable<Collection<Actor>>
{
  private final Bucket[][] BUCKETS;
  private final LinkedList<Bucket> BUCKET_LIST; // :P
  private final int CELL_SIZE_X, CELL_SIZE_Y;
  private final int NUM_BUCKETS_X, NUM_BUCKETS_Y;

  private class Bucket
  {
    private final HashSet<Actor> ACTORS = new HashSet<>(50);

    public boolean contains(Actor actor)
    {
      return ACTORS.contains(actor);
    }

    public void clear()
    {
      ACTORS.clear();
    }

    public HashSet<Actor> contents()
    {
      return ACTORS;
    }

    public void insert(Actor actor)
    {
      ACTORS.add(actor);
    }
  }

  public SpatialHashMap(int worldPixelWidth, int worldPixelHeight,
                        int tilePixelWidth, int tilePixelHeight)
  {
    CELL_SIZE_X = tilePixelWidth;
    CELL_SIZE_Y = tilePixelHeight;
    NUM_BUCKETS_X = worldPixelWidth / tilePixelWidth + 1; // add some padding
    NUM_BUCKETS_Y = worldPixelHeight / tilePixelHeight + 1; // add some padding
    BUCKETS = new Bucket[NUM_BUCKETS_X][NUM_BUCKETS_Y];
    BUCKET_LIST = new LinkedList<>();
    initBuckets();
  }

  @Override
  public Iterator<Collection<Actor>> iterator()
  {
    return new Iterator<Collection<Actor>>()
    {
      Iterator<Bucket> iter = BUCKET_LIST.iterator();

      @Override
      public boolean hasNext()
      {
        return iter.hasNext();
      }

      @Override
      public Collection<Actor> next()
      {
        return iter.next().contents();
      }
    };
  }

  public void clear()
  {
    for (int x = 0; x < NUM_BUCKETS_X; x++)
    {
      for (int y = 0; y < NUM_BUCKETS_Y; y++)
      {
        BUCKETS[x][y].clear();
      }
    }
  }

  public void insert(Actor actor)
  {
    int hashedX = hashX(actor.getLocation().getX());
    int hashedY = hashY(actor.getLocation().getY());
    // the division by CELL_SIZE_X/Y is to convert an object's width/height in pixels into
    // its width/height in tiles
    int endX = hashedX + actor.getWidth() / CELL_SIZE_X + 1;
    int endY = hashedY + actor.getHeight() / CELL_SIZE_Y + 1;
    // if the actor is outside of the world (maybe no-clip is active or something), don't bother
    // adding it
    if (hashedX < 0 || hashedX >= NUM_BUCKETS_X || hashedY < 0 || hashedY >= NUM_BUCKETS_Y) return;
    if (endX > NUM_BUCKETS_X) endX = NUM_BUCKETS_X; // bounds checking
    if (endY > NUM_BUCKETS_Y) endY = NUM_BUCKETS_Y; // bound checking
    for (int x = hashedX; x < endX; x++)
    {
      for (int y = hashedY; y < endY; y++)
      {
        BUCKETS[x][y].insert(actor);
      }
    }
  }

  private int hashX(double x)
  {
    return (int)x / CELL_SIZE_X;
  }

  private int hashY(double y)
  {
    return (int)y / CELL_SIZE_Y;
  }

  private void initBuckets()
  {
    for (int x = 0; x < NUM_BUCKETS_X; x++)
    {
      for (int y = 0; y < NUM_BUCKETS_Y; y++)
      {
        BUCKETS[x][y] = new Bucket();
        BUCKET_LIST.add(BUCKETS[x][y]);
      }
    }
  }
}

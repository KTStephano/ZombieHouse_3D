package cs351.project1;

import cs351.core.Actor;
import cs351.core.Engine;

import java.util.*;

/**
 * See https://developer.mozilla.org/en-US/docs/Games/Techniques/2D_collision_detection
 * for more information.
 *
 * This class is used to detect and respond to collision events between two actors. It makes
 * heavy use of two spatial hash maps (one for moving objects and one for static objects)
 * to cut down on the number of collision checks it has to do each frame.
 */
// TODO figure out why the camera sometimes shakes a little during collision
public class CollisionDetection
{
  private final SpatialHashMap MOVING_ENTITIES;
  private final SpatialHashMap STATIC_ENTITIES;
  private final Engine ENGINE;
  private final HashMap<Actor, BoundingCircle> ACTOR_CIRCLE_MAP;
  private final ArrayList<Actor> ACTOR_BUFFER = new ArrayList<>(100);
  private final HashMap<Actor, LinkedList<Actor>> COLLISIONS = new HashMap<>(100);

  private class BoundingCircle
  {
    private final Actor ACTOR;
    private final double RADIUS;
    private double actorX;
    private double actorY;
    private double otherActorX;
    private double otherActorY;
    private double otherActorRadius;
    private double roughDistanceBetweenThisAndOther;

    public BoundingCircle(Actor actor)
    {
      ACTOR = actor;
      RADIUS = actor.getWidth() / 2.0;
    }

    /**
     * The idea with this function is that it first checks to see if
     * the actor stored in the class has overlapped with the given actor. If it has
     * it calculates x and y offsets and pushes this actor away from the other.
     *
     * @param other actor to check for collision with
     * @return true if the given actor collided with the stored actor and false if not
     */
    public boolean processCollision(Actor other)
    {
      if (!collided(other) || ACTOR.noClipActive() || other.noClipActive()) return false;
      double dx = actorX - otherActorX;
      double dy = actorY - otherActorY;
      double xOffsetDirection = dx;// < 0 ? -1 : 1;
      double yOffsetDirection = dy;// < 0 ? -1 : 1;
      double roughOffset = (RADIUS + otherActorRadius - roughDistanceBetweenThisAndOther);
      // if either is static, add the total offset to just one of the actors
      if (ACTOR.isStatic()) other.setLocation(other.getLocation().getX() + roughOffset * -xOffsetDirection,
                                              other.getLocation().getY() + roughOffset * -yOffsetDirection);
      else if (other.isStatic()) ACTOR.setLocation(ACTOR.getLocation().getX() + roughOffset * xOffsetDirection,
                                                   ACTOR.getLocation().getY() + roughOffset * yOffsetDirection);
      else
      {
        //other.setLocation(other.getLocation().getX() + roughOffset * -xOffsetDirection,
                          //other.getLocation().getY() + roughOffset * -yOffsetDirection);
        ACTOR.setLocation(ACTOR.getLocation().getX() + roughOffset * xOffsetDirection,
                          ACTOR.getLocation().getY() + roughOffset * yOffsetDirection);
      }
      return true;
    }

    private boolean collided(Actor other)
    {
      actorX = ACTOR.getLocation().getX();
      actorY = ACTOR.getLocation().getY();
      otherActorX = other.getLocation().getX();
      otherActorY = other.getLocation().getY();
      otherActorRadius = other.getWidth() / 2.0;

      double dx = actorX - otherActorX;
      double dy = actorY - otherActorY;
      roughDistanceBetweenThisAndOther = Math.sqrt(dx * dx + dy * dy);
      if (roughDistanceBetweenThisAndOther < RADIUS + otherActorRadius)
      {
        return true; // the two have collided
      }
      return false;
    }
  }

  public CollisionDetection(Engine engine)
  {
    int worldPixelWidth = engine.getWorld().getWorldPixelWidth();
    int worldPixelHeight = engine.getWorld().getWorldPixelHeight();
    int tilePixelWidth = engine.getWorld().getTilePixelWidth();
    int tilePixelHeight = engine.getWorld().getTilePixelHeight();

    ENGINE = engine;
    MOVING_ENTITIES = new SpatialHashMap(worldPixelWidth, worldPixelHeight,
                              tilePixelWidth, tilePixelHeight);
    STATIC_ENTITIES = new SpatialHashMap(worldPixelWidth, worldPixelHeight,
                                         tilePixelWidth, tilePixelHeight);
    ACTOR_CIRCLE_MAP = new HashMap<>(50);
  }

  public void initFrame()
  {
    // only clear the moving entities since they are changing constantly
    MOVING_ENTITIES.clear();
  }

  public void insert(Actor actor)
  {
    if (!ACTOR_CIRCLE_MAP.containsKey(actor)) ACTOR_CIRCLE_MAP.put(actor, new BoundingCircle(actor));
    if (!actor.isStatic()) MOVING_ENTITIES.insert(actor);
    else STATIC_ENTITIES.insert(actor);
  }

  public HashMap<Actor, LinkedList<Actor>> detectCollisions()
  {
    Iterator<Collection<Actor>> iterMoving = MOVING_ENTITIES.iterator();
    Iterator<Collection<Actor>> iterStatic = STATIC_ENTITIES.iterator();
    COLLISIONS.clear();
    while (iterMoving.hasNext())
    {
      ACTOR_BUFFER.clear();
      ACTOR_BUFFER.addAll(iterMoving.next());
      ACTOR_BUFFER.addAll(iterStatic.next());
      for (int x = 0; x < ACTOR_BUFFER.size(); x++)
      {
        Actor outer = ACTOR_BUFFER.get(x);
        for (int y = x + 1; y < ACTOR_BUFFER.size(); y++)
        {
          Actor inner = ACTOR_BUFFER.get(y);
          if (outer.isStatic() && inner.isStatic()) continue; // this would be true for two walls sitting next to each other
          if (ACTOR_CIRCLE_MAP.get(outer).processCollision(inner))
          {
            if (!COLLISIONS.containsKey(outer)) COLLISIONS.put(outer, new LinkedList<>());
            COLLISIONS.get(outer).add(inner);
          }
        }
      }
    }
    return COLLISIONS;
  }
}

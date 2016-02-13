package cs351.core;

/**
 * A Tile represents either part of the floor or part of the ceiling. The Engine
 * will not check for collisions on either one of them so that all other Actors
 * can walk over them.
 *
 * The reason they extend Actor is because we might want to update some of
 * the Tiles each frame to see if a zombie needs to spawn on one.
 *
 * TODO: Need to finish this - still figuring out what all needs to be part of the base Actor classes/subclasses
 */
public abstract class Tile extends Actor
{
  protected boolean isPartOfFloor; // true if floor tile, false if ceiling tile
}

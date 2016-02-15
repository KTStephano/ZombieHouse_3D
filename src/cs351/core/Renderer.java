package cs351.core;

import javafx.scene.paint.Color;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Shape3D;

/**
 * The Renderer is meant to manage the final translation/rotation of all registered
 * models, lights, etc. in order to render a scene each frame.
 *
 * TODO need to finish this interface
 */
public interface Renderer
{
  /**
   * When this is called this will walk through all of the actors that have
   * been registered with it and render them relative to the player.
   *
   * @param mode if this is set to DrawMode.FILL the objects will appear solid, but if
   *             this is set to DrawMode.LINE everything will look like it is a wire-frame model
   */
  void render(DrawMode mode);

  /**
   * This is how you let the renderer know which actor is the player in the scene. It will
   * automatically attach a camera to this actor.
   *
   * @param player the game's player
   * @param fieldOfView this determines how much the player can see on the screen - normal FOV is 45.0 degrees
   *                    and a large FOV is 90.0 degrees
   */
  void registerPlayer(Actor player, double fieldOfView);

  /**
   * Registers an Actor object with the renderer. What this does is to basically map an Actor object to
   * an object that should be rendered in the world. Shape3D can be things like a sphere, box, etc. (any
   * valid Shape3D from JavaFX).
   *
   * @param actor object to add to the scene
   * @param shape shape of the object
   * @param diffuseColor this is the color the object scatters in all directions when light hits it
   * @param specularColor this is the color the object reflects in one direction when light hits it -
   *                      results in a shiny circles on the surface
   * @param ambientColor base color of the object
   */
  void registerActor(Actor actor, Shape3D shape, Color diffuseColor, Color specularColor, Color ambientColor);

  /**
   * To add a tile to the scene which is either part of the floor or part of the ceiling, use this function.
   *
   * @param tile object to add as a floor/ceiling object
   * @param shape shape of the object
   * @param diffuseColor this is the color the object scatters in all directions when light hits it
   * @param specularColor this is the color the object reflects in one direction when light hits it -
   *                      results in a shiny circles on the surface
   * @param ambientColor base color of the object
   */
  void registerStaticTile(Tile tile, Shape3D shape, Color diffuseColor, Color specularColor, Color ambientColor);
}
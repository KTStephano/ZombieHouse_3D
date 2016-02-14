package cs351.project1;

import cs351.core.*;
import cs351.entities.Player;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ZombieHouseRenderer implements Renderer
{
  private final Scene SCENE;
  private PerspectiveCamera camera;
  private Translate cameraTranslation;
  private Rotate cameraRotation;
  private Player player;
  private Group renderSceneGraph; // camera and all actors are added to this
  private SubScene renderScene; // renderSceneGraph added to this for redraw each frame
  private final HashMap<Actor, Model> ACTOR_MODEL_MAP = new HashMap<>(50);
  private final HashMap<Tile, Model> TILE_MODEL_MAP = new HashMap<>(50);

  private class Model
  {
    public Mesh mesh; // if a custom model was loaded
    public Shape3D shape; // if one of the default shapes was used
    public Material material; // usually PhongMaterial
    public ArrayList<PointLight> lights;
    public Translate translation; // this is used to position the model within the scene
    public Rotate rotation; // this is used to rotate the model about the origin within the scene
  }

  public ZombieHouseRenderer(Stage stage, int width, int height)
  {
    renderSceneGraph = new Group();
    renderScene = new SubScene(renderSceneGraph, width, height, true, SceneAntialiasing.BALANCED);
    renderScene.setFill(Color.GRAY);
    Group renderSceneGroup = new Group();
    renderSceneGroup.getChildren().add(renderScene);
    SCENE = new Scene(renderSceneGroup);
    stage.setScene(SCENE);
  }

  @Override
  public void render(DrawMode mode)
  {
    // orient the player to their rotation/location
    orientPlayerToScene();

    // render the tiles
    renderFloorAndCeiling(mode);

    // render the actors
    renderActors(mode);
  }

  @Override
  public void registerPlayer(Actor player, double fieldOfView)
  {
    camera = new PerspectiveCamera(true);
    camera.setFieldOfView(fieldOfView);
    renderSceneGraph.getChildren().add(camera);
    renderScene.setCamera(camera);
    this.player = (Player)player;
    cameraTranslation = new Translate(this.player.getLocation().getX(), 0.0, this.player.getLocation().getY());
    cameraRotation = new Rotate(0.0, 0.0, 0.0);
    camera.getTransforms().addAll(cameraTranslation, cameraRotation);
    SCENE.setOnKeyPressed(this.player::keyPressed);
    SCENE.setOnKeyReleased(this.player::keyReleased);
  }

  @Override
  public void registerActor(Actor actor, Shape3D shape, Color diffuseColor, Color specularColor, Color ambientColor)
  {
    Model model = generateModel(shape, diffuseColor, specularColor, ambientColor);
    ACTOR_MODEL_MAP.put(actor, model);
    renderSceneGraph.getChildren().addAll(model.shape);
  }

  @Override
  public void registerStaticTile(Tile tile, Shape3D shape, Color diffuseColor, Color specularColor, Color ambientColor)
  {
    Model model = generateModel(shape, diffuseColor, specularColor, ambientColor);
    TILE_MODEL_MAP.put(tile, model);
    renderSceneGraph.getChildren().add(model.shape);
  }

  private Model generateModel(Shape3D shape, Color ambientColor, Color diffuseColor, Color specularColor)
  {
    Model model = new Model();
    model.shape = shape;
    PhongMaterial material = new PhongMaterial(ambientColor);
    material.setDiffuseColor(diffuseColor);
    material.setSpecularColor(specularColor);
    model.material = material;
    shape.setMaterial(material);
    model.lights = new ArrayList<>(5);
    model.translation = new Translate(0.0, 0.0, 0.0);
    model.rotation = new Rotate(0.0, 0.0, 0.0);
    // TODO come up with a better way to make the player see eye-to-eye with things the same height as them
    if (player != null) model.translation.setY(player.getHeight() / 3.0);
    shape.getTransforms().addAll(model.rotation, model.translation);

    return model;
  }

  private void renderFloorAndCeiling(DrawMode mode)
  {
    int floorHeightOffset = 0;
    int ceilingHeightOffset = 0;
    if (player != null)
    {
      floorHeightOffset = player.getHeight();
      ceilingHeightOffset = -floorHeightOffset;
    }

    for (Map.Entry<Tile, Model> entry : TILE_MODEL_MAP.entrySet())
    {
      Tile tile = entry.getKey();
      Model model = entry.getValue();
      if (model.shape == null) continue;
      setTranslationValuesForModel(model, tile.getLocation().getX(),
                                   tile.isPartOfFloor() ? floorHeightOffset : ceilingHeightOffset,
                                   tile.getLocation().getY());
      model.shape.getTransforms().add(model.translation);
      model.shape.setDrawMode(mode);
    }
  }

  private void renderActors(DrawMode mode)
  {
    Translate translate = new Translate(0.0, 0.0, 0.0);
    for (Map.Entry<Actor, Model> entry : ACTOR_MODEL_MAP.entrySet())
    {
      Actor actor = entry.getKey();
      Model model = entry.getValue();
      if (model.shape == null) continue;
      //translate.setX(actor.getLocation().getX() - model.translation.getX());
      //translate.setY(model.translation.getY());
      //translate.setZ(actor.getLocation().getY() - model.translation.getZ());
      //model.shape.getTransforms().clear();
      //model.shape.getTransforms().addAll(translate);
      setTranslationValuesForModel(model, actor.getLocation().getX(), model.translation.getY(), actor.getLocation().getY());
      model.shape.setDrawMode(mode);
    }
  }

  private void orientPlayerToScene()
  {
    if (player == null) return;
    // calculate the amount of displacement between the current location the camera thinks
    // it's at versus the actual location of the player
    //camera.getTransforms().clear();
    //Translate translate = new Translate(player.getLocation().getX() - cameraTranslation.getX(),
                                        //0.0, player.getLocation().getY() - cameraTranslation.getZ());
    //camera.getTransforms().addAll(translate);
    cameraTranslation.setX(player.getLocation().getX());
    cameraTranslation.setZ(player.getLocation().getY());
  }

  private void setTranslationValuesForModel(Model model, double x, double y, double z)
  {
    model.translation.setX(x);
    model.translation.setY(y);
    model.translation.setZ(z);
  }
}
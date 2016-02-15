package cs351.project1;

import cs351.core.*;
import cs351.entities.Player;
import javafx.scene.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ZombieHouseRenderer implements Renderer
{
  private Scene scene;
  private PerspectiveCamera camera;
  private Translate cameraTranslation;
  private Rotate cameraRotation;
  private Player player;
  private PointLight playerLight;
  private Group renderSceneGraph; // camera and all actors are added to this
  private SubScene renderScene; // renderSceneGraph added to this for redraw each frame
  private final HashMap<Actor, Model> ACTOR_MODEL_MAP = new HashMap<>(50);
  private final HashMap<Actor, Model> TILE_MODEL_MAP = new HashMap<>(50);
  private final HashMap<String, Texture> TEXTURE_LOOKUP = new HashMap<>(50);
  private Group renderSceneGroup;

  private class Model
  {
    public Mesh mesh; // if a custom model was loaded
    public Shape3D shape; // if one of the default shapes was used
    public PhongMaterial material;
    public Texture diffuseTexture;
    public Texture specularTexture;
    public ArrayList<PointLight> lights;
    public Translate translation; // this is used to position the model within the scene
    public Rotate rotation; // this is used to rotate the model about the origin within the scene
  }

  private class Texture
  {
    private Image texture;

    public Texture(Image texture)
    {
      this.texture = texture;
    }

    public Image getTexture()
    {
      return texture;
    }
  }

  /**
   * This constructor will initialize itself, create a new scene with its renderSceneGroup
   * and then set the stage's scene to it.
   *
   * @param stage stage object that will have its scene set
   * @param width width of the scene's window
   * @param height height of the scene's window
   */
  public ZombieHouseRenderer(Stage stage, int width, int height)
  {
    this(width, height);
    scene = new Scene(renderSceneGroup);
    stage.setScene(scene);

    initLighting();
  }

  public ZombieHouseRenderer(Stage stage, StackPane pane, int width, int height)
  {
    this(width, height);
    scene = stage.getScene();
    //pane.setContent(renderSceneGroup);
    pane.getChildren().add(renderSceneGroup);

    initLighting();
  }

  private ZombieHouseRenderer(int width, int height)
  {
    renderSceneGraph = new Group();
    renderScene = new SubScene(renderSceneGraph, width, height, true, SceneAntialiasing.BALANCED);
    renderScene.setFill(Color.GRAY);
    renderSceneGroup = new Group();
    renderSceneGroup.getChildren().add(renderScene);
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
    playerLight = new PointLight(Color.color(0.2, 0.2, 0.2));
    playerLight.setLightOn(true);
    renderSceneGraph.getChildren().add(playerLight);
    cameraTranslation = new Translate(this.player.getLocation().getX(), 0.0, this.player.getLocation().getY());
    cameraRotation = new Rotate(0.0, 0.0, 0.0);
    playerLight.getTransforms().add(cameraTranslation);
    camera.getTransforms().addAll(cameraTranslation, cameraRotation);
    scene.setOnKeyPressed(this.player::keyPressed);
    scene.setOnKeyReleased(this.player::keyReleased);
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

  @Override
  public void associateDiffuseTextureWithActor(Actor actor, String textureFile)
  {
    if (!ACTOR_MODEL_MAP.containsKey(actor) && !TILE_MODEL_MAP.containsKey(actor))
    {
      throw new RuntimeException("Actor not registered with renderer before call to associateTextureWithActor");
    }
    Model model = ACTOR_MODEL_MAP.containsKey(actor) ? ACTOR_MODEL_MAP.get(actor) : TILE_MODEL_MAP.get(actor);
    // register the texture with the renderer and then set the diffuse map of the model's material
    // to the newly-registered texture
    model.diffuseTexture = registerTexture(textureFile);
    model.material.setDiffuseMap(model.diffuseTexture.getTexture());
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

    for (Map.Entry<Actor, Model> entry : TILE_MODEL_MAP.entrySet())
    {
      Tile tile = (Tile)entry.getKey();
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

  private void initLighting()
  {
    AmbientLight ambient = new AmbientLight(Color.color(0.3, 0.3, 0.3));
    ambient.setLightOn(true);
    renderSceneGraph.getChildren().add(ambient);
  }

  private Texture registerTexture(String textureFile)
  {
    // if the texture already exists then don't bother reloading it
    if (TEXTURE_LOOKUP.containsKey(textureFile)) return TEXTURE_LOOKUP.get(textureFile);
    InputStream stream = ZombieHouseRenderer.class.getResourceAsStream(textureFile);
    if (stream == null) throw new RuntimeException("Unable to load " + textureFile + " as a resource");
    Texture texture = new Texture(new Image(stream));
    TEXTURE_LOOKUP.put(textureFile, texture);
    return texture;
  }
}
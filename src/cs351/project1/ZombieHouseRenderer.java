package cs351.project1;

import cs351.core.*;
import cs351.entities.Player;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ZombieHouseRenderer implements Renderer
{
  private Scene scene;
  private PerspectiveCamera camera;
  private Player player;
  private PlayerController controller;
  private PointLight playerLight;
  private Group renderSceneGraph; // camera and all actors are added to this
  private SubScene renderScene; // renderSceneGraph added to this for redraw each frame
  private final HashMap<Actor, Model> ACTOR_MODEL_MAP = new HashMap<>(50);
  private final HashMap<String, Texture> TEXTURE_LOOKUP = new HashMap<>(50);
  private double largestWall = 0.0; // used to calculate where the ceiling should be
  private Group renderSceneGroup;

  private class Model
  {
    public RenderEntity entity; // if a custom model was loaded
    private MeshView currMeshView;
    public HashMap<TriangleMesh, MeshView> meshViewMap;
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
    private PhongMaterial material;

    public Texture(Image texture)
    {
      this.texture = texture;
      material = new PhongMaterial(Color.BEIGE);
      material.setDiffuseColor(Color.BEIGE);
      material.setSpecularColor(Color.WHITE);
    }

    public Image getTexture()
    {
      return texture;
    }

    public PhongMaterial getMaterial()
    {
      return material;
    }
  }

  private class PlayerController
  {
    private Player player;
    private Translate translation;
    private Rotate rotation;
    private Point2D direction;
    private Point2D right;
    private double prevX = 0.0;
    private double angle = 0.0;
    private final double SPEED = 1.0; // for x and y movement

    public PlayerController(Player player)
    {
      this.player = player;
      translation = new Translate(0.0, 0.0, 0.0);
      rotation = new Rotate(0.0, 0.0, 0.0);
      rotation.setAxis(new Point3D(0.0, 1.0, 0.0)); // sets it to rotate about the y-axis
      rotation.setAngle(angle);
      buildDirectionVectors();
      scene.setOnMouseMoved(this::mouseMoved);
      scene.setOnKeyPressed(this::keyPressed);
      scene.setOnKeyReleased(this::keyReleased);
    }

    public Translate getTranslation()
    {
      return translation;
    }

    public Rotate getRotation()
    {
      return rotation;
    }

    public void update()
    {
      rotation.setAngle(angle);
      buildDirectionVectors();
    }

    private void mouseMoved(MouseEvent event)
    {
      // This if statement is here since prevX is initially going to be 0.0, so it can cause
      // an initial spasm with the camera since event.getX() - prevX might result in a very large
      // angle
      if (prevX > 0.0)
      {
        angle += event.getX() - prevX;
        angle = angle % 360.0;
      }
      prevX = event.getX();
    }

    public void keyPressed(KeyEvent event)
    {
      //if (event.getText().equals("w")) player.setLocation(player.getLocation().getX() + direction.getY(), player.getLocation().getY() + direction.getX());
      //else if (event.getText().equals("s")) player.setLocation(player.getLocation().getX() + -direction.getY(), player.getLocation().getY() + -direction.getX());
      if (event.getText().equals("w"))
      {
        player.setForwardSpeedX(SPEED); //speedY = SPEED;
        player.setForwardSpeedY(SPEED); //speedY = SPEED;
      }
      else if (event.getText().equals("s"))
      {
        player.setForwardSpeedX(-SPEED); //speedY = SPEED;
        player.setForwardSpeedY(-SPEED); //speedY = -SPEED;
      }
      else if (event.getText().equals("a"))
      {
        player.setRightSpeedX(-SPEED);
        player.setRightSpeedY(-SPEED);
      }
      else if (event.getText().equals("d"))
      {
        player.setRightSpeedX(SPEED);
        player.setRightSpeedY(SPEED);
      }
    }

    public void keyReleased(KeyEvent event)
    {
      if (event.getText().equals("w") || event.getText().equals("s"))
      {
        player.setForwardSpeedX(0.0);
        player.setForwardSpeedY(0.0);
      }
      else if (event.getText().equals("a") || event.getText().equals("d"))
      {
        player.setRightSpeedX(0.0);
        player.setRightSpeedY(0.0);
      }
    }

    private double degreesToRadians(double angle)
    {
      return angle * (3.14159265359 / 180.0);
    }

    private void buildDirectionVectors()
    {
      // for information on this, see:
      // http://stackoverflow.com/questions/1568568/how-to-convert-euler-angles-to-directional-vector
      //
      // In our case the angle variable represents yaw (rotation about the y-axis), but pitch and roll remain at a constant 0.0. Because
      // of this, the z-component of the direction vector is always 0, so it is a 2D direction vector in the end (starts off pointing
      // down x-axis at 0 degrees, y-axis at 90 degrees, etc.).
      // Notes:
      //        1) When the angle variable is 0, the direction vector points straight down the x-axis (on a 2-dimensional grid)
      //        2) When the angle variable is 90, the direction vector points straight up the y-axis
      //        3) For our game, an actor's y-component represents forward in 3D space and its x-component represents side to side

      // CAUTION: The values are reversed: direction's x-component should be Math.cos(degreesToRadians(angle)), but it is
      // inverted since the x-component needs to point straight ahead, but straight ahead (for us) is the y-axis
      direction = new Point2D(Math.sin(degreesToRadians(angle)), Math.cos(degreesToRadians(angle)));
      direction.normalize(); // with direction vectors you only need their magnitude to be 1.0 since their job is just to point
      // For more information on this, see:
      // http://answers.unity3d.com/questions/228203/getting-vector-which-is-pointing-to-the-rightleft.html
      // I dropped the negative since in JavaFX the -y points up instead of down, so when x = 1.0, the right of
      // that should be y = -1.0, but in JavaFX it needs to be y = 1.0

      // CAUTION: The negative sign should be in front of direction.getY(), but since direction's components
      // are reversed I had to swap it around for the math to work inside of the Player class (otherwise right
      // became left and left became right)
      right = new Point2D(direction.getY(), -direction.getX()); // figure out the direction of right
      // this is flipped (getY first instead of getX) because we start off with a direction vector that points
      // down the x-axis, and we want that initial pointing to represent forward.
      player.setForwardDirection(direction);
      player.setRightDirection(right);
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
    renderScene.setFill(Color.BLACK);
    renderSceneGroup = new Group();
    renderSceneGroup.getChildren().add(renderScene);
  }

  @Override
  public void render(Engine engine, DrawMode mode, double deltaSeconds)
  {
    // orient the player to their rotation/location
    orientPlayerToScene();

    // render the actors
    renderActors(engine, mode, deltaSeconds);
  }

  @Override
  public void reset()
  {
    camera = null;
    player = null;
    controller = null;
    largestWall = 0.0;
    ACTOR_MODEL_MAP.clear();
    // doesn't clear textures in case they're needed again for the next frame
    renderSceneGraph.getChildren().clear();
    // clearing the scene graph kills the lighting
    initLighting();
  }

  @Override
  public void registerPlayer(Actor player, double fieldOfView)
  {
    this.player = (Player)player;
    camera = new PerspectiveCamera(true);
    //camera.setFarClip(1000);
    controller = new PlayerController(this.player);
    camera.setFieldOfView(fieldOfView);
    renderSceneGraph.getChildren().add(camera);
    renderScene.setCamera(camera);
    playerLight = new PointLight(Color.color(0.2, 0.2, 0.2));
    playerLight.setLightOn(true);
    renderSceneGraph.getChildren().add(playerLight);
    controller.getTranslation().setX(this.player.getLocation().getX());
    controller.getTranslation().setY(-this.player.getHeight() / 2.0);
    controller.getTranslation().setZ(this.player.getLocation().getY());
    playerLight.getTransforms().addAll(controller.getTranslation(), controller.getRotation());
    camera.getTransforms().addAll(controller.getTranslation(), controller.getRotation());
    //scene.setOnKeyPressed(this.player::keyPressed);
    //scene.setOnKeyReleased(this.player::keyReleased);
  }

  @Override
  public void registerActor(Actor actor, Shape3D shape, Color diffuseColor, Color specularColor, Color ambientColor)
  {
    Model model = generateModel(actor, shape, diffuseColor, specularColor, ambientColor);
    // renderer uses largestWall to calculate where it should put the ceiling
    if (actor.isStatic() && -actor.getHeight() < largestWall) largestWall = -actor.getHeight();
    ACTOR_MODEL_MAP.put(actor, model);
    renderSceneGraph.getChildren().addAll(model.shape);
  }

  @Override
  public void registerActor(Actor actor, RenderEntity entity, Color diffuseColor, Color specularColor, Color ambientColor)
  {
    Model model = generateModel(actor, null, diffuseColor, specularColor, ambientColor);
    if (actor.isStatic() && -actor.getHeight() < largestWall) largestWall = -actor.getHeight();
    ACTOR_MODEL_MAP.put(actor, model);
    model.entity = entity;
    // A RenterEntity stores its meshes in the form of a list where each element in the list
    // is a different mesh corresponding to a different frame in the animation. This means that
    // the renderer needs to build a map of meshes to meshViews and create each meshView.
    TriangleMesh[] meshList = model.entity.getMeshList();
    model.meshViewMap = new HashMap<>(meshList.length);
    for (TriangleMesh mesh : meshList)
    {
      MeshView meshView = new MeshView(mesh);
      model.meshViewMap.put(mesh, meshView);
      meshView.setMaterial(model.material);
      meshView.getTransforms().addAll(model.rotation, model.translation);
      meshView.setCullFace(CullFace.NONE);
      meshView.setVisible(false);
      renderSceneGraph.getChildren().addAll(meshView);
    }
    model.currMeshView = model.meshViewMap.get(meshList[0]);
    model.currMeshView.setVisible(true);
  }

  @Override
  public void mapTextureToActor(String textureFile, Actor actor)
  {
    if (!ACTOR_MODEL_MAP.containsKey(actor))
    {
      throw new RuntimeException("Actor not registered with renderer before call to associateTextureWithActor");
    }
    Model model = ACTOR_MODEL_MAP.get(actor);
    // register the texture with the renderer and then set the diffuse map of the model's material
    // to the newly-registered texture
    model.diffuseTexture = registerTexture(textureFile);
    // update the model's material to use the material created during
    // the texture loading/creation process
    model.material = model.diffuseTexture.getMaterial();
    if (model.shape != null) model.shape.setMaterial(model.material);
    else
    {
      TriangleMesh[] meshList = model.entity.getMeshList();
      for (TriangleMesh mesh : meshList) model.meshViewMap.get(mesh).setMaterial(model.material);
    }
    model.material.setDiffuseMap(model.diffuseTexture.getTexture());
  }

  private Model generateModel(Actor actor, Shape3D shape, Color ambientColor, Color diffuseColor, Color specularColor)
  {
    Model model = new Model();
    model.shape = shape;
    PhongMaterial material = new PhongMaterial(ambientColor);
    material.setDiffuseColor(diffuseColor);
    material.setSpecularColor(specularColor);
    model.material = material;
    if (shape != null) shape.setMaterial(material);
    model.lights = new ArrayList<>(5);
    model.translation = new Translate(actor.getLocation().getX(),
                                      0.0,
                                      actor.getLocation().getY());
    model.rotation = new Rotate(0.0, 0.0, 0.0);
    if (shape != null) shape.getTransforms().addAll(model.rotation, model.translation);

    return model;
  }

  private void renderActors(Engine engine, DrawMode mode, double deltaSeconds)
  {
    //double floorDepthOffset = engine.getWorld().getTilePixelHeight();
    double floorDepthOffset = 0.5;
    double floorHeightOffset = largestWall;
    for (Map.Entry<Actor, Model> entry : ACTOR_MODEL_MAP.entrySet())
    {
      Actor actor = entry.getKey();
      Model model = entry.getValue();
      //translate.setX(actor.getLocation().getX() - model.translation.getX());
      //translate.setY(model.translation.getY());
      //translate.setZ(actor.getLocation().getY() - model.translation.getZ());
      //model.shape.getTransforms().clear();
      //model.shape.getTransforms().addAll(translate);
      double actorHeightOffset = model.shape != null ? -actor.getHeight() / 2.0 : 0.0;
      // TODO come up with a better way to make the player see eye-to-eye with things the same height as them
      if (actor.isPartOfFloor()) setTranslationValuesForModel(model, actor.getLocation().getX(),
                                                              floorDepthOffset,
                                                              actor.getLocation().getY());
      else if (actor.isPartOfCeiling()) setTranslationValuesForModel(model, actor.getLocation().getX(),
                                                                     floorHeightOffset - actor.getHeight() / 2.0,
                                                                     actor.getLocation().getY());
      else setTranslationValuesForModel(model, actor.getLocation().getX(),
                                        actorHeightOffset,
                                        actor.getLocation().getY());
      if (model.shape != null) model.shape.setDrawMode(mode);
      else
      {
        model.entity.animate(deltaSeconds);
        model.currMeshView.setVisible(false); // this is now the old mesh view - disable its visibility
        model.currMeshView = model.meshViewMap.get(model.entity.getMesh());
        model.currMeshView.setVisible(true); // only the current one
        model.currMeshView.setDrawMode(mode);
        model.currMeshView.setCullFace(CullFace.BACK);
      }
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
    controller.getTranslation().setX(player.getLocation().getX());
    controller.getTranslation().setZ(player.getLocation().getY());
    controller.update();
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
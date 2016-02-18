package cs351.project1;


import cs351.core.*;
import javafx.scene.shape.DrawMode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Collection;
import java.util.HashSet;

public class ZombieHouseEngine implements Engine
{
  private World world;
  private SoundEngine soundEngine;
  private Renderer renderer;
  private int worldWidth, worldHeight; // measured in tiles
  private final HashSet<Actor> ALL_ACTORS;
  private final HashSet<Actor> UPDATE_ACTORS; // only the actors that want to be updated each frame
  private boolean isInitialized = false;
  private boolean isPaused = false;
  private boolean isPendingShutdown = false;
  // pendingLevelRestart and pendingNextLevel let the engine know if it needs to do something
  // after the current frame is finished
  private boolean pendingLevelRestart;
  private boolean pendingNextLevel;
  private long millisecondsSinceLastFrame;
  private long millisecondTimeStamp;

  public ZombieHouseEngine()
  {
    ALL_ACTORS = new HashSet<Actor>(500);
    UPDATE_ACTORS = new HashSet<Actor>(500);
  }

  @Override
  public World getWorld()
  {
    validateEngineState();
    return world;
  }

  @Override
  public SoundEngine getSoundEngine()
  {
    validateEngineState();
    return soundEngine;
  }

  @Override
  public Renderer getRenderer()
  {
    validateEngineState();
    return renderer;
  }

  @Override
  public int getWorldWidth()
  {
    validateEngineState();
    return worldWidth;
  }

  @Override
  public int getWorldHeight()
  {
    validateEngineState();
    return worldHeight;
  }

  @Override
  public boolean isEnginePendingShutdown()
  {
    return isPendingShutdown;
  }

  @Override
  public Collection<Actor> getNeighboringActors(Actor actor, int tileDistance)
  {
    // TODO make this work
    return null;
  }

  @Override
  public void init(Stage stage, World world, SoundEngine soundEngine, Renderer renderer)
  {
    if (isInitialized) throw new RuntimeException("Engine was not shutdown before a new call to init");
    System.out.println("-> Initializing engine");
    this.world = world;
    this.soundEngine = soundEngine;
    this.renderer = renderer;

    isInitialized = true;
    isPendingShutdown = false;
    pendingLevelRestart = false;
    pendingNextLevel = true;
    millisecondTimeStamp = System.currentTimeMillis();
    millisecondsSinceLastFrame = 0;
    togglePause(false);
    stage.setOnCloseRequest(this::windowClosed);
    initEngineFromWorld(true); // init the initial engine state from the world
  }

  @Override
  public void shutdown()
  {
    if (!isInitialized) throw new RuntimeException("Engine was not initialized before the call to shutdown");
    System.out.println("-> Shutting down engine");
    isInitialized = false;
    ALL_ACTORS.clear();
    UPDATE_ACTORS.clear();
  }

  @Override
  public void togglePause(boolean value)
  {
    isPaused = value;
  }

  @Override
  public void frame()
  {
    //System.out.println("called");
    if (!isInitialized || isPaused || isPendingShutdown) return;
    // TODO add collision detection/collision event pushing
    millisecondsSinceLastFrame = System.currentTimeMillis() - millisecondTimeStamp;
    millisecondTimeStamp = System.currentTimeMillis(); // mark the time when this frame started
    double deltaSeconds = millisecondsSinceLastFrame / 1000.0; // used for the actors
    // update all actors and process their return statements
    for (Actor actor : UPDATE_ACTORS) processActorReturnStatement(actor.update(this, deltaSeconds));
    getSoundEngine().update();
    getRenderer().render(this, DrawMode.FILL);
    // set the center point for the sound engine
    getSoundEngine().setCentralPoint((int)getWorld().getPlayer().getLocation().getX(),
                                     (int)getWorld().getPlayer().getLocation().getY());
    // if during the frame an actor(s) were added to the world, pull them now
    pullLatestActorsFromWorld();
    // with the frame complete, call initEngineState to see if anything needs to change
    initEngineState();
  }

  private void windowClosed(WindowEvent event)
  {
    if (isInitialized) shutdown();
  }

  private void validateEngineState()
  {
    if (!isInitialized || isPaused) throw new IllegalStateException("Engine was not initialized/un-paused before use");
  }

  private void processActorReturnStatement(Actor.UpdateResult result)
  {
    if (pendingLevelRestart || pendingNextLevel) return; // if it is already set to do something at the end of the frame, do nothing
    if (result == Actor.UpdateResult.PLAYER_DEFEAT) pendingLevelRestart = true;
    else if (result == Actor.UpdateResult.PLAYER_VICTORY) pendingNextLevel = true;
  }

  private void initEngineState()
  {
    if (pendingNextLevel && getWorld().hasNextLevel())
    {
//      getWorld().nextLevel(this);
      initEngineFromWorld(true);
      pendingNextLevel = false;
    }
    else if (pendingLevelRestart)
    {
      initEngineFromWorld(false);
      pendingLevelRestart = false;
    }
    else if (pendingNextLevel && !getWorld().hasNextLevel()) isPendingShutdown = true;
  }

  private void initEngineFromWorld(boolean shouldGetNextLevel)
  {
    final double DEFAULT_FIELD_OF_VIEW = 90.0; // measured in degrees
    ALL_ACTORS.clear();
    UPDATE_ACTORS.clear();
    getRenderer().reset();
    // queue up the next level/restart the current level
    if (shouldGetNextLevel) getWorld().nextLevel(this);
    else getWorld().restartLevel(this);

    pullLatestActorsFromWorld();
    // the +1 accounts for it truncating values after the decimal when the
    // division isn't clean
    worldWidth = world.getWorldPixelWidth() / world.getTilePixelWidth() + 1;
    worldHeight = world.getWorldPixelHeight() / world.getTilePixelHeight() + 1;
    renderer.registerPlayer(getWorld().getPlayer(), DEFAULT_FIELD_OF_VIEW);
  }

  private void pullLatestActorsFromWorld()
  {
    if (getWorld().getChangeList(false).size() == 0) return;
    ALL_ACTORS.addAll(getWorld().getChangeList(true)); // this should contain all actors since nextLevel was called
    for (Actor actor : ALL_ACTORS)
    {
      if (actor.shouldUpdate()) UPDATE_ACTORS.add(actor);
    }
  }
}

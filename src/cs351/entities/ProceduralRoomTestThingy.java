package cs351.entities;

 

import javafx.animation.AnimationTimer;

/*
 =======================================================================
 This class creates a procedurally generated level map. The following
 algorithm explains the process:
 
 1.) Fill entire map with a rectangular Wall, initialize a queue with 
     this one large area.
 2.) While the queue still has "Areas" waiting in line, take the front
     area, (or head) and divide it
     a.) If (we don't have 6 hallways created yet) AND 
            (The area is large enough)
           Then carve out a straight line (hallway), rotating 90 degrees
                each time. Add both blocks (either side first) back to
                the queue.
     b.) Else-If the area is still too big
           Then cut the area in half (pick a random spot) and add both
                sides back into the queue.
         Else carve out the final room and stop because you have enough
              rooms.
              
              
 Credit for this algorithm: http://www.polygonpi.com/?p=1191
 =====================================================================
 */

import javafx.application.Application;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import java.util.LinkedList;
import javafx.scene.Group;
import java.util.HashMap;
import java.util.Random;

import cs351.core.Actor;
import cs351.project1.ZombieHouseEngine;

import java.util.Queue;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ProceduralRoomTestThingy extends Application
{
  private Group root = new Group();
  private Scene scene = new Scene(root, 500, 500, Color.BLACK);
  private Queue<ProceduralRoomTestThingy> divideRoomsQueue = new LinkedList<>();
  
  private Queue<ProceduralRoomTestThingy> unReachableRooms = new LinkedList<>();
  private int numberOfExistingHallways = 0;
  static final boolean VERTICAL   = true;
  static final boolean HORIZONTAL = false;
  private boolean ROTATION        = true;
  private int BOARD_WIDTH         = 100;
  private int BOARD_HEIGHT        = 100;
  private int splitRotation       = 1;
  private int randomNumber2;
  private int randomNumber;
  private int totalHeight;
  private int totalWidth;
  private int xStartPt;
  private int yStartPt;
  private int numRooms;
  private int randInt;
  private int height;
  private int width;
  
  Stage stage;
  
  
  private int n = 1;
  
  HashMap<Integer, Integer> areaOneCenter = new HashMap<>();
  private int[][] boardArray = new int [BOARD_WIDTH][BOARD_HEIGHT];
  

  public ProceduralRoomTestThingy(){}
  
  public ProceduralRoomTestThingy (int x, int y, int width, int height)
  {
    this.height    = height;
    this.width     = width;
    this.xStartPt  = x;
    this.yStartPt  = y;
  }
  

  /*
   ===================================================
   This is where the action begins. A board perimeter
   is created, Blocks or "chunks" where the rooms 
   will exist are formed. The halls are connected, 
   and finally the rooms get connected.
   ===================================================
   */
  public void initializeBoard()
  {
    Player p = new Player(BOARD_HEIGHT, BOARD_HEIGHT, BOARD_HEIGHT);
    xStartPt     = 1;
    yStartPt     = 1;
    totalWidth   = 99;  
    totalHeight  = 99;
    
    
    System.out.println("initialize Array");
    for (int x = 0; x < 100; x++)
    {
      for (int y = 0; y < 100; y++)
      {
        boardArray[x][y] = 0;
      }
    }
    
    
    
    divideRoomsQueue.add( new ProceduralRoomTestThingy( xStartPt, yStartPt, totalWidth, totalHeight) );
    
    ProceduralRoomTestThingy firstInQueue = divideRoomsQueue.remove();
    
    //initial division of a single room
    divideAreaOne(firstInQueue);
    
    //change split rotation
    splitRotation++;
    
    carveBlockArea();

    Stage stage = new Stage();
    ZombieHouseEngine engine = new ZombieHouseEngine();
    
    new AnimationTimer(){

      @Override
      public void handle(long arg0)
      {
     //  System.out.println("player location x"+ p.getLocation().getX() + "y is "+ p.getLocation().getY());
       //System.out.println("> " + engine.getWorld().getPlayer().getLocation().getX());
      
      
      }
    }.start();


    Rectangle r7 = new Rectangle(p.getLocation().getX(), 100, 3, 3);
    r7.setFill(Color.WHITE);
    root.getChildren().add(r7);
    
    stage.setTitle("Level Map");
    stage.setScene(scene);
    stage.show();
  }
  
  /*
   ======================================================================
   This will carve out large sections, or Blocks of the board, that will 
   eventually become rooms. This is similar to binary space partitioning.
   
   For more information read:
   http://www.roguebasin.com/index.php?title=Basic_BSP_Dungeon_generation
   ======================================================================
   */
  void carveBlockArea()
  {
    
    while(!divideRoomsQueue.isEmpty())
    {
      
      //remove the first two from the list
      ProceduralRoomTestThingy firstInQueue = divideRoomsQueue.remove();
      ProceduralRoomTestThingy secondInQueue = divideRoomsQueue.remove();

      //connectRooms();
      
      divideAreas(firstInQueue, secondInQueue);
      
      //TODO change value below to determine how many rooms get created
      if( divideRoomsQueue.size()  > 14 )
      {
        break;
      }
    }
    
    connectRooms();
    
    printArray();
    //ALSO connectRooms is being called in add to queue
  }
  
  
  /*
  =================================================
  So far the rooms are created but not accessible.
  We will need to 
  =================================================
  */
  void connectRooms()
  {
    //TODO figure out how to check the size of the rooms
    //TODO
    while( !unReachableRooms.isEmpty() )
    {
     // System.out.println(" queue size " + unReachableRooms.size());
      ProceduralRoomTestThingy remainingRooms = unReachableRooms.remove();
      
      for ( int x = remainingRooms.xStartPt; x <= remainingRooms.width; x++)
      {
        for ( int y = remainingRooms.yStartPt; y <= remainingRooms.height; y++)
        {
        //door on right side of area
          if(   x == remainingRooms.width 
             && y == remainingRooms.height / 2
             && x != totalWidth)
          {
            boardArray[x][y] = 0;
            boardArray[x][y + 1] = 0;
            
          }
          
          // door on left side of area, could be an exit
          if(   x == remainingRooms.xStartPt
              && y == remainingRooms.height / 2
              && x != xStartPt
               )
           {
             boardArray[x][y] = 0;
             boardArray[x][y + 1] = 0;
  
           }
          
          //door on top of area
          if(   x == remainingRooms.width / 2 
              && y == remainingRooms.yStartPt 
              && y != yStartPt
              )
           {
             boardArray[x][y] = 0;
             boardArray[x + 1][y] = 0;

           }
          
          //door on bottom of area
          if(   x == remainingRooms.width / 2 
              && y == remainingRooms.height 
              && y != totalHeight
              )
           {
             boardArray[x][y] = 0;
             boardArray[x + 1][y] = 0;
           }
          
        }
      }
    }
  }
  
  /************************ START HELPER METHODS ***************************/
  /*
   ===========================================
   Alternate between vertical and horizontal
   division of rooms
   ===========================================
   */
  public boolean splitDirection_IsVertical()
  {
    if(splitRotation % 2 == 0) return false;
    return true;
  }
  
  /*
   =================================
   Vertical is true, Horizontal is 
   false
   ================================
   */
  public boolean changeSplitDir()
  {
    if (ROTATION == VERTICAL)
    {
      ROTATION = HORIZONTAL; 
      return HORIZONTAL;
    }
    else 
    {
      ROTATION = VERTICAL;
      return VERTICAL;
    }
  }
  
  /*
   =================================================
   If more hallways are needed, carve more hallways.
   Otherwise split the remaining rooms
   =================================================
   */
  public void divideAreaOne( ProceduralRoomTestThingy firstInQueue )
  {
      randomNumber = getRandomNumber(firstInQueue, VERTICAL);
      verticalDivide(firstInQueue, randomNumber);
  }
  
  public void divideAreas( ProceduralRoomTestThingy firstInQueue, ProceduralRoomTestThingy secondInQueue )
  {

     //TODO CHANGED from splitDirection_IsVertical()
     if (ROTATION && roomIsLargeEnough(firstInQueue, VERTICAL) && roomIsLargeEnough(secondInQueue, VERTICAL))
    {
      randomNumber  = getRandomNumber(firstInQueue, VERTICAL);
      randomNumber2 = getRandomNumber(secondInQueue, VERTICAL);
      verticalDivide( firstInQueue, randomNumber );
      verticalDivide( secondInQueue, randomNumber2 );
    }
    else if ( roomIsLargeEnough(firstInQueue, HORIZONTAL)&& roomIsLargeEnough(secondInQueue, HORIZONTAL))
    {
      randomNumber = getRandomNumber(firstInQueue, HORIZONTAL);
      randomNumber2 = getRandomNumber(secondInQueue, HORIZONTAL);
      horizontalDivide( firstInQueue, randomNumber );
      horizontalDivide( secondInQueue, randomNumber2 );
    }
   //  numberOfExistingHallways++;
  }
  
  
  
  
  
  public void verticalDivide(ProceduralRoomTestThingy firstInQueue, int randomNumber)
  {
    System.err.println(numberOfExistingHallways);
    for (int x = firstInQueue.xStartPt; x < firstInQueue.width; x++)
    {
      for (int y = firstInQueue.yStartPt; y < firstInQueue.height; y++)
      {
   
        if( numberOfExistingHallways < 7)
        {
          /* draw area of square */
          if ((x == firstInQueue.xStartPt) || (y == firstInQueue.yStartPt) || (x == firstInQueue.width - 1)
              || (y == firstInQueue.height - 1))
          {
            Rectangle r = new Rectangle(x, y, 1, 1);
            r.setFill(Color.BLUE);
            root.getChildren().add(r);

            boardArray[x][y] = 1;
          }
        }

        if( x == randomNumber 
            && ( y < firstInQueue.height - 1 ) )
        {
          if( numberOfExistingHallways < 6 )
          {
          }
          else 
          {
            //TURN ON PIXEL
            boardArray[x][y] = 1;
            
            Rectangle r3 = new Rectangle(x , y , 1, 1);
            r3.setFill(Color.GREEN);
            root.getChildren().add(r3);
          }
        }
        //add doorway here
        
      }
    } /* END TOP LEVEL FOR-LOOP */
    
    
    
    /* split Chunks and put them back into queue */
    addToQueue(firstInQueue, randomNumber, VERTICAL);
    
  }
  
  
  
  public void horizontalDivide( ProceduralRoomTestThingy firstInQueue, int randomNumber )
  {
    System.err.println(numberOfExistingHallways);
    for (int x = firstInQueue.xStartPt; x < firstInQueue.width; x++)
    {
      for (int y = firstInQueue.yStartPt; y < firstInQueue.height; y++)
      {

        if( numberOfExistingHallways < 7)
        {
          if ((x == firstInQueue.xStartPt) || (y == firstInQueue.yStartPt) || (x == firstInQueue.width - 1)
              || (y == firstInQueue.height - 1))
          {
            Rectangle r4 = new Rectangle(x, y, 1, 1);
            r4.setFill(Color.BLUE);
            root.getChildren().add(r4);
            
            boardArray[x][y] = 1;
          }
        }
        
        if(y == randomNumber )
        {
          if(numberOfExistingHallways < 6)
          {
          }
          else
          {
            //Turn on pixel
            boardArray[x][y] = 1;
            Rectangle r6 = new Rectangle(x + 1 , y , 1, 1);
            r6.setFill(Color.GREEN);
            root.getChildren().add(r6);
          }
        }

      }
    } /* END TOP LEVEL FOR-LOOP */
    
    /* splits chunks into two areas and puts them back into queue */
    addToQueue(firstInQueue, randomNumber, HORIZONTAL);
    
  }
  
  void addToQueue(ProceduralRoomTestThingy firstInQueue, int randomNumber, boolean splitIsVertical)
  {
    int xStartPt = firstInQueue.xStartPt;
    int yStartPt = firstInQueue.yStartPt;
    int width    = firstInQueue.width;
    int height   = firstInQueue.height;
   
    
    //HALLWAY VERTICAL
    if( ( numberOfExistingHallways < 6 ) && splitIsVertical )
    {
      divideRoomsQueue.add( new ProceduralRoomTestThingy( xStartPt, yStartPt, randomNumber - 1, height ) );
      divideRoomsQueue.add( new ProceduralRoomTestThingy( randomNumber + 3, yStartPt, width, height ) );
      

      unReachableRooms.add( new ProceduralRoomTestThingy( xStartPt, yStartPt, randomNumber - 1, height ) );
      unReachableRooms.add( new ProceduralRoomTestThingy( randomNumber + 3, yStartPt, width, height ) );
    
      numberOfExistingHallways++;
    }
    //NOT HALLWAY - VERTICAL
    else if ( numberOfExistingHallways >= 6 && ( splitIsVertical ) )
    {
      divideRoomsQueue.add( new ProceduralRoomTestThingy( xStartPt, yStartPt, randomNumber, height ) );
      divideRoomsQueue.add( new ProceduralRoomTestThingy( randomNumber , yStartPt, width, height ) );
      

      unReachableRooms.add( new ProceduralRoomTestThingy( xStartPt, yStartPt, randomNumber - 1, height ) );
      unReachableRooms.add( new ProceduralRoomTestThingy( randomNumber , yStartPt, width, height ) );
    }
    //HALLWAY HORIZONTAL
    else if ( ( numberOfExistingHallways < 6 ) && (!splitIsVertical) )
    {
      divideRoomsQueue.add( new ProceduralRoomTestThingy( xStartPt, yStartPt, width, randomNumber - 1 ) );
      divideRoomsQueue.add( new ProceduralRoomTestThingy( xStartPt,randomNumber + 3 , width, height ) );
      
      unReachableRooms.add( new ProceduralRoomTestThingy( xStartPt, yStartPt, width, randomNumber - 1 ) );
      unReachableRooms.add( new ProceduralRoomTestThingy( xStartPt,randomNumber + 3 , width, height ) );
      
      numberOfExistingHallways++;
    }
    //NOT HALLWAY - HORIZONTAL
    else if ( numberOfExistingHallways >= 6 && ( !splitIsVertical ) )
    {
      divideRoomsQueue.add( new ProceduralRoomTestThingy( xStartPt, yStartPt, width, randomNumber ) );
      divideRoomsQueue.add( new ProceduralRoomTestThingy( xStartPt,randomNumber -1  , width, height ) );
      
      unReachableRooms.add( new ProceduralRoomTestThingy( xStartPt, yStartPt, width, randomNumber ) );
      unReachableRooms.add( new ProceduralRoomTestThingy( xStartPt,randomNumber  , width, height ) );
    }
   // numRooms++;
    
    
    numRooms = divideRoomsQueue.size();
    if(numRooms == Math.pow(2, n)){
     // System.err.println("Queue size =" + unConnectingRoomsQueue.size());
      System.out.println("boolean " + changeSplitDir() );
      n++;
    }
   // connectRooms();
  }
  
  
  /*
   ===================================================
   Returns a random number to divide the x axis or the
   y axis. The upper and lower bound variables 
   restrict the line division between 1/3 and 2/3. the
   reason for this is so the hallways and borders 
   don't end up too close to another wall
   ===================================================
   */
  public int getRandomNumber(ProceduralRoomTestThingy firstInQueue, boolean splitIsVertical)
  {
    
    int upperBound_X = ( firstInQueue.width * 2 )  / 3;
    int upperBound_Y = ( firstInQueue.height * 2 ) / 3;
    
    int lowerBound_X = ( firstInQueue.width )      / 3;
    int lowerBound_Y = ( firstInQueue.height )     / 3;
    

    Random rand = new Random();
    
    if(splitIsVertical)
    {
      try
      {
       randInt = rand.nextInt(( upperBound_X - lowerBound_X ) ) + lowerBound_X;
      }
      catch(IllegalArgumentException e)
      {
        System.err.println("upperBound_X = "+upperBound_X+" lowerBound_X = "+lowerBound_X+" randInt = "+randInt);
      }
    }
    
    else
    {
      try
      {
        
        randInt = rand.nextInt( upperBound_Y - lowerBound_Y ) + lowerBound_Y;
      }
      catch( IllegalArgumentException e)
      {
        System.err.println("upperBound_Y = "+upperBound_Y+" lowerBound_X = "+lowerBound_X+" randInt = "+randInt);
      }
    }
    
    return randInt;
  }
  
  /*
   ===========================================================
   Checks to see that the minimum size room is not going to be 
   divided. A room that is too narrow, but still has a long 
   length can still be divided. In this case, we could still 
   divide it horizontally
   ===========================================================
   */
  public boolean roomIsLargeEnough( ProceduralRoomTestThingy firstInQueue, boolean checkWidth)
  {
    
    // check the width since we want to make a vertical line
    if( checkWidth )
    {
      if(    ( firstInQueue.width  -  firstInQueue.xStartPt ) < 6 
          && ( firstInQueue.height -  firstInQueue.yStartPt ) < 6 )
        return false;
    }
    //TODO fix this later on
    //TODO MAY NOT WORK 
    return true;
  }
  
  /*
  =====================================================
  This will find the center of any created room and
  return the coordinates of this center
  =====================================================
  */
 public Point getPoints()
 {
   Point center = new Point((int) (Math.floor(xStartPt + width) / 2), 
                                   Math.floor((yStartPt + height) / 2));
   return center;
 }
 
 
 public void printArray(){
   System.out.println("print Array");
   for (int x = 0; x < BOARD_WIDTH; x++)
   {
     for (int y = 0; y < BOARD_HEIGHT; y++)
     {
       System.out.print(boardArray[x][y]);
     }System.out.println("\n");
   }System.out.println("\n");
 }
 
 public int[][] getArray()
 {
   return(boardArray);
 }
  
  /*********************** END HELPER METHODS **************************/ 
  
  
  /*
  ================================================
  Inner class that instantiates coordinate points
  from the Rooms class
  ================================================  
  */
 public class Point {
     
     private double x;
     private double y;

     public Point(double x, double y)
     {
         this.x = x;
         this.y = y;
     }
    
     public double getX(){
       return this.x;
     }
     
     public double getY(){
       return this.y;
     }
 }
 
  @Override
  public void start(Stage stage)
  {
   // initializeBoard();
  }
  
  
//  public static void main(String[] args)
//  {
//    System.out.println("This is being reached");
//    launch(args);
//  }
  
  
  
//  private class BoundingCircle
//  {
//    private final Actor ACTOR;
//    private final double RADIUS;
//    private final double RADIUS_SQUARED;
//    private double actorX;
//    private double actorY;
//    private double otherActorX;
//    private double otherActorY;
//    private double otherActorRadius;
//    private double roughDistance;
//    private double combinedRadii_Squared;
//
//    public BoundingCircle(Actor actor)
//    {
//      ACTOR = actor;
//      RADIUS = actor.getWidth() / 2.0;
//      RADIUS_SQUARED = RADIUS * RADIUS;
//    }
//  }
  
  
  
}






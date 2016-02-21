/*
 =================================================================
 Citation: http://gamedevelopment.tutsplus.com/tutorials/
 =================================================================
 */

package cs351.entities;

import java.util.LinkedList;
import java.util.Random;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class RoomTestThingy extends Application
{
  private Group root = new Group();
  private Scene scene = new Scene(root, 250, 250, Color.BLACK);

  // Board dimensions
  private int BOARD_WIDTH  = 31;
  private int BOARD_HEIGHT = 31;
  
  private int [][] boardArray = new int [BOARD_WIDTH][BOARD_HEIGHT];

  // min and max size of Rooms
  private int minRoomSize = 4;
  private int maxRoomSize = 10;

  // number of Rooms
  private int maxNumRooms = 50;
  
  private int colorVal = 0;

  // values that hold grid coordinates for each corner of the room
  private int x_StartPt;
  private int x_EndPt;
  private int y_StartPt;
  private int y_EndPt;
  
  // width and height of room in terms of grid
  private int width;
  private int height;

  private int x;
  private int y;

  public Point center;

  public int [][] getBoard()
  {
    return boardArray;
  }
  
  
  
  //Default constructor
  public RoomTestThingy() 
  {
    System.out.println("test constructor ");
//    initializeArray();
//    placeRooms();
  }

  // constructor for creating new rooms
  public RoomTestThingy (int x, int y, int width, int height)
  {
    x_StartPt = x;
    x_EndPt = x + width;
    y_StartPt = y;
    y_EndPt = y + width;

    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  /*
   =====================================================
   This will find the center of any created room and
   return the coordinates of this center
   =====================================================
   */
  public Point getPoints()
  {
    Point center = new Point((int) (Math.floor(x_StartPt + x_EndPt) / 2), 
                                    Math.floor((y_StartPt + y_EndPt) / 2));
    return center;
  }

  /*
   ===================================================
   This function will return true if there are no
   intersections with another room.
   ===================================================
   */
  public boolean intersects(RoomTestThingy room)
  {
    // All these conditions must be satisfied in order 
    //to not be an intersection of rooms
    return ( x_StartPt      <= room.x_EndPt     &&
               x_EndPt      >= room.x_StartPt   &&
               y_StartPt    <= room.y_EndPt     &&
               room.y_EndPt >= room.y_StartPt);
  }
  
   /* 
    ========================================================= 
    Next part here is to use random numbers to procedurally 
    generate and place a room
    =========================================================
   */

  public void placeRooms()
  {

    System.out.println("place rooms called");
    LinkedList<RoomTestThingy> rooms = new LinkedList<>();
    
    RoomTestThingy previousRoom;
    
    int new_Y;
    int new_X;
    int previous_X;
    int previous_Y;

    for (int j = 0; j < maxNumRooms; j++)
    {
      // randomize values for the rooms, Also initialize min and max room
      // size
      Random rnd = new Random();
      
      width  = minRoomSize + rnd.nextInt(maxRoomSize  - minRoomSize + 1);
      height = minRoomSize + rnd.nextInt(maxRoomSize  - minRoomSize + 1);
      x      = minRoomSize + rnd.nextInt(BOARD_WIDTH  - width - 1) + 1;
      y      = minRoomSize + rnd.nextInt(BOARD_HEIGHT - height - 1) + 1;
      
      //TODO find out why x and y go outside of the bounds,

      // Now create rooms with random values
      RoomTestThingy newRoom = new RoomTestThingy (x, y, width, height);

      boolean intersectingRooms = false;
      
      for (RoomTestThingy otherRoom : rooms)
      {
        if (newRoom.intersects(otherRoom))
        {
          intersectingRooms = true;
          break;
        }
      }
      if (!intersectingRooms)
      {
        createRoom(newRoom);

        new_X = (int) newRoom.getPoints().getX();
        new_Y = (int) newRoom.getPoints().getY();

        if (rooms.size() != 0)
        {
          //Store center of previous room
          //This gets the second to last room in the list
          if(rooms.size() > 1) {previousRoom = rooms.get(rooms.size() - 2);}
          else {previousRoom = rooms.get(0);}

          previous_X = (int) previousRoom.getPoints().getX();
          previous_Y = (int) previousRoom.getPoints().getY();

          if (rnd.nextInt(2) == 1)
          {
            horizontalHall(previous_X, new_X, previous_Y);
            verticalHall(previous_Y, new_Y, previous_X);
          } else
          {
            verticalHall(previous_Y, new_Y, previous_X);
            horizontalHall(previous_X, new_X, previous_Y);
          }
        }
      }
      if (!intersectingRooms) rooms.add(newRoom); //add new rooms into room array
    }
  }

  public void createRoom (RoomTestThingy newRoom)
  {
    int x   = newRoom.x_StartPt;
    int x_2 = newRoom.x_EndPt;
    int y   = newRoom.y_StartPt;
    int y_2 = newRoom.y_EndPt;
    
    Color blu = Color.BLUE;
    Color grn = Color.GREEN;
    Color red = Color.RED;
    Color pnk = Color.PINK;
    
    Color color;
    
    if( colorVal > 2 ) colorVal = 0;
    
    //Switch room colors to make the model more interesting
    switch(colorVal)
    {
      case 0: color = blu;
        break;
        
      case 1: color = grn;
        break;
        
      case 2: color = red;
        break;
        
     default: color = pnk;
    }

    //draws the dimensions of a rectangle
    for (int j = x; j < x_2; j++)
    {
      for (int k = y; k < y_2; k++)
      {
//        Rectangle r = new Rectangle(j, k, 1, 1);
//        r.setFill(color);
//        root.getChildren().add(r);
        
        /********************************************/
        try
        {
          // prevents a seg fault
          if (j < BOARD_WIDTH && k < BOARD_HEIGHT)
            boardArray[j][k] = 0;

        } catch (IndexOutOfBoundsException e)
        {
          System.out.println("out of bounds #1");
          System.out.println(" j = " + j + " k = " + k);
        }
        /*********************************************/
      }
    }
    
    colorVal++;

//    stage.setTitle("Procedurally Generated Dungeon Prototype");
//    stage.setScene(scene);
//    stage.show();
  }

  
   /*======================================================================= 
    The following methods will connect each room with hallways. The point
     of this is to make every room accessible.
    
    The idea here is when a new room is generated, a path is created from
    the center of the previous room, to the center of the newly created room.
    =========================================================================
   */
  public void horizontalHall(int previous_X, int new_X, int previous_Y)
  {
    //Defining the minimum value guarantees that the hallway will go in
    //the correct direction. It can be left or right, but as long as 
    //the loops starts at the min value, the path will be correct.
    int minVal = Math.min(previous_X, new_X);
    int maxVal = Math.max(previous_X, new_X);

    //Start at the previous x point and draw the path to the current x point
    for (int j = minVal; j < maxVal + 1; j++)
    {
      // X direction increases, y stays fixed
//      Rectangle r = new Rectangle(j, previous_Y, 1, 1);
//      r.setFill(Color.PINK);
//      root.getChildren().add(r);
      
      /***********************************************************/
      try
      {
        // prevents a seg fault
        if (j < BOARD_WIDTH && previous_Y < BOARD_HEIGHT)
          boardArray[j][previous_Y] = 0;

      } catch (IndexOutOfBoundsException e)
      {
        System.out.println("out of bounds #2");
        System.out.println(" j = " + j + " prev Y = " + previous_Y);
      }
      /***********************************************************/
    }
  }

  public void verticalHall(int previous_Y, int new_Y, int previous_X)
  {
    int minY_Val = Math.min(previous_Y, new_Y);
    int maxY_Val = Math.max(previous_Y, new_Y);

    // Y direction increases, x stays fixed
    for (int j = minY_Val; j < maxY_Val + 1; j++)
    {
//      Rectangle r = new Rectangle(previous_X, j, 1, 1);
//      r.setFill(Color.PINK);
//      root.getChildren().add(r);
      
      /**********************************************************/
      try
      {
        // prevents a seg fault
        if (j < BOARD_WIDTH && previous_X < BOARD_HEIGHT)
          boardArray[previous_X][j] = 0;

      } catch (IndexOutOfBoundsException e)
      {
        System.out.println("out of bounds #3");
        System.out.println(" prev X = " + previous_X +" j = " + j);
      }
      /***********************************************************/
    }
  }
  
  public void initializeArray(){
    System.out.println("initialize Array");
    for (int x = 0; x < 31; x++)
    {
      for (int y = 0; y < 31; y++)
      {
        boardArray[x][y] = 1;
      }
    }
  }
  
  /*
   ==========================================
   This just returns numbers that correspond 
   to the type of texture each room will 
   have. The point is to make each room have
   a different look to it
   ===========================================
   */
  public void getTextureNumber()
  {
    
  }
  public void printArray(){
    System.out.println("print Array");
    for (int x = 0; x < 31; x++)
    {
      for (int y = 0; y < 31; y++)
      {
        System.out.print(boardArray[x][y]);
      }System.out.println("\n");
    }System.out.println("\n");
  }
  
  public int[][] getArray()
  {
    return(boardArray);
  }
  
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
    initializeArray();
    placeRooms();
    printArray();
  }

  public static void main(String[] args)
  {
    launch(args);
  }

}
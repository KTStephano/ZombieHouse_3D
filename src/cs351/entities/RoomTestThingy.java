package cs351.entities;

//package application;

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
  private Scene scene = new Scene(root, 500, 500, Color.BLACK);

  // Board dimensions
  private int BOARD_WIDTH  = 300;
  private int BOARD_HEIGHT = 300;

  // min and max size of Rooms
  private int minRoomSize = 10;
  private int maxRoomSize = 50;

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

  //Default constructor
  public RoomTestThingy() {}

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

  public void placeRooms(Stage stage)
  {

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

      // Now create rooms with random values
      RoomTestThingy newRoom = new RoomTestThingy (x, y, width, height);

      boolean failed = false;
      
      for (RoomTestThingy otherRoom : rooms)
      {
        if (newRoom.intersects(otherRoom))
        {
          failed = true;
          break;
        }
      }
      if (!failed)
      {
        createRoom(newRoom, stage);

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
      if (!failed) rooms.add(newRoom); //add new rooms into room array
    }
  }

  public void createRoom (RoomTestThingy newRoom, Stage stage)
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

    for (int j = x; j < x_2; j++)
    {
      for (int k = y; k < y_2; k++)
      {
        Rectangle r = new Rectangle(j, k, 1, 1);
        r.setFill(color);
        root.getChildren().add(r);
      }
    }
    
    colorVal++;

    stage.setTitle("JavaFX Scene Graph Demo");
    stage.setScene(scene);
    stage.show();
  }

  
   /*====================================================================== 
    The following methods will connect each room with hallways. The point
     of this is to make every room accessible.
    
    A point variable is used to keep track of the center of each room. 
    When this center is defined, we will connect it to the previous room's 
    center.
    ========================================================================
   */
  public void horizontalHall(int x_start, int x_end, int y_start)
  {
    int minVal = Math.min(x_start, x_end);
    int maxVal = Math.max(x_start, x_end);

    int distance = (maxVal - (minVal) + 1);

    System.out.println("for loop x "+ x+" distance is "+ distance);
    for (int j = minVal; j < maxVal + 1; j++)
    {
      // X direction increases, y stays fixed
      Rectangle r = new Rectangle(j, y, 5, 5);
      r.setFill(Color.BLUE);
      root.getChildren().add(r);
    }
  }

  public void verticalHall(int y_start, int y_end, int x)
  {
    int minY_Val = Math.min(y_start, y_end);
    int maxY_Val = Math.max(y_start, y_end);

    // Y direction increases, x stays fixed
    for (int j = minY_Val; j < maxY_Val + 1; j++)
    {
      Rectangle r = new Rectangle(x, j, 5, 5);
      r.setFill(Color.BLUE);
      root.getChildren().add(r);
    }
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
    placeRooms(stage);
  }

  public static void main(String[] args)
  {
    launch(args);
  }

}
package cs351.entities;

//package application;



import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
 
public class RoomTestThingy extends Application {
 
   private Scene r;

  @Override
   public void start(Stage stage) {
       Group root = new Group();
       Scene scene = new Scene(root, 500, 500, Color.BLACK);
 
      // Rectangle r = new Rectangle(25,25,5,5);
       
    for (int x = 100; x < 200; x++)
    {
      for (int y = 100; y < 200; y++)
      {
        if(x == 100 || x == 199)
        {
        Rectangle r = new Rectangle(x, y, 1, 1);
        r.setFill(Color.BLUE);
        root.getChildren().add(r);
        }
        if(y == 100)
        {
          Rectangle r = new Rectangle(x, y, 1, 1);
          r.setFill(Color.BLUE);
          root.getChildren().add(r);
        }
      }
    }
 
       stage.setTitle("JavaFX Scene Graph Demo");
       stage.setScene(scene);
       stage.show();
   }
 
   public static void main(String[] args) {
       launch(args);
   }
}

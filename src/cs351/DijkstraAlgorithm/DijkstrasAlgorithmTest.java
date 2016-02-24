package cs351.DijkstraAlgorithm;

import javafx.geometry.Point2D;

public class DijkstrasAlgorithmTest {

  public static void main(String[] args) {
   
  
    TestDijkstraAlgorithm t = new TestDijkstraAlgorithm();
  
    t.executeTest();
    
    boolean [][] board = new boolean[4][4];
     
    for (int n=0;n<16;n++)
    {
      board[n/4][n%4]=true;
    }
    t.initGraph(board, 4, 4);
    Point2D pt = t.getNextLocation(1,1,3,3);
    if (pt==null)
    {
      System.out.println("bad news null");
    }
    else
    {
      System.out.println("x "+pt.getX()+" y "+pt.getY());           
    }
      
  }
}

package cs351.DijkstraAlgorithm;
/*
 * 
 * Lars Vogel
 * Version 1.1
 * 
 * Copyright © 2009-2011, 2011 vogella GmbH
 * 
 * 02.11.2009
 * 
 * http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html
 * 
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import cs351.DijkstraAlgorithm.DijkstraAlgorithm;
import cs351.DijkstraAlgorithm.Edge;
import cs351.DijkstraAlgorithm.Graph;
import cs351.DijkstraAlgorithm.Vertex;
import javafx.geometry.Point2D;


public class TestDijkstraAlgorithm {

  private List<Vertex> nodes;
  private List<Edge> edges;
  private Graph graph;
  private DijkstraAlgorithm dijkstra;

  public void executeTest(boolean [][] board, int widthBoard, int heightBoard) {
    nodes = new ArrayList<Vertex>();
    edges = new ArrayList<Edge>();
    int start=0;
    int end=0;
    boolean haveStart=false;
    boolean haveEnd = false;
    
    for (int x = 0; x < widthBoard; x++) {
      for (int y = 0; y < heightBoard; y++) {
        Vertex location = new Vertex(x,y);
        nodes.add(location);
       
        if (board[x][y]) 
        {
          if (!haveStart)
          {
            start = nodes.indexOf(location);  
            haveStart = true;
          }
          else if (!haveEnd)
          {
            haveEnd = true;
            end = nodes.indexOf(location);          
          }
        }
      }
    }



    for (int x = 0; x < widthBoard; x++) {
      for (int y = 0; y < heightBoard; y++) {
        if (board[x][y]) 
        {

          for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {

              if ((i!=0)&&(j!=0))
              {
                if ((x+i>=0 )&&(y+j>=0))
                {
                  if (board[x+i][y+j]) {  
                    addEdge(nodes.indexOf(new Vertex(x,y)) ,nodes.indexOf(new Vertex(x+i,y+j)));
                  }
                }
              }
            }
          }

        }
      }
    }


    Graph graph = new Graph(nodes, edges);
    DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);

    dijkstra.execute(nodes.get(start));
    LinkedList<Vertex> path = dijkstra.getPath(nodes.get(end));

    if (path!=null)
    {
      for (Vertex vertex : path) {
        System.out.println(vertex);
      }
    }

  }




  public void initGraph(boolean [][] board, int widthBoard, int heightBoard) {
    nodes = new ArrayList<Vertex>();
    edges = new ArrayList<Edge>();


    for (int x = 0; x < widthBoard; x++) {
      for (int y = 0; y < heightBoard; y++) {
        Vertex location = new Vertex(x,y);
        nodes.add(location);
      }
    }



    for (int x = 0; x < widthBoard; x++) {
      for (int y = 0; y < heightBoard; y++) {
        if (board[x][y]) 
        {

          for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {

              if ((i!=0)&&(j!=0))
              {
                if ((x+i>=0 )&&(y+j>=0))
                {
                  if (board[x+i][y+j]) {  
                    addEdge(nodes.indexOf(new Vertex(x,y)) ,nodes.indexOf(new Vertex(x+i,y+j)));
                  }
                }
              }
            }
          }
        }
      }
    }


    graph = new Graph(nodes, edges);
    dijkstra = new DijkstraAlgorithm(graph);


  }

  public Point2D getNextLocation(int currentX, int currentY, int targetX, int targetY)
  {
    Point2D pt=null;
    int start = nodes.indexOf(new Vertex(currentX,currentY));
    int end = nodes.indexOf(new Vertex(targetX,targetY));
    if ((start == -1)||(end==-1))
    {
      return null;
    }
    Vertex vStart = nodes.get(start);
    Vertex vEnd = nodes.get(end);
    if ((vStart!=null)&&(vEnd!=null))
    {
      dijkstra.execute(vStart);  
      LinkedList<Vertex> path = dijkstra.getPath(vEnd);

      if (path!=null)
      {
        int x = path.getFirst().getX();
        int y = path.getFirst().getY();
        pt = new Point2D(x,y);
      }  
    }

    return pt;
  }

  private void addEdge(int sourceLocNo, int destLocNo) {
    Edge anEdge = new Edge(nodes.get(sourceLocNo), nodes.get(destLocNo));
    edges.add(anEdge);
  }


} 

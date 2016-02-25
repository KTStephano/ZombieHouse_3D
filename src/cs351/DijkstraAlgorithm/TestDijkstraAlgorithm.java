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

  public void executeTest() {
    nodes = new ArrayList<Vertex>();
    edges = new ArrayList<Edge>();

    nodes.add(new Vertex(0,0));
    nodes.add(new Vertex(0,1));
    nodes.add(new Vertex(1,0));
    nodes.add(new Vertex(1,1));
       

    int start = nodes.indexOf(new Vertex(0,0));
    int end = nodes.indexOf(new Vertex(0,1));
    
    addEdge(nodes.indexOf(new Vertex(0,0)) ,nodes.indexOf(new Vertex(0,1)));
    addEdge(nodes.indexOf(new Vertex(0,0)) ,nodes.indexOf(new Vertex(1,1)));
    addEdge(nodes.indexOf(new Vertex(0,0)) ,nodes.indexOf(new Vertex(1,0)));

       
    

    Graph graph = new Graph(nodes, edges);
    DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);

    dijkstra.execute(nodes.get(start));
    LinkedList<Vertex> path = dijkstra.getPath(nodes.get(end));
    System.out.println("nothing yet...");
    if (path!=null)
    {
      for (Vertex vertex : path) {
        System.out.println(vertex);
      }
    }
    System.out.println("...and now we're done.");

  }




  public void initGraph(boolean [][] board, int widthBoard, int heightBoard) {
    nodes = new ArrayList<Vertex>();
    edges = new ArrayList<Edge>();


    for (int x = 0; x < widthBoard; x++) {
      for (int y = 0; y < heightBoard; y++) {
        nodes.add(new Vertex(x,y));
      }
    }



    for (int x = 1; x < widthBoard-1; x++) {
      for (int y = 1; y < heightBoard-1; y++) {

        addEdge(nodes.indexOf(new Vertex(x,y)) ,nodes.indexOf(new Vertex(x+0,y+1)));
        addEdge(nodes.indexOf(new Vertex(x,y)) ,nodes.indexOf(new Vertex(x+1,y+1)));
        addEdge(nodes.indexOf(new Vertex(x,y)) ,nodes.indexOf(new Vertex(x+1,y+0)));
        addEdge(nodes.indexOf(new Vertex(x,y)) ,nodes.indexOf(new Vertex(x+0,y-1)));
        addEdge(nodes.indexOf(new Vertex(x,y)) ,nodes.indexOf(new Vertex(x+0,y-1)));
        addEdge(nodes.indexOf(new Vertex(x,y)) ,nodes.indexOf(new Vertex(x-1,y-1)));
        addEdge(nodes.indexOf(new Vertex(x,y)) ,nodes.indexOf(new Vertex(x-1,y+0)));
        addEdge(nodes.indexOf(new Vertex(x,y)) ,nodes.indexOf(new Vertex(x+0,y-1)));
      
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
        
        path.remove(path.getFirst());
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

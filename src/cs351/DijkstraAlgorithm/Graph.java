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
import java.util.List;

public class Graph {
  private final List<Vertex> vertexes;
  private final List<Edge> edges;

  public Graph(List<Vertex> vertexes, List<Edge> edges) {
    this.vertexes = vertexes;
    this.edges = edges;
  }

  public List<Vertex> getVertexes() {
    return vertexes;
  }

  public List<Edge> getEdges() {
    return edges;
  }
  
  
  
} 


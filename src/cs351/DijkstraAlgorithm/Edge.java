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

public class Edge  {
  private final Vertex source;
  private final Vertex destination;
  
  public Edge(Vertex source, Vertex destination) {
    this.source = source;
    this.destination = destination;
  }
  
  public Vertex getDestination() {
    return destination;
  }

  public Vertex getSource() {
    return source;
  }
 
  
  @Override
  public String toString() {
    return source + " " + destination;
  }
  
  
} 
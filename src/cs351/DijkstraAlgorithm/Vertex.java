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
public class Vertex {
  final private int x;
  final private int y;
  final private String id;
  final private String name;
  
  
  public Vertex(int x,int y) {
    this.id =  ""+x+"!"+y;
    this.name =  "x: "+ x +"y: "+ y;
    this.x = x;
    this.y = y;
  }
  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }
  
  public int getX() {
    return x;
  }
  
  public int getY() {
    return y;
  }
   
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Vertex other = (Vertex) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return name;
  }

} 
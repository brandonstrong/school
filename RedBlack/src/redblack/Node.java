/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redblack;

/**
 *
 * @author Brandon
 */

public class Node
{
	
     public boolean isRed;
	
     private int item;
     private Node left;
     private Node right;
     
     Node(int a)
  {
      item = a;
      isRed = true;
  }
  
  public int getItem()
  {
      return item;
  }
  
  public void setRight(Node i)
  {
     right = i;
   }
  public void setLeft(Node i)
  {
     left = i;
   }
   public Node getLeft()
   { 
       return left;
   }
   public Node getRight()
   {
      return right;
   }
  
}


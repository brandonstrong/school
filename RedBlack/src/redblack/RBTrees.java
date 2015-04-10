/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redblack;

/**
 *
 * @author Brandon
 */

public class RBTrees
{
		
	 private boolean fixupRequired;
	 private boolean addReturn;
     private Node root;
     
  RBTrees()
	{
	   root = null;
	   fixupRequired = false;
	}
	
  public boolean add(int item) {
    if (root == null) {
      root = new Node (item);
      root.isRed = false; // root is black.
      return true;
    }
    else {
      root = add(root, item);
      root.isRed = false; // root is always black.
      return addReturn;
    }

  }


  private Node add(Node localRoot, int item) {
    if (localRoot.getItem() == item) {
      // item already in the tree.
      addReturn = false;
      return localRoot;
    }

    else if (item < localRoot.getItem()) {
      // item < localRoot.data.
      if (localRoot.getLeft() == null) {
        // Create new left child.
        localRoot.setLeft(new Node(item));
        addReturn = true;
        return localRoot;
      }

      else { // Need to search.
        // Check for two red children, swap colors if found.
        moveBlackDown(localRoot);
        // Recursively add on the left.
        localRoot.setLeft(add(localRoot.getLeft(), item));

        // See whether the left child is now red
        if (localRoot.getLeft().isRed) {

          if (localRoot.getLeft().getLeft() != null
              && ( localRoot.getLeft().getLeft()).isRed) {
            // Left-left grandchild is also red.

            // Single rotation is necessary.
            localRoot.getLeft().isRed = false;
            localRoot.isRed = true;
            return rotateRight(localRoot);
          }
          else if (localRoot.getLeft().getRight() != null
                   && (localRoot.getLeft().getRight()).isRed) {
            // Left-right grandchild is also red.
            // Double rotation is necessary.
            localRoot.setLeft(rotateLeft(localRoot.getLeft()));
            localRoot.getLeft().isRed = false;
            localRoot.isRed = true;
            return rotateRight(localRoot);
          }
        }
        return localRoot;
      }
    }

    else {
      // item > localRoot.data
      if (localRoot.getRight() == null) {
        // create new right child
        localRoot.setRight(new Node(item));
        addReturn = true;
        return localRoot;
      }
      else { // need to search
        // check for two red children swap colors
        moveBlackDown(localRoot);
        // recursively insert on the right
        localRoot.setRight(add(localRoot.getRight(), item));
        // see if the right child is now red
        if (localRoot.getRight().isRed) {
          if (localRoot.getRight().getRight() != null
              && ( localRoot.getRight().getRight()).isRed) {
            // right-right grandchild is also red
            // single rotate is necessary
            localRoot.getRight().isRed = false;
            localRoot.isRed = true;
            return rotateLeft(localRoot);
          }
          else if (localRoot.getRight().getLeft() != null
                   && (localRoot.getRight().getLeft().isRed)) {
            // left-right grandchild is also red
            // double rotate is necessary
            localRoot.setRight(rotateRight(localRoot.getRight()));
            localRoot.getRight().isRed = false;
            localRoot.isRed = true;
            return rotateLeft(localRoot);
          }
        }
        return localRoot;
      }
    
    }
  }

  private void moveBlackDown(Node localRoot) {
    // see if both children are red
    if (localRoot.getLeft() != null && localRoot.getRight() != null
        && localRoot.getLeft().isRed
        && localRoot.getRight().isRed )
        {
      //make them black and myself red
           localRoot.getLeft().isRed = false;
           localRoot.getRight().isRed = false;
           localRoot.isRed = true;
    }
  }
  
   private Node  rotateRight(Node  root) {
    System.out.println("Rotating Right");
    Node temp = root.getLeft();
    root.setLeft(temp.getRight());
    temp.setRight(root);
    return temp;
  }


   private Node rotateLeft(Node localRoot) {
     System.out.println("Rotating Left");
     Node temp = localRoot.getRight();
     localRoot.setRight(temp.getLeft());
     temp.setLeft(localRoot);
     return temp;
   }
}


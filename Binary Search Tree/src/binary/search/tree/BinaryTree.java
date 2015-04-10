package binary.search.tree;


/**
 *
 * @author Brandon
 * @class CSCI 232
 * @version 1/24/2014
 */
public class BinaryTree {

    TreeNode root;
    int numNodes;

    public BinaryTree() {
        root = null;
        numNodes = 0;
    }

    /**
     * This method will insert a Node                               INSERT NODE
     * @param n 
     */
    public void insert(TreeNode n) {
        numNodes++;
        if (root == null) {
            root = n;
            return;
        }
        TreeNode curNode = root; // root is not null
        while (true) {
            if (n.getDataInt() <= curNode.getDataInt()) {
                if (curNode.getLeft() == null) {
                    curNode.setLeft(n);
                    n.setParent(curNode);
                    return;
                }
                curNode = curNode.getLeft();
            } else {
                if (curNode.getRight() == null) {
                    curNode.setRight(n);
                    n.setParent(curNode);
                    return;
                }
                curNode = curNode.getRight();
            }
        }
    }

    public int getNumNodes() {
        return numNodes;
    }

    public TreeNode getRoot() {
        return root;
    }

    /**
     * This searches for a node                                     SEARCH NODE
     * @param value
     * @return 
     */
    public TreeNode search(int value) {
        if (root == null) {
            return null;
        }
        return root.searchFrom(value);
    }
    
    /**
     * This searches for depth of tree                              TREE DEPTH
     * @return 
     */
    public int depth() {
        if (root == null) {
            return 0;
        }
        return root.findMaxDepth();
    }

    /**
     * This will delete a node                                      DELETE NODE
     * @param n 
     */
    public void delete(TreeNode n) {
        if (root == null) {
            System.out.println("Error: trying to delete a node from an empty tree");
            return;
        }
        if (!root.searchForNode(n)) {
            System.out.println("Error: trying to delete a node (" + n + ") that does not exist in tree " + this);
            return;
        }
        numNodes--;

        TreeNode parent = n.getParent();

        // case 1: n is a leaf
        if (n.getLeft() == null && n.getRight() == null) {
            if (parent == null) {
                root = null;
                return;
            }
            if (n == parent.getLeft()) {
                parent.setLeft(null);
                return;
            } else {
                parent.setRight(null);
                return;
            }
        }

        // case 2: n has a single descendant
        if (n.getLeft() == null || n.getRight() == null) {
            TreeNode child;
            if (n.getLeft() != null) {
                child = n.getLeft();
            } else {
                child = n.getRight();
            }
            if (parent != null) {
                if (n == parent.getLeft()) {
                    parent.setLeft(child);
                    child.setParent(parent);
                    return;
                } else {
                    parent.setRight(child);
                    child.setParent(parent);
                    return;
                }
            } else {
                root = child;
                root.setParent(null);
                return;
            }
        }

        // case 3: n has two descendants
        TreeNode predecessor = n.getLeft();
        while (predecessor.getRight() != null) {
            predecessor = predecessor.getRight();
        }

        TreeNode predParent = predecessor.getParent();
        if (predParent != n) {
            predParent.setRight(predecessor.getLeft());
        } else {
            predParent.setLeft(predecessor.getLeft());
        }
        if (predecessor.getLeft() != null) {
            predecessor.getLeft().setParent(predParent);
        }

        if (parent != null) {
            if (n == parent.getLeft()) {
                parent.setLeft(predecessor);
            } else {
                parent.setRight(predecessor);
            }
        } else {
            root = predecessor;
            root.setParent(null);
        }
        predecessor.setParent(parent);
        predecessor.setLeft(n.getLeft());
        if (predecessor.getLeft() != null) {
            predecessor.getLeft().setParent(predecessor);
        }
        predecessor.setRight(n.getRight());
        if (predecessor.getRight() != null) {
            predecessor.getRight().setParent(predecessor);
        }
    }

    
    /**
     * Three traversal methods
     * @param n 
     */
    //Pre Order Traversal
    public static void preOrderTraversal(TreeNode n) {
        if (n != null) {
            System.out.print(" " + n.getDataInt());
        }
        if (n != null && n.getLeft() != null) {
            preOrderTraversal(n.getLeft());
        }
        if (n != null && n.getRight() != null) {
            preOrderTraversal(n.getRight());
        }
    }
    
    //In Order Traversal
    public static void inOrderTraversal(TreeNode n) {
        if (n != null && n.getLeft() != null) {
            inOrderTraversal(n.getLeft());
        }
        if (n != null) {
            System.out.print(" " + n.getDataInt());
        }
        if (n != null && n.getRight() != null) {
            inOrderTraversal(n.getRight());
        }
    }

    //Post Order Traversal
    public static void postOrderTraversal(TreeNode n) {
        if (n != null && n.getLeft() != null) {
            postOrderTraversal(n.getLeft());
        }
        if (n != null && n.getRight() != null) {
            postOrderTraversal(n.getRight());
        }
        if (n != null) {
            System.out.print(" " + n.getDataInt());
        }
    }
}

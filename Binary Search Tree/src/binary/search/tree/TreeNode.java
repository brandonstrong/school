package binary.search.tree;


/**
 *
 * @author Brandon
 * @class CSCI 232
 * @version 1/24/2014
 */
public class TreeNode {
    
    String dataStr;
    int dataInt;
    TreeNode parent;
    TreeNode left;
    TreeNode right;
    protected int column;
    protected int row;
    protected int center;

    public TreeNode(int d, String s) {
        dataStr = s;
        dataInt = d;
        parent = null;
        left = null;
        right = null;
    }

    public int getDataInt() {
        return dataInt;
    }
    
    public String getDataStr(){
        return dataStr;
    }

    public void setParent(TreeNode n) {
        parent = n;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setLeft(TreeNode n) {
        left = n;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setRight(TreeNode n) {
        right = n;
    }

    public TreeNode getRight() {
        return right;
    }

    public TreeNode searchFrom(int value) {
        if (dataInt == value) {
            System.out.println("The integer value is: "+this.getDataInt()+" and the string is: "+this.getDataStr());
            return this;
        }
        if (value < dataInt) {
            if (left == null) {
                return null;
            }
            return left.searchFrom(value);
        } else {
            if (right == null) {
                return null;
            }
            return right.searchFrom(value);
        }
    }

    public boolean searchForNode(TreeNode searchNode) {
        if (this == searchNode) {
            return true;
        }
        if (left != null && left.searchForNode(searchNode)) {
            return true;
        }
        if (right != null && right.searchForNode(searchNode)) {
            return true;
        }
        return false;
    }

    public int findMaxDepth() {
        int lmax = 0;
        int rmax = 0;
        if (left != null) {
            lmax = left.findMaxDepth();
        }
        if (right != null) {
            rmax = right.findMaxDepth();
        }
        return 1 + Math.max(lmax, rmax);
    }

    public String toString() {
        return "(" + dataInt + ")";
    }
}

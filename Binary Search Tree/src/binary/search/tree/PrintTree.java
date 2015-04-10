package binary.search.tree;

/**
 *
 * @author Brandon
 * @class CSCI 232
 * @version 1/24/2014
 */
public class PrintTree {

    // some static variables for printing:
    public static int curCol;
    public static char[][] board;

    public static int getWidth(TreeNode n, int depth, int colOffset) {
        // print nodes belonging to subtree of this node
        int lwidth = 0;
        int rwidth = 0;
        if (n.getLeft() != null) {
            lwidth = getWidth(n.getLeft(), depth + 1, colOffset);
        }
        if (n.getRight() != null) {
            rwidth = getWidth(n.getRight(), depth + 1, colOffset + lwidth + 2);
        }
        char[] data = ("" + n.getDataInt()).toCharArray();
        int width = Math.max(lwidth + rwidth + 2, data.length + 2);


        n.column = colOffset + width / 2 - data.length / 2 - 1;
        n.center = n.column + 1 + data.length/2;
        n.row = 4 * depth;

        return width;
    }

    public static void recFillNodes(TreeNode n) {
        char[] data = ("" + n.getDataInt()).toCharArray();
        for (int i = 0; i < data.length; i++) {
            board[n.row][n.column + i + 1] = data[i];
        }
        board[n.row][n.column] = '(';
        board[n.row][n.column + data.length + 1] = ')';

        if (n.getLeft() != null) {
            recFillNodes(n.getLeft());
        }
        if (n.getRight() != null) {
            recFillNodes(n.getRight());
        }
    }

    public static void recFillEdges(TreeNode n) {
        if (n.getLeft() != null) {
            board[n.row+1][n.center] = '|';
            board[n.row+2][n.center] = '+';
            board[n.getLeft().row-2][n.getLeft().center] = '+';
            board[n.getLeft().row-1][n.getLeft().center] = '|';
            for (int j = n.getLeft().center + 1; j < n.center; j++) {
                board[n.getLeft().row-2][j] = '-';
            }
            recFillEdges(n.getLeft());
        }
        if (n.getRight() != null) {
            board[n.row+1][n.center] = '|';
            board[n.row+2][n.center] = '+';
            board[n.getRight().row-2][n.getRight().center] = '+';
            board[n.getRight().row-1][n.getRight().center] = '|';
            for (int j = n.center + 1; j < n.getRight().center; j++) {
                board[n.getRight().row-2][j] = '-';
            }
            recFillEdges(n.getRight());
        }

    }

    public static void printTree(BinaryTree tree) {

        if (tree.getRoot() == null) {
            System.out.println("(null)");
            return;
        }
        int width = getWidth(tree.getRoot(), 0, 0);
        int depth = 4 * tree.depth() -3;

        board = new char[depth][width];
        for (int i = 0; i < depth; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = ' ';
            }
        }
        if (tree.getRoot() != null) {
            recFillNodes(tree.getRoot());
            recFillEdges(tree.getRoot());

            for (int i = 0; i < depth; i++) {
                for (int j = 0; j < width; j++) {
                    System.out.print(board[i][j]);
                }
                if (i % 4 == 0) {
                    System.out.println(" <- depth " + i/4);
                } else {
                    System.out.println();
                }
            }
        }
    }
}

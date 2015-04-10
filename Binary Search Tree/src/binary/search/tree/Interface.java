package binary.search.tree;

/**
 *
 * @author Brandon
 * @class CSCI 232
 * @version 1/24/2014
 */
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class Interface extends JFrame {

    /*
     * Set up variables for interface
     */
    JTextField inputInt,
               inputStr;
    JButton insert,
            delete,
            search,
            findOrder,
            print,
            quit;

    /*
     * Main method for program
     */
    public static void main(String[] args) {
        JFrame iFrame = new Interface();
        iFrame.setVisible(true);
        iFrame.setSize(500, 150);
        iFrame.setLocation(500, 100);
    }

    /*
     * Run the interface
     */
    public Interface() {
        final BinaryTree t = new BinaryTree();
        setTitle("Binary Tree");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set up JPanels
        JPanel tField = new JPanel();
        JPanel bField = new JPanel();

        //Assign variables to JPanels
        inputInt = new JTextField(15);
        inputStr = new JTextField(15);
        insert = new JButton("Insert");
        delete = new JButton("Delete");
        search = new JButton("Search");
        findOrder = new JButton("Order");
        print = new JButton("Print Tree");
        quit = new JButton("Quit");
        
        
        //Add varables (buttons and text fields) to Interface window
        tField.add(new JLabel("Enter an Integer:                       "));
        tField.add(inputInt);
        tField.add(new JLabel("Enter a String:                             "));
        tField.add(inputStr);
        tField.add(new JLabel("Select an Operation:                "));
        bField.add(insert);
        bField.add(delete);
        bField.add(search);
        bField.add(findOrder);
        bField.add(print);
        bField.add(quit);
        
        //Show Interface Window
        bField.setLayout(new BoxLayout(bField, BoxLayout.X_AXIS));

        /*
         * Handle the insert Button
         */
        insert.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int dataInt = Integer.parseInt(inputInt.getText());
                String dataStr = inputStr.getText();
                TreeNode node = new TreeNode(dataInt, dataStr);  
                t.insert(node);
                System.out.println(inputInt.getText() + " was added");
                inputStr.setText(null);
                inputInt.setText(null);
            }
        });
        
        /*
         * Handle the Search button
         */
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dataInt = Integer.parseInt(inputInt.getText()); 
                System.out.println("Searching for: " + dataInt + "...");
                inputStr.setText(null);
                inputInt.setText(null);
                t.search(dataInt);
            }
        });
        
        /*
         * Handle the delete button
         */
        delete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int dataInt = Integer.parseInt(inputInt.getText()); 
                System.out.println("Deleting : " + dataInt + "...");
                inputStr.setText(null);
                inputInt.setText(null);
                t.delete(t.search(dataInt));
            }
        });
        
        /*
         * Handle the find order button (find order will print post, in, and pre order traversals of tree t)
         */
         findOrder.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Driver.mainDriver(t);
            }
        });
         
         /*
          * Handles print button (print will output the tree)
          */
         print.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                PrintTree.printTree(t);
            }
        });
         
         /*
          * Handle the quit button (quit button will exit program)
          */
        quit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        setContentPane(tField);
        getContentPane().add(bField);
    }
}

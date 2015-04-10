package redblack;

import java.util.*;

public class Driver
{
	public static void main(String [] args)
	{
	   RBTrees rb = new RBTrees();
	   Scanner scan = new Scanner(System.in);
	   int start = 0;
	   do{
	     System.out.println("Add what? (-1 to quit)");
	     start = scan.nextInt();
	     scan.nextLine();
	     boolean ans = rb.add(start);
	     if(!ans)
	        System.out.println(start + " not added");
	    } while(start != -1);
	    
	    System.out.println("Goodbye");
	}
}

package a4_JonahKubath;

import java.util.Scanner;

/**
 * This class is used to read all input from the command line / standard input
 * @author Jonah Kubath
 *
 */
public class ScanObject {
	//Global scanner used for user input from the command line
	static Scanner scan = new Scanner(System.in);
	
	/**
	 * Read the next line in from standard input
	 * @return String return the line read
	 */
	public static String nextLine() {
		return scan.nextLine();
	}
	
	/**
	 * Cleanup the scanner used for input
	 */
	public static void closeScanner() {
		scan.close();
	}
	
}

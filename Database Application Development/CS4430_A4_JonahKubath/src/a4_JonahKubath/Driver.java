package a4_JonahKubath;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Driver - this is the main driver for the application.  Receives the user's
 * choice and selects the appropriate database action to take.
 * @author Jonah Kubath
 *
 */
public class Driver {
	public static void mainDriver() throws SQLException {
		int choice = 0;
		
		while(choice != 7) {
			//get user input
			choice = getInput();
			//System.out.println("Your choice - " + choice);
			try {
				//Turn off auto commit to allow for transactions
				Database.getConnection().setAutoCommit(false);
				
				//Call appropriate action
				switch(choice) {
					case 1:
						Database.addCustomer();
						break;
					case 2:
						Database.addOrder();
						break;
					case 3:
						Database.deleteOrder();
						break;
					case 4:
						Database.shipOrder();
						break;
					case 5:
						Database.printOrderDetails();
						break;
					case 6:
						Database.restockParts();
						break;
					case 7:
						//Exit the program
						return;
				} //End of switch statement
				
				//Commit the changes
				Database.getConnection().commit();
			}
			catch(Exception e) {
				//Rollback the changes if an error was thrown
				System.out.println("Error was caught, changes were reverted");
				Database.getConnection().rollback();
			}
			
		}//End of while loop
		
		//Close the scanner used to read from Standard Input
		ScanObject.closeScanner();
		
	}
	
	/**
	 * Reads the user's input choice from standard input and returns it
	 * Error checking with only integers and integers in the bounds of 
	 * the choices.
	 * @return Integer - the option selected by the user
	 */
	public static int getInput() {
		int choice = 0;
		boolean goodInput = false;
		int maxAction = 7;
		int minAction = 1;
		
		//Print options
		System.out.println("Enter the number to perform an action");
		System.out.println("1. add a customer");
		System.out.println("2. add an order");
		System.out.println("3. remove an order");
		System.out.println("4. ship an order");
		System.out.println("5. print pending orders with customer information");
		System.out.println("6. restock parts");
		System.out.println("7. exit");
		
		while(!goodInput) {
			try {
				//Get an integer from the user
				choice = getInteger(1);
				
				//Check to make sure it is in bounds of the options
				if(choice < minAction || choice > maxAction) {
					throw new ArithmeticException();
				}
				//Break the loop since we have good input
				goodInput = true;
				
			}
			catch(ArithmeticException e) {
				System.out.println("ERROR: Enter a number from 1 to 7");
			}
		
		}
		
		return choice;
	
	}
	
	/**
	 * Receive an integer from the user with error checks
	 * for bad input
	 * @param required - 0 if an input can be null, 1 if input cannot be null
	 * @return Value - the integer input by the user
	 * -1 is returned if nothing is entered
	 */
	public static int getInteger(int required) {
		String input = "";
		boolean goodInput = false;
		int value = 0;
		
		while(!goodInput) {
			//Read a line
			input = ScanObject.nextLine();
			try {
				//Nothing is entered
				if(required == 1 && input.compareTo("") == 0) {
					throw new ArithmeticException();
				}
				else if(input.isEmpty()) {
					return -1;
				}
				//Parse the string
				value = Integer.parseInt(input);
				goodInput = true;
			}
			catch(NumberFormatException e) {
				System.out.println("ERROR: Enter only integers");
			}
			catch(ArithmeticException e) {
				System.out.println("ERROR: The number cannot be NULL");
			}
			
		}
		
		return value;
	}
	
	/**
	 * Receive a double from the user with error checks
	 * for bad input
	 * @param required - 0 if an input can be null, 1 if input cannot be null
	 * @return Value - the double input by the user
	 * -1 is returned if nothing is entered
	 */
	public static double getDouble(int required) {
		String input = "";
		boolean goodInput = false;
		double value = 0;
		
		while(!goodInput) {
			//Read a line
			input = ScanObject.nextLine();
			try {
				//Nothing is entered
				if(required == 1 && input.compareTo("") == 0) {
					throw new ArithmeticException();
				}
				else if(input.compareTo("") == 0) {
					return -1;
				}
				//Parse the string
				value = Double.parseDouble(input);
				goodInput = true;
			}
			catch(NumberFormatException e) {
				System.out.println("ERROR: Enter a double number");
			}
			catch(ArithmeticException e) {
				System.out.println("ERROR: A number must be entered");
			}
			
		}
		
		return value;
	}
	
	/**
	 * Receive a float from the user with error checks
	 * for bad input
	 * @return Value - the float input by the user
	 * -1 is returned if nothing is entered
	 */
	public static float getFloat() {
		String input = "";
		boolean goodInput = false;
		float value = 0;
		
		while(!goodInput) {
			//Read a line
			input = ScanObject.nextLine();
			try {
				//Nothing is entered
				if(input.compareTo("") == 0) {
					return -1;
				}
				//Parse the string
				value = Float.parseFloat(input);
				goodInput = true;
			}
			catch(NumberFormatException e) {
				System.out.println("ERROR: Enter a float number");
			}
			
		}
		
		return value;
	}
	
	/**
	 * Receives a date from the user and checks that it passes as correct format
	 * @param force - 1 if the date is required, 0 if the user can enter null
	 * @return String - the date from the user in the correct format
	 */
	public static String getDate(int force) {
		String input = "";
		boolean goodInput = false;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
		
		while(!goodInput) {
			try{
				//Get the line
				input = ScanObject.nextLine();
				//Allow the user to enter nothing
				if(force == 0 && input.compareTo("") == 0) {
					return "";
				}
				else if(force == 1 && input.compareTo("") == 0) {
					throw new NumberFormatException();
				}
				//Check the line for correct format
			    format.parse(input);
			    goodInput = true;
			}catch(ParseException e){
			    System.out.println("ERROR: Incorrect date format (YYYY-MM-DD HH:MM:SS)");
			}
			catch(NumberFormatException e) {
				System.out.println("ERROR: Date is required");
			}
		}
		
		return input;
	}
	
	/**
	 * Read a string from the user that is less than or equal the length
	 * of the charLimit passed in
	 * @param charLimit - length limit for the string to receive
	 * @param required - 0 if an input can be null, 1 if input cannot be null
	 * @return String - the string entered by the user
	 */
	public static String getString(int charLimit, int required) {
		String input = "";
		boolean goodInput = false;
		
		while(!goodInput) {
			//Read a line
			input = ScanObject.nextLine();
			
			//Check the line length
			if(input.length() > charLimit) {
				System.out.println("Enter a string length <= " + charLimit);
			}
			else if(required == 1) {
				if(input.isEmpty()) {
					System.out.println("This value cannot be NULL");
				}
				else
					goodInput = true;
			}
			else
				goodInput = true;
			
		}
		
		return input;
	}
}

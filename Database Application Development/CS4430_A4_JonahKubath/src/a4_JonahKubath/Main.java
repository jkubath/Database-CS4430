package a4_JonahKubath;

import java.sql.*;

/**
 * Main - first piece of code to run in the Database manipulation application
 * @author Jonah Kubath
 *
 */
public class Main {
	
	public static void main(String[] args) throws SQLException {
		//Database connection initialization
		if(!Database.init()) {
			//Database connection failed - end the program
			return;
		};
		
		//Call the main driver of the application
		Driver.mainDriver();
		
		
		//Database.checkKey("employees", "EmployeeID", "1.5");
		//Test query
		//Database.testQuery();

	}

}

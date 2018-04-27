package a4_JonahKubath;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database - This class is used for all database interactions 
 * @author Jonah Kubath
 *
 */
public class Database {
	private static Connection conn = null;
	private static Statement stat = null;
	private static ResultSet result = null;
	
	
	/**
	 * Method to make the connection to the Northwind Database
	 * @return Returns true on successful connection to the database
	 * 	False if the connection failed
	 */
	public static boolean init() {
		String db = "jdbc:mysql://localhost:3306/northwind";
		String username = "root";
		String password = "root";
		
		try {
			//System.out.println("Program Started");
			conn = DriverManager.getConnection(db, username, password);			
			//System.out.println("Connection made");
			
			stat = conn.createStatement();
			//System.out.println("Statement object created");
			
			//System.out.println("makeConnection() finished");
			return true;
			
		}
		catch(SQLException exc) {
			System.out.println("SQL Error on Database Connection");
			//exc.printStackTrace();
			return false;
		}
		
	}
	
	/**
	 * Adds a customer tuple to the customers table, CustomerID is the primary key
	 * and cannot already exist in the table.  CustomerID is the only required
	 * attribute.
	 */
	public static void addCustomer() {
		String CustomerID, CompanyName, ContactName, ContactTitle, Address,
			City, Region, PostalCode, Country, Phone, Fax;
		
		CustomerID = getPrimaryKey("customers", "CustomerID", 5);
		System.out.println("Enter the CompanyName");
		CompanyName = Driver.getString(40, 0);
		System.out.println("Enter the ContactName");
		ContactName = Driver.getString(30, 0);
		System.out.println("Enter the ContactTitle");
		ContactTitle = Driver.getString(30, 0);
		System.out.println("Enter the Address");
		Address = Driver.getString(60, 0);
		System.out.println("Enter the City");
		City = Driver.getString(15, 0);
		System.out.println("Enter the Region");
		Region = Driver.getString(15, 0);
		System.out.println("Enter the PostalCode");
		PostalCode = Driver.getString(10, 0);
		System.out.println("Enter the Country");
		Country = Driver.getString(15, 0);
		System.out.println("Enter the Phone");
		Phone = Driver.getString(24, 0);
		System.out.println("Enter the Fax");
		Fax = Driver.getString(24, 0);
		
		/* Create the INSERT MYSQL string */
		String attributeString = "";
		String valueString = "";
		
		if(!CustomerID.isEmpty()) {
			attributeString += "`CustomerID`";
			valueString += "'" + CustomerID + "'";
		}
		if(!CompanyName.isEmpty()) {
			attributeString += ", `CompanyName`";
			valueString += ", '" + CompanyName + "'";
		}
		if(!ContactName.isEmpty()) {
			attributeString += ", `ContactName`";
			valueString += ", '" + ContactName + "'";
		}
		if(!ContactTitle.isEmpty()) {
			attributeString += ", `ContactTitle`";
			valueString += ", '" + ContactTitle + "'";
		}
		
		if(!Address.isEmpty()) {
			attributeString += ", `Address`";
			valueString += ", '" + Address + "'";
		}
		if(!City.isEmpty()) {
			attributeString += ", `City`";
			valueString += ", '" + City + "'";
		}
		if(!Region.isEmpty()) {
			attributeString += ", `Region`";
			valueString += ", '" + Region + "'";
		}
		if(!PostalCode.isEmpty()) {
			attributeString += ", `PostalCode`";
			valueString += ", '" + PostalCode + "'";
		}
		if(!Country.isEmpty()) {
			attributeString += ", `Country`";
			valueString += ", '" + Country + "'";
		}
		
		if(!Phone.isEmpty()) {
			attributeString += ", `Phone`";
			valueString += ", '" + Phone + "'";
		}
		if(!Fax.isEmpty()) {
			attributeString += ", `Fax`";
			valueString += ", '" + Fax + "'";
		}
		
		String insertString = "INSERT INTO CUSTOMERS (" + 
				attributeString + ") VALUES (" + valueString + ");";
		int returnValue = 0;
		
		//System.out.println("Insert String into CUSTOMERS\n" + insertString);
		
		/* Insert into Customers table */
		try {
			if(conn == null || stat == null) {
				throw new Exception();
			}
			//Perform the insert into the orders table
			returnValue = stat.executeUpdate(insertString, Statement.RETURN_GENERATED_KEYS);
			//Result is the number of rows affected by the insert
			if(returnValue == 0) {
				throw new SQLException();
			}
			
		}
		catch(SQLException e) {
			System.out.println("Error: Insert into customers failed");
		}
		catch(Exception e) {
			System.out.println("Insert Failed: Error with database connection");
		}
	}
	
	/**
	 * Adds an order to the ORDER and ORDER_DETAILS table.
	 * Required values: CustomerID, EmployeeID, OrderDate, ShipVia, ProductID
	 */
	public static void addOrder() {
		//DATETIME is entered as 'YYYY-MM-DD HH:MM:SS' into mysql statement
		
		/*Foreign Keys for ORDERS table
		* Orders.ShipVia and Shippers.ShipperID
		* Orders.EmployeeID and Employees.EmployeeID
		* Orders.CustomerID and Customers.CustomerID
		*/
		
		/*Foreign Keys for ORDER_DETAILS table
		 * order_details.OrderID and Orders.OrderID
		 * order_details.ProductID and Products.ProductID
		 * */
		
		/* Test Case
		 ALFAA
		 1
		 1999-12-20 10:20:20
		 1999-12-20 10:20:20
		 1999-12-20 10:20:20
		 1
		 19.9
		 Hanari Carnes
		 Starenweg 5
		 City
		 Region
		 
		 USA
		 1
		 1
		 1
		 1
		 */
		
		
		int EmployeeID, ShipVia = -1;
		double Freight = -1;
		/* Values needed to insert into ORDERS */
		String CustomerID, ShipName, ShipAddress, ShipCity, ShipRegion;
		String ShipPostalCode, ShipCountry;
		String OrderDate, RequiredDate, ShippedDate;
		
		/* Values needed to insert into ORDER_DETAILS */
		//OrderID is found after inserting into ORDERS
		int ProductID = -1;
		int Quantity = -1;
		double UnitPrice = -1;
		float Discount = -1;
		
		/* Initialize String variables */
		CustomerID = ShipName = ShipAddress = ShipCity = ShipRegion = null;
		ShipPostalCode = ShipCountry = null;
		OrderDate = RequiredDate = ShippedDate = null;
		
		System.out.println("Add an Order");
		
		/* Get the values needed to enter a tuple into the Orders table */
		CustomerID = verifyForeignKey("Customers", "CustomerID", "CustomerID", 0);
		EmployeeID = Integer.parseInt(verifyForeignKey("Employees", "EmployeeID", "EmployeeID", 1));
		System.out.println("Enter the OrderDate - YYYY-MM-DD HH:MM:SS");
		OrderDate = Driver.getDate(1);
		System.out.println("Enter the RequiredDate - YYYY-MM-DD HH:MM:SS");
		RequiredDate = Driver.getDate(0);
		System.out.println("Enter the ShippedDate - YYYY-MM-DD HH:MM:SS");
		ShippedDate = Driver.getDate(0);
		ShipVia = Integer.parseInt(verifyForeignKey("Shippers", "ShipVia", "ShipperID", 1));
		System.out.println("Enter the Freight");
		Freight = Driver.getDouble(0);
		System.out.println("Enter the ShipName");
		ShipName = Driver.getString(40, 0);
		System.out.println("Enter the ShipAddress");
		ShipAddress = Driver.getString(60, 0);
		System.out.println("Enter the ShipCity");
		ShipCity = Driver.getString(15, 0);
		System.out.println("Enter the ShipRegion");
		ShipRegion = Driver.getString(15, 0);
		System.out.println("Enter the ShipPostalCode");
		ShipPostalCode = Driver.getString(10, 0);
		System.out.println("Enter the ShipCountry");
		ShipCountry = Driver.getString(15, 0);
		
		/*Foreign Keys for ORDER_DETAILS table
		 * order_details.OrderID and Orders.OrderID
		 * order_details.ProductID and Products.ProductID
		 * */
		
		/* Set the values to insert into ORDER_DETAILS */
		ProductID = Integer.parseInt(verifyForeignKey("Products", "ProductID", "ProductID", 1));
		System.out.println("Enter the Quantity");
		Quantity = Driver.getInteger(0);
		System.out.println("Enter the UnitPrice");
		UnitPrice = Driver.getDouble(0);
		System.out.println("Enter the Discount");
		Discount = Driver.getFloat();
		
		
		/* Create the INSERT MYSQL string */
		String attributeString = "";
		String valueString = "";
		
		if(!CustomerID.isEmpty()) {
			attributeString += "`CustomerID`";
			valueString += "'" + CustomerID + "'";
		}
		if(EmployeeID != -1) {
			attributeString += ", `EmployeeID`";
			valueString += ", '" + EmployeeID + "'";
		}
		if(!OrderDate.isEmpty()) {
			attributeString += ", `OrderDate`";
			valueString += ", '" + OrderDate + "'";
		}
		else {
			attributeString += ", `OrderDate`";
			valueString += ", '0000-00-00 00:00:00'";
		}
		if(!RequiredDate.isEmpty()) {
			attributeString += ", `RequiredDate`";
			valueString += ", '" + RequiredDate + "'";
		}
		if(!ShippedDate.isEmpty()) {
			attributeString += ", `ShippedDate`";
			valueString += ", '" + ShippedDate + "'";
		}
		if(ShipVia != -1) {
			attributeString += ", `ShipVia`";
			valueString += ", '" + ShipVia + "'";
		}
		if(Freight != -1) {
			attributeString += ", `Freight`";
			valueString += ", '" + Freight + "'";
		}
		if(!ShipName.isEmpty()) {
			attributeString += ", `ShipName`";
			valueString += ", '" + ShipName + "'";
		}
		if(!ShipAddress.isEmpty()) {
			attributeString += ", `ShipAddress`";
			valueString += ", '" + ShipAddress + "'";
		}
		if(!ShipCity.isEmpty()) {
			attributeString += ", `ShipCity`";
			valueString += ", '" + ShipCity + "'";
		}
		if(!ShipRegion.isEmpty()) {
			attributeString += ", `ShipRegion`";
			valueString += ", '" + ShipRegion + "'";
		}
		if(!ShipPostalCode.isEmpty()) {
			attributeString += ", `ShipPostalCode`";
			valueString += ", '" + ShipPostalCode + "'";
		}
		if(!ShipCountry.isEmpty()) {
			attributeString += ", `ShipCountry`";
			valueString += ", '" + ShipCountry + "'";
		}
		
		String insertString = "INSERT INTO ORDERS (" + 
				attributeString + ") VALUES (" + valueString + ");";
		
		//System.out.printf("The Insert String\n" + "%s\n", insertString);
		
		/* The order should be rejected if the product is discontinued */
		try {
			//Select the ProductID and Discontinued fields
			result = stat.executeQuery("SELECT `ProductID`, `Discontinued` FROM `products` WHERE `ProductID` = " + ProductID + ";");
			if(result.next()) {
				//Check if the ProductID is discontinued
				if(result.getString("Discontinued").equals("y")) {
					System.out.println("ERROR: The Product in the order has been discontinued");
					System.out.println("The order was not processed");
					return;
				}
			}
		}
		catch(SQLException e) {
			System.out.println("ERROR: SQL Query for ProductID from the products table failed");
		}
		
		/* returnValue - Holds the returnValue from the INSERT
		 * 0 - zero rows were affected = insert failed
		 * 1 - one row was added to the database = insert successful
		*/
		int returnValue = 0;
		int OrderID = -1;
		/* Insert into orders table */
		try {
			if(conn == null || stat == null) {
				throw new Exception();
			}
			//Perform the insert into the orders table
			returnValue = stat.executeUpdate(insertString, Statement.RETURN_GENERATED_KEYS);
			//Result is the number of rows affected by the insert
			if(returnValue == 0) {
				throw new SQLException();
			}
			
		}
		catch(SQLException e) {
			System.out.println("Error: Insert into orders failed");
		}
		catch(Exception e) {
			System.out.println("Insert Failed: Error with database connection");
		}
		
		/* Get the created OrderID */
		try {
			result = stat.getGeneratedKeys();
			OrderID = -1;
			if(result.next()) {;
				OrderID = result.getInt(1);
				//System.out.println("OrderID - " + OrderID);
			}
		}
		catch(SQLException e) {
			System.out.println("ERROR: Failed to retrieve the OrderID");
		}
		
		/* Create the insert string for order_details */
		String detailsAttribute = "";
		String detailsValues = "";
		String order_detailsSQL = "";
		
		if(OrderID != -1) {
			detailsAttribute += "`OrderID`";
			detailsValues += "'" + OrderID + "'";
		}
		if(ProductID != -1) {
			detailsAttribute += ", `ProductID`";
			detailsValues += ", '" + ProductID + "'";
		}
		if(Quantity != -1) {
			detailsAttribute += ", `Quantity`";
			detailsValues += ", '" + Quantity + "'";
		}
		if(UnitPrice != -1) {
			detailsAttribute += ", `UnitPrice`";
			detailsValues += ", '" + UnitPrice + "'";
		}
		if(Discount != -1) {
			detailsAttribute += ", `Discount`";
			detailsValues += ", '" + Discount + "'";
		}
		
		order_detailsSQL = "INSERT INTO order_details (" + detailsAttribute
				+ ") VALUES (" + detailsValues + ");";
		
		/* Insert into order_details table */
		try {
			//System.out.println("The Insert String\n" + order_detailsSQL);
			/* Perform the insert into order_details */
			returnValue = stat.executeUpdate(order_detailsSQL);
			//Result is the number of rows affected by the insert
			if(returnValue == 0) {
				throw new SQLException();
			}
		}
		catch(SQLException e) {
			System.out.println("ERROR: Insert into order_details failed");
		}
		
		/* Increase the quantity of the products on order in the Products table 
		 * ProductID is currently an int type, "" + ProductID makes a string
		 */
		increaseOnOrder("Products", "UnitsOnOrder", "ProductID", "" + ProductID, Quantity);
		
		System.out.println("Order created - " + OrderID);
	}
	
	/**
	 * Removes the Order specified by the user.  This also removes the products from the Order_details
	 * table and updates the UnitsOnOrder from the products table with the removed products quantity
	 * Required values: OrderID
	 */
	public static void deleteOrder() {
		int OrderID = -1;
		boolean goodOrderID = false;
		System.out.println("Enter the OrderID you would like to delete");
		
		/* Get the OrderID from the user, if the OrderID doesn't exist ask again */
		while(!goodOrderID) {
			OrderID = Driver.getInteger(1);
			
			//The user wants to quit
			if(OrderID == -1) {
				return;
			}
			else if(!checkKey("orders", "OrderID", OrderID + "")) {
				System.out.println("ERROR: The OrderID entered does not exist");
				System.out.println("Re-enter the OrderID or -1 to quit");
			}
			else 
				goodOrderID = true;
		}
		
		int Quantity = -1;
		int ProductID = -1;
		/* Update the Number of ItemsOnOrder */
		try {
			result = stat.executeQuery("SELECT * FROM order_details WHERE `OrderID` = " + OrderID + ";");
			
			try {
				while(result.next()) {
					//System.out.println("In result Set");
					//System.out.println("ProductID - " + result.getString("ProductID"))
					try {
						ProductID = Integer.parseInt(result.getString("ProductID"));
						Quantity = result.getInt("Quantity");
						
						//System.out.println(ProductID + " - " + Quantity);
						//UPDATE `products` SET `UnitsOnOrder`= `UnitsOnOrder` - 1 WHERE `ProductID` = 1;
						String updateString = "UPDATE products SET `UnitsOnOrder` = `UnitsOnOrder` - " +
								Quantity + " WHERE `ProductID` = " + ProductID + ";";
						//System.out.println("Update string - " + updateString);
						
						//UPDATE `products` SET `Quantity` = `Quantity` - 1 WHERE `ProductID` = 1;
						//Update all the ProductIDs in the given Order
						stat.executeUpdate(updateString);
						//System.out.println("Done executing the update string");
					}
					catch(SQLException e) {
						System.out.println("ERROR: inside of while loop");
					}
				}
			}
			catch(SQLException e) {
				//System.out.println("Restult.next() error");
			}
			
		}
		catch(SQLException e) {
			System.out.println("ERROR: Updating ItemsOnOrder from products table failed.");
		}
		
		/* Delete from the order_details table */
		try {
			stat.executeUpdate("DELETE FROM order_details WHERE `OrderID` = " 
					+ OrderID + ";");
			
		}
		catch(SQLException e) {
			System.out.println("ERROR: Delete from order_details failed");
		}
		
		/* Delete from the Orders table */
		try {
			stat.executeUpdate("DELETE FROM orders WHERE `OrderID` = " 
					+ OrderID + ";");
			
		}
		catch(SQLException e) {
			System.out.println("ERROR: Delete from orders failed");
		}
		
	}
	
	/**
	 * Receives the input from the user for the OrderID and sets the ShippedDate to
	 * the current time.
	 * 
	 * Ship Order: Fill in the Orders table with shipping info, ShippedDate, 
	 * ShipVia, Freight, and check ShipName,ShipAddress, 
	 * etc to make sure they are not NULL.
	 */
	public static void shipOrder() {
		//Get the OrderID from the user
		int OrderID = getOrderID();
		double Freight = -1;
		String ShippedDate;
		String ShipName, ShipAddress, ShipCity, 
			ShipRegion, ShipPostalCode, ShipCountry;
		
		if(OrderID == -1) {
			return;
		}
		
		System.out.println("Enter the ShippedDate (YYYY-MM-DD hh:mm:ss)");
		ShippedDate = Driver.getDate(1);
		
		System.out.println("Enter the Freight");
		Freight = Driver.getDouble(0);
		
		//Make sure the ShipName is not null
		ShipName = selectValue("orders", "ShipName", "OrderID", OrderID + "");
		if(ShipName == null) {
			System.out.println("Enter the Receiver's Name");
			ShipName = Driver.getString(40, 1);
		}
		
		//Make sure the ShipAddress is not null
		ShipAddress = selectValue("orders", "ShipAddress", "OrderID", OrderID + "");
		if(ShipAddress == null) {
			System.out.println("Enter the ShipAddress");
			ShipAddress = Driver.getString(60, 1);
		}
		
		//Make sure the ShipCity is not null
		ShipCity = selectValue("orders", "ShipCity", "OrderID", OrderID + "");
		if(ShipCity == null) {
			System.out.println("Enter the ShipCity");
			ShipCity = Driver.getString(15, 1);
		}
		
		//Make sure the ShipRegion is not null
		ShipRegion = selectValue("orders", "ShipRegion", "OrderID", OrderID + "");
		if(ShipRegion == null) {
			System.out.println("Enter the ShipRegion");
			ShipRegion = Driver.getString(15, 1);
		}
		
		//Make sure the ShipPostalCode is not null
		ShipPostalCode = selectValue("orders", "ShipPostalCode", "OrderID", OrderID + "");
		if(ShipPostalCode == null) {
			System.out.println("Enter the ShipPostalCode");
			ShipPostalCode = Driver.getString(10, 1);
		}
		
		//Make sure the ShipPostalCode is not null
		ShipCountry = selectValue("orders", "ShipCountry", "OrderID", OrderID + "");
		if(ShipCountry == null) {
			System.out.println("Enter the ShipCountry");
			ShipCountry = Driver.getString(15, 1);
		}
		
		String sql = "UPDATE `orders` SET `ShippedDate`=\"" + ShippedDate 
				+ "\",`Freight`=\"" + Freight +
				"\",`ShipName`=\"" + ShipName + "\",`ShipAddress`=\"" + ShipAddress + 
				"\",`ShipCity`=\"" + ShipCity + "\",`ShipRegion`=\"" + ShipRegion +
				"\",`ShipPostalCode`=\"" + ShipPostalCode 
				+ "\",`ShipCountry`=\"" + ShipCountry + "\" WHERE OrderID =\"" + OrderID + "\";";
		//System.out.println(sql);
		
		//Update the ShippedDate value in the orders table with the current time
		try {
			stat.executeUpdate(sql);
			//Print to the user that ship date was set
			System.out.println(OrderID + " ship date set to - " + ShippedDate);
		}
		catch(SQLException e) {
			System.out.println("ERROR: Update to ShippedDate failed");
		}
	}
	
	/**
	 * Print all the orders with a NULL "ShippedDate"
	 */
	public static void printOrderDetails() {
		//Select all orders with a null shipped date
		try {
			String query = "SELECT * FROM orders WHERE ShippedDate IS NULL ORDER"
					+ " BY OrderDate";
			//System.out.println("Order Query: " + query);
			result = stat.executeQuery(query);
		}
		catch(SQLException e) {
			System.out.println("ERROR: Query for pending orders has failed");
		}
		
		/* Print the order information */
		String resultString = "";
		try {
			System.out.println("Pending Orders sorted by Order Date");
			System.out.println("OrderID   CustomerID\tOrderDate");
			
			while(result.next()) {
				//String formatting
				resultString = String.format("%-7s - %-10s\t%s", result.getString("OrderID"), 
						result.getString("CustomerID"), result.getDate("OrderDate"));
				System.out.println(resultString);
			}
		}
		catch(SQLException e) {
			System.out.println("ERROR: Printing results failed");
		}
		
		
		
	}
	
	/**
	 * The user enters the ProductID.  If this exists in the products table, then
	 * the user is prompted to enter the number of products to add.  The number of
	 * UnitsOnOrder is subtracted from before the UnitsInStock are added.  If the
	 * ProductID does not exists, the user is prompted to enter the needed info
	 * to add the product to the products table.
	 */
	public static void restockParts() {
		int SupplierID, ProductID = -1;
		int CategoryID, UnitsInStock, 
			UnitsOnOrder, ReorderLevel = -1;
		String ProductName, QuantityPerUnit;
		double UnitPrice = -1;
		char Discontinued = 'n';
		
		/* 
		 * Products categoryid = Category.CategoryID
		 * Products supplierid = Suppliers.SupplierID
		 * Products product name
		 *
		 * */
		
		System.out.println("Enter the ProductID");
		ProductID = Driver.getInteger(1);
		
		//Check if the ProductID exists already
		if(checkKey("products", "ProductID", ProductID + "")) {
			/* Update UnitsInStock, UnitsOnOrder,ReorderLevel */
			System.out.println("Enter the Units to Add");
			UnitsInStock = Driver.getInteger(1);
//			UnitsOnOrder = Integer.parseInt(selectValue("products", "UnitsOnOrder", "ProductID", ProductID + ""));
//			
//			if(UnitsOnOrder > UnitsInStock) {
//				UnitsOnOrder -= UnitsInStock;
//				UnitsInStock = 0;
//			}
//			else if(UnitsInStock > UnitsOnOrder) {
//				UnitsInStock -= UnitsOnOrder;
//				UnitsOnOrder = 0;
//			}
//			else {
//				UnitsInStock = 0;
//				UnitsOnOrder = 0;
//			}
			
			System.out.println("Enter the Reorder level");
			ReorderLevel = Driver.getInteger(1);
			
			String sql = "UPDATE `products` SET `UnitsInStock`=" + 
					UnitsInStock + ", `ReorderLevel`=" + 
					ReorderLevel + " WHERE ProductID = " + ProductID + ";";
			
			try {
				stat.executeUpdate(sql);
				System.out.println("ProductID " + ProductID + " restocked to " 
						+ UnitsInStock);
				
			}
			catch(SQLException e) {
				System.out.println(sql);
				System.out.println("ERROR: SQL update on products failed");
			}
			
			
		}
		else {
			/* 
			 * Products categoryid = Category.CategoryID
			 * Products supplierid = Suppliers.SupplierID
			 * Products product name
			 *
			 * */
			/* Add the product to the products table */
			System.out.println(ProductID + "does not currently exist.  "
					+ "Enter the new product information");
			System.out.println("Enter the Product Name");
			ProductName = Driver.getString(40, 1);
			SupplierID = Integer.parseInt(verifyForeignKey("suppliers", 
					"SupplierID", "SupplierID", 1));
			CategoryID = Short.parseShort(verifyForeignKey("categories", 
					"CategoryID", "CategoryID", 1));
			System.out.println("Enter the Units to Add");
			UnitsInStock = Driver.getInteger(1);
			System.out.println("Enter the ReorderLevel");
			ReorderLevel = Driver.getInteger(0);
			System.out.println("Enter the UnitPrice");
			UnitPrice = Driver.getDouble(1);
			System.out.println("Enter the QuantityPerUnit");
			QuantityPerUnit = Driver.getString(20, 0);
			
			/* These must be set like this as it is a new product */
			UnitsOnOrder = 0;
			Discontinued = 'n';
			
			String sql = "INSERT INTO `products`(`ProductName`, `SupplierID`, "
					+ "`CategoryID`, `QuantityPerUnit`, `UnitPrice`," + 
					"`UnitsInStock`, `UnitsOnOrder`, `ReorderLevel`, `Discontinued`)" +
					" VALUES (\"" + ProductName + "\", " + SupplierID + ", " +
					CategoryID + ", " + QuantityPerUnit + ", " + 
					UnitPrice + ", " + UnitsInStock + ", " + UnitsOnOrder + ", " +
					ReorderLevel + ", '" + Discontinued + "');";	
			
			try {
				stat.executeUpdate(sql);
				System.out.println(ProductName + " added to products");
			}
			catch(SQLException e) {
				System.out.println(sql);
				System.out.println("ERROR: Insert into products table failed");
			}
		}
		
		
	}
	/**
	 * This method takes in the table, attribute and checks if that value is in
	 * in the table.  This is used to verify foreign keys.
	 * @param table The table to search for the attribute in
	 * @param attribute The attribute of the table to check
	 * @param value The value to check for
	 * @return 1 for found, 0 for not found
	 */
	public static boolean checkKey(String table, String attribute, String value) {
		//Build the query with the given inputs
		String query = "SELECT * FROM ";
		query += table + " WHERE " + attribute + "=" + "\"" + value + "\";";
		
		try {
			//Execute the query
			result = stat.executeQuery(query);
			//If the result points to a value, then the query found something
			//If result.next() is false - nothing was found
			if(result.next()) {
				return true;
			}
			else {
				return false;
			}
		}
		catch(SQLException e) {
			System.out.println("Query failed");
		}
		
		
		return false;
	}
	
	/**
	 * Verifies that a foreign key constraint is met before returning the value
	 * @param table The other table to check for the foreign key
	 * @param currentAttribute The attribute we are trying to enter into the database
	 * @param foreignAttribute The attribute to check for
	 * @param returnType - 0 for String, 1 for Integer, 2 for Double
	 * @return String - the value entered by the user 
	 * 	that passes the foreign key constraint
	 */
	public static String verifyForeignKey(String table, String currentAttribute,
			String foreignAttribute, int returnType) {
		boolean foreignKey = false;
		String returnString = "";
		
		while(!foreignKey) {
			System.out.println("Enter the " + currentAttribute);
			/* varchar of length 5, and 1 means the value cannot be null */
			returnString = Driver.getString(5, 1);
			
			/* Verify the attribute exists in the other table */
			if(checkKey(table, foreignAttribute, returnString)) {
				foreignKey = true;
			}
			else {
				System.out.println("ERROR: Foreign Key failed for " + foreignAttribute + " in " + table);
			}
			
			/* Should the returnString be checked for being an Integer */
			if(foreignKey && returnType == 1) {
				try {
					Integer.parseInt(returnString);
				}
				catch (NumberFormatException e) {
					System.out.println("ERROR: An integer must be entered");
					foreignKey = false;
				}
			}
			/* Should the returnString be checked for being an Double */
			else if(foreignKey && returnType == 2) {
				try {
					Double.parseDouble(returnString);
				}
				catch (NumberFormatException e) {
					System.out.println("ERROR: A Double must be entered");
					foreignKey = false;
				}
			}
			/* Should the returnString be checked for being an Float */
			else if(foreignKey && returnType == 3) {
				try {
					Float.parseFloat(returnString);
				}
				catch (NumberFormatException e) {
					System.out.println("ERROR: A Float must be entered");
					foreignKey = false;
				}
			}
		}
		
		return returnString;
	}
	
	/**
	 * Receives the primary key from the user (value cannot be null and cannot already exist in the table)
	 * @param table The table to search for existing primary keys
	 * @param attribute The attribute of the primary key
	 * @param charLimit The limit of the string
	 * @return String - return the string entered by the user after passing primary key rules
	 */
	public static String getPrimaryKey(String table,String attribute, int charLimit) {
		String input = "";
		boolean goodInput = false;
		
		while(!goodInput) {
			System.out.println("Enter the " + attribute);
			input = Driver.getString(charLimit, 1);
			
			//System.out.println(input);
			
			if(checkKey(table, attribute, input)) {
				System.out.println("ERROR: " + attribute + " already exists.");
			}
			else
				goodInput = true;
			
		
		}
		
		return input;
	}
	
	/**
	 * Updates the attribute in updateAttribute field with the increment integer.  The keyAttribute, key is used to select the tuple
	 * @param table The table to update in
	 * @param updateAttribute The attribute / field to update
	 * @param keyAttribute The Primary Key field for the table
	 * @param key The Primary key we are searching for to distinguish the tuple
	 * @param increment The amount to increment the field
	 * @return Returns 1 on failure and 0 on success
	 */
	public static int increaseOnOrder(String table, String updateAttribute, 
			String keyAttribute, String key, int increment) {
		int valuesUpdated = 0;
		
		//Error check for bad increment value
		if(increment < 0) {
			increment = 0;
		}
		
		String sqlStatement = "UPDATE " + table + " SET `" + updateAttribute 
				+ "` = (" + updateAttribute + "+" + increment + ") WHERE " 
				+ keyAttribute + "=" + key + ";";
		
		//System.out.println("Update String\n" + sqlStatement);
		
		try {
			//Execute the query
			valuesUpdated = stat.executeUpdate(sqlStatement);
		
			if(valuesUpdated == 0) {
				throw new SQLException();
			}
		}
		catch(SQLException e) {
			System.out.println("ERROR: Update failed on " + table);
			return 1;
		}
		
		return 0;
		
	}

	/**
	 * Receives the OrderID from the user.  Doesn't allow negative numbers.
	 * Also checks that the OrderID exists in the orders table and that 
	 * the ShippedDate value is null (meaning the order hasn't shipped).
	 * @return Integer - the value of the OrderID to mark as shipped
	 */
	public static int getOrderID() {
		boolean goodInput = false;
		System.out.println("Enter the OrderID you would like to ship");
		int OrderID = -2;
		while(!goodInput) {
			try {
				OrderID = Driver.getInteger(1);
				
				if(OrderID == -1) {
					return -1;
				}
				
				//Don't allow negative OrderIDs
				if(OrderID < 0) {
					throw new ArithmeticException();
				}
				
				//Verify the OrderID exists
				if(checkKey("orders", "OrderID", OrderID + "")) {
					goodInput = true;
				}
				else {
					throw new Exception();
				}
				
				if(!nullShippedDate(OrderID)) {
					System.out.println("ERROR: The OrderID you entered has already been shipped");
					System.out.println("Re-enter the OrderID or -1 to quit");
					goodInput = false;
				}
				
				
				}
			catch(ArithmeticException e) {
				System.out.println("ERROR: OrderID cannot be negative");
				System.out.println("Re-enter the OrderID or -1 to quit");
			}
			catch(Exception e) {
				System.out.println("ERROR: OrderID does not exist");
				System.out.println("Re-enter the OrderID or -1 to quit");
			}
		}
		
		
		return OrderID;
	}
	
	/**
	 * Verifies that the givne OrderID has a null ShippedDate 
	 * value in the orders table
	 * @param OrderID The OrderID to check for
	 * @return True if the ShippedDate is null, false otherwise
	 */
	public static boolean nullShippedDate(int OrderID) {
		//Select all orders with a null shipped date
		try {
			String query = "SELECT * FROM orders WHERE ShippedDate IS NULL"
					+ " AND OrderID = " + OrderID + " ORDER BY OrderDate";
			//System.out.println("Order Query: " + query);
			result = stat.executeQuery(query);
		}
		catch(SQLException e) {
			System.out.println("ERROR: Query for pending orders has failed");
		}
		
		//If the result has values then the shipped date is null - return true
		try {
			if(result.next()) {
				return true;
			}
			else {
				return false;
			}
		}
		catch(SQLException e) {
			System.out.println("ERROR: SQL result failed");
		}
		
		return false;
	}
	
	/**
	 * Perform the SQL select for a specific attribute.  Return null if the
	 * value is empty.
	 * SELECT attribute FROM table WHERE key = keyValue
	 * @param table The table to search in
	 * @param attribute The attribute to return
	 * @param key The key attribute   
	 * @param keyValue The key value
	 * @return The value of the attribute
	 */
	public static String selectValue(String table, String attribute, 
			String key, String keyValue) {
		String returnValue = null;
		
		String sql = "SELECT `" + attribute + "` FROM " + table
				+ " WHERE " + key + " = \"" + keyValue + "\";";
		
		try {
			result = stat.executeQuery(sql);
		}
		catch(SQLException e) {
			System.out.println(sql);
			System.out.println("ERROR: Query failed");
		}
		
		try {
			if(result.next())
				returnValue = result.getString(attribute);
		}
		catch(SQLException e) {
			System.out.println(sql);
			System.out.println("ERROR: Result on query failed");
		}
		
		//Return null if no value is entered, null and "" are caught
		if(returnValue.isEmpty()) {
			return null;
		}
		
		return returnValue;
	}
	/**
	 * Test method - sends a test query to print all data in the Shippers table
	 */
	public static void testQuery() {
		try {
			if(conn == null || stat == null) {
				throw new Exception();
			}
			result = stat.executeQuery("select * from shippers");
			
			//System.out.println("Query Finished");
			while(result.next()) {
				System.out.println(result.getString("ShipperID") + " - " + result.getString("CompanyName"));
			}
		}
		catch(SQLException e) {
			System.out.println("Error on query");
			//e.printStackTrace();
		}
		catch(Exception e) {
			System.out.println("Query Failed: Error with database connection");
		}
	}
	
	/**
	 * Getter for the Connection object
	 * @return Connection
	 */
	public static Connection getConnection() {
		return conn;
	}
	
	
	
	
	
}

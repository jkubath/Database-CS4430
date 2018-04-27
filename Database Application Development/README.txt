README.txt

Class: CS4430 - Northwind Assignment 4
Author: Jonah Kubath
Email: jnn3350@wmich.edu
Date: 04-01-2018

Requirements:
	Java Run Environment
	MYSQL server installed and running


Compilation:
	A make file is included in the current directory.  Make sure that the terminal current directory has the makefile.

	~$ make
		This will call the java compiler to compile the .java files in the src directory to .class files in the bin directory

	~$ make use
		This will use the java executable to run the A4 assignment.


NOTES:
	The assignment used a vague term when it says that most of the fields are required.  It was hard for me 
	to decide then which database attributes counted as "most".  My requirements for the required fields were this:
	I required only the attributes that would return an error if the attribute was null.  
	This means that the required fields are primary keys and foreign keys.  My implementation can easily
	be changed if this was mis-interpreted from the assignment as my methods have a switch which turns on / off whether or not a 
	field can null or not.

	Also, I assumed that the person running this program has their MYSQL setup to run on port 3306.  If this is not the case
	you will get a message "SQL Error on Database Connection".  The port number can be changed in the Database.java file.
	Re-compile and the program should work.
	




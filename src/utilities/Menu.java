package utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import database.StockDatabase;
//import java.sql.Statement;

public class Menu {
	private String user = null;
	Scanner scan = null;
	public Menu() {
		scan = new Scanner(System.in);
	}
	
	public int LoginMenu(StockDatabase database) {
		String username = null;
		String password = null;
		String databaseName = null;
		do {
			clearScreen();
			System.out.println("Login Menu");
			
			System.out.print("Insert Database Name \u001B[31m[Type \"0\" to exit]\u001B[0m: ");
			databaseName = scan.nextLine();
			if(databaseName.equals("0")) return -1;
			
			System.out.print("Insert Username \u001B[31m[Type \"0\" to exit]\u001B[0m: ");
			username = scan.nextLine();
			if(username.equals("0")) return -1;
			
			System.out.print("Insert Password: ");
			password = scan.nextLine();
			
			database.connect(databaseName, username, password);
			System.out.print("Press [Enter] to continue...");
			scan.nextLine();
		} while(!database.getIsConnected());	
		user = username;
		return 1;
	}
	
	public int mainMenu() {
		int choice = 0;
		clearScreen();
		System.out.println(user + " Main Menu");
		System.out.println("1. Export Data for Stock");
		System.out.println("2. Insert Data to Database");
		System.out.println("3. Logout");
		do {
			System.out.print(">> ");
			try {
				choice = scan.nextInt();
			} catch (Exception e) {
				System.err.println("Invalid");
			}
			scan.nextLine();
		} while(choice < 1 || choice > 3);
		return choice;
	}

	public void menuExport(StockDatabase stockDB) {
		String stockName = null;
		ResultSet result = null;
		String searchQuery = "SELECT * FROM Stocks WHERE Code = ? ORDER BY Date";
		PreparedStatement statement = null;
		try {
			statement = stockDB.getConnectionDB().prepareStatement(searchQuery);
			
			// Continue looping until the entered Stock Name is found in the database.
			do {
				System.out.print("Stock name \u001B[31m[Type \"0\" to exit]\u001B[0m: ");
				stockName = scan.nextLine();
				if(stockName.equals("0")) return;
				if(stockName.length() != 4){
					System.out.println("Invalid Input");
					continue;
				}
				System.out.println("Testing Input");
				statement.setString(1, stockName);
				result = statement.executeQuery();
			} while(result == null || !result.next());
			
			try {
				stockDB.export(result, stockName);
			} catch (IOException e) {
				System.err.println(e);
			}
		} catch (SQLException e) {
			System.err.println(e);
		}
		
		/*  This code has the potential to introduce SQL Injection vulnerability at line 100.
			try {
				Statement statement = (Statement) stockDB.getConnectionDB().createStatement();
				do {
					System.out.print("Stock name \u001B[31m[Type \"0\" to exit]\u001B[0m: ");
					stockName = scan.nextLine();
					// If user want to exit
					if(stockName.equals("0")) return;
					result = statement.executeQuery("SELECT * FROM Stocks WHERE Code = '"+ stockName + "'");
				} while(!result.next());
			
				try {
					stockDB.export(result, stockName);
				} catch (IOException e) {
					System.err.println(e);
				}
			} catch (SQLException e) {
				System.err.println(e);
			}
		*/
		System.out.print("Press [Enter] to continue...");
		scan.nextLine();
	}
	
	public void menuInsert(StockDatabase stockDB) {
    	// Folder Data
		File file = new File("Data/");
    	
		// List of file and folder inside "Data" folder
    	String listFile[] = file.list();
    	
		int numFile = listFile.length;
		if(numFile == 0){
			System.out.println("No File in folder");
			return;
		}

    	// Print list of file and folder in "Data" folder
    	System.out.println("List in Data Folder:");
    	for(int i = 0; i < numFile; i++) {
    		System.out.printf("%d. %s\n", (i + 1), listFile[i]);
    	}
    	
    	// File name
		int fileChoice = 0;
    	File fileInsert = null;
    	do{
			do{
				System.out.printf("Input file [1 - %d] to be inserted \u001B[31m[Type \"0\" to exit]\u001B[0m: ", numFile);
				try {
					fileChoice = scan.nextInt();
				} catch (Exception e) {
					System.err.println(e);
				}
				scan.nextLine();
			} while(fileChoice < 1 || fileChoice > numFile);

    		if(fileChoice == 0) return;    		
    		fileInsert = new File("Data/" + listFile[fileChoice - 1]);
    	} while(!fileInsert.exists() && !fileInsert.isFile());
    	
    	try {
			stockDB.insertFile(fileInsert);
		} catch (FileNotFoundException | SQLException e) {
			System.err.println(e);
		} 
    	
		System.out.print("Press [Enter] to continue...");
		scan.nextLine();
	}
	
	public void clearScreen() {
		for(int i = 0; i < 25; i++) {
			System.out.println("");
		}
	}
}
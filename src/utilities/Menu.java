package utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.InputMismatchException;
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
				choice = Integer.parseInt(scan.nextLine());
			} catch (InputMismatchException e) {
				System.err.println("Invalid");
			}
		} while(choice < 1 || choice > 3);
		return choice;
	}

	public void menuExport(StockDatabase stockDB) {
		String stockName = null;
		ResultSet result = null;
		String searchQuery = null;
		PreparedStatement statement = null;
		clearScreen();
		searchQuery = exportRange();
		System.out.println("Query: "+ searchQuery);
		try {
			// Continue looping until the entered Stock Name is found in the database.
			boolean valid = true;
			do {
				valid = true;
				System.out.print("Stock name \u001B[31m[Type \"0\" to exit]\u001B[0m: ");
				stockName = scan.nextLine();
				if(stockName.equals("0")) return;

				// Indonesia stock code consists of 4 letter
				if(stockName.length() != 4){
					System.out.println("Stock name must be 4 letter");
					valid = false;
				} else{	
					statement = stockDB.getConnectionDB().prepareStatement(searchQuery);
					statement.setString(1, stockName);
					result = statement.executeQuery();
					if(!result.next()){
						System.out.printf("Stock %s is not available in Database\n", stockName);
						valid = false;
					}
				}
			} while(!valid);
			
			try {
				stockDB.export(result, stockName);
			} catch (IOException  | SQLException e) {
				System.err.println(e);
			}
			result.close();
			statement.close();
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
	
	private String exportRange(){
		int choice = 0;
		System.out.println("Export Date Range");
		System.out.println("1. All Date");
		System.out.println("2. Last Six Month");
		System.out.println("3. Custom Range");
		do{
			System.out.print(">> ");
			try {
				choice = Integer.parseInt(scan.nextLine());
			} catch (InputMismatchException e) {
				System.err.println(e);
			}
		} while(choice < 1 || choice > 3);
		
		String query = null;
		switch (choice) {
			case 1:
				query = "SELECT * FROM Stocks WHERE `Code` = ? ORDER BY `Date`";
				break;
				
			case 2:
				query = "SELECT * FROM (SELECT * FROM Stocks WHERE `Code` = ? ORDER BY `Date` DESC LIMIT 6) AS Output ORDER BY `Date` ASC";
				break;
			
			case 3:
				String startDateString = null;
				String endDateString = null;
				LocalDate endDate = null;
				LocalDate startDate = null;
				boolean valid = false;

				do {
					do{
						valid = true;
						System.out.print("Input Start Date [YYYY-MM]: ");
						startDateString = scan.nextLine();
						if(!startDateString.matches("\\d{4}-\\d{2}")){
							valid = false;
							System.out.println("Inccorect Input Format");
						} else{
							try {
								startDateString += "-01";
								startDate = LocalDate.parse(startDateString);
							} catch (Exception e) {
								System.out.println("Invalid Month");
								valid = false;
							}
						}
					} while(!valid);
					
					do{
						valid = true;
						System.out.print("Input End Date [YYYY-MM]: ");
						endDateString = scan.nextLine();
						if(!endDateString.matches("\\d{4}-\\d{2}")){
							valid = false;
							System.out.println("Inccorect Input Format");
						} else{
							try {
								endDateString += "-01";
								endDate = LocalDate.parse(endDateString);
							} catch (Exception e) {
								System.out.println("Invalid Month");
								valid = false;
							}
						}
					} while(!valid);
	
					if(startDate.isAfter(endDate)){
						valid = false;
						System.out.println("Invalid: Start date is after end date");
					}
				} while (!valid);
				
				endDate = endDate.plusMonths(1);
				endDateString = endDate.toString();

				// This code is safe from SQL injection because we have checked the input pattern in the loop
				query = "SELECT * FROM Stocks WHERE `Code` = ? AND `Date` >= '" + startDateString + "' AND `Date` < '" + endDateString + "' ORDER BY `Date`";
				break;
		}
		return query;
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

		clearScreen();

    	// Print list of file and folder in "Data" folder
    	System.out.println("List in Data Folder:");
    	for(int i = 0; i < numFile; i++) {
    		System.out.printf("%d. %s\n", (i + 1), listFile[i]);
    	}
    	
    	// File name
		int fileChoice = -1;
    	File fileInsert = null;
    	do{
			do{
				System.out.printf("Input file [1 - %d] to be inserted \u001B[31m[Type \"0\" to exit]\u001B[0m: ", numFile);
				try {
					fileChoice = Integer.parseInt(scan.nextLine());
				} catch (InputMismatchException e) {
					System.err.println(e);
				}
			} while(fileChoice < 0 || fileChoice > numFile);
			
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
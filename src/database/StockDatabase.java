package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class StockDatabase {
	private String listColumn[] = {
		"Date", "Code", "Local IS", "Local CP", "Local PF", "Local IB", "Local ID", 
		"Local MF", "Local SC", "Local FD", "Local OT", "Foreign IS", "Foreign CP", 
		"Foreign PF", "Foreign IB", "Foreign ID", "Foreign MF", "Foreign SC", "Foreign FD", "Foreign OT"
	};
	private Connection connection = null;
	private boolean isConnected = false;
	
	public StockDatabase() {
		try {
			// load class "com.mysql.cj.jdbc.Driver"
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Success load driver");
		} catch (Exception e) {
			System.err.println("Invalid Load Driver");
		}	
	}
	
	public boolean getIsConnected() {
		return isConnected;
	}
	
	public Connection getConnectionDB() {
		return connection;
	}
	
	public void connect(String databaseName, String username, String password) {
		// Format url = "jdbc:mysql://host:port/databaseName
		String url = "jdbc:mysql://localhost:3306/" + databaseName;
		try {
			connection = DriverManager.getConnection(url, username, password);
			isConnected = true;
			System.out.println("\u001B[32mConnected to Database\u001B[0m");
		} catch (SQLException e) {
			System.err.println("Failed to Connect");
		}
	}
	
	public void closeDatabase() {
		try {
			connection.close();
			connection = null;
			isConnected = false;
		} catch (SQLException e) {
			System.err.println(e);
		}
	}
	
	public void export(ResultSet result, String stockName) throws SQLException, IOException {
		// File stockName.csv will be exported to "Output" folder
		File outputFile = new File("Output/" + stockName + ".csv");
		FileWriter writer = new FileWriter(outputFile);
		int size = listColumn.length;
		
		// Column Header
		for(int i = 0; i < size; i++) {
			if(i != size - 1) {
				writer.write(listColumn[i] + ",");
			} else {
				writer.write(listColumn[i] + "\n");
			}
		}
		
		Date date = null;
		do {
			date = result.getDate(1);
			writer.write(date.toString() + "," + stockName);
			for(int i = 3; i <= size; i++) {
				writer.write("," + Long.toString(result.getLong(i)));
			}
			writer.write("\n");
		} while(result.next());

		writer.close();
		result.close();
		System.out.printf("File %s.csv has been exported\n", stockName);
	}
	
   public void insertFile(File fileInsert) throws FileNotFoundException, SQLException {
		String insertQuery = "INSERT INTO Stocks VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		// Scanner to scan the file
		Scanner fileScanner = new Scanner(new File("Data/" + fileInsert.getName()));
    	
		// Make Prepared Statement
		PreparedStatement statement = connection.prepareStatement(insertQuery);
		
		// Remove Header of txt file
		String lineData = fileScanner.nextLine(); 
		
		int size = 0;
		SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

		// Loop while file has next line to read
		while(fileScanner.hasNextLine()) {
			lineData = fileScanner.nextLine();
			String[] temp = lineData.split("\\|");
			
			// If the type is not 'EQUITY', break out of the loop because I only want to insert stocks into the database.
			if(!temp[2].equals("EQUITY")) break;
			
			// If the total letter of stock Code is not equal to 4 than continue (Indonesian Normal Stock only have 4 letter) 
			if(temp[1].length() != 4) continue;
			
			Date date = null;
			int columnDatabase = 1;
			try {
				date = inputFormat.parse(temp[0]);
				String formattedDate = outputFormat.format(date);
				date = outputFormat.parse(formattedDate);
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				
				// Set the parameter value
				statement.setDate(columnDatabase++, sqlDate);
				statement.setString(columnDatabase++, temp[1]);
				size = temp.length - 1;
				for(int i = 5; i < size; i++) {
					// Skip "Total Local" Column
					if(i == 14) continue;
					statement.setLong(columnDatabase++, Long.parseLong(temp[i]));
				}
			
				// Execute the statement to database
				statement.executeUpdate();
			} catch (ParseException e) {
				System.err.println(e);
			}
		}
        
		// Close the statement
		statement.close();

		// Close file scanner
		fileScanner.close();
	}
}
import database.StockDatabase;
import utilities.Menu;

public class Main {

	public static void main(String[] args) {
		int choice = 0;
		StockDatabase stockDB = new StockDatabase();
		Menu menu = new Menu();
		while(menu.LoginMenu(stockDB) != -1) {
			do {
				choice = menu.mainMenu();
				switch(choice) {
					case 1:
						// Export data to CSV
						menu.menuExport(stockDB);
						break;

					case 2:
						// Insert chosen file txt to database
						menu.menuInsert(stockDB);
						break;
					
					default:
						stockDB.closeDatabase();
						break;
				}
			} while (choice != 3);
		}
		System.out.println("Program Finished");
	}
}
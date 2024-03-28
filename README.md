## Indonesia Stock Holding Composition with JDBC

### Stock Market: Indonesia
### Data Source: KSEI
### Link: https://www.ksei.co.id/archive_download/holding_composition?setLocale=en-US

### System Requirements
- Software used in developing this program:
  - Java 20.0.2
  - MySQL 8.3.0
  - Text Editor: Visual Studio Code

### Program Description
This program allows users to export a CSV file containing the holder composition of scriptless shares for a chosen stock to a folder named "Output". Additionally, it provides functionality to insert data, sourced from KSEI in its original txt file format, into a MySQL database. The program stores the source txt files in a dedicated folder named "Data".

### Program Preparation
1. Install all the required software such as Java and MySQL.
2. Install the MySQL Connector from https://dev.mysql.com/downloads/connector/j/.
3. Add the MySQL Connector jar file to the reference library. <br>
   ![image](https://github.com/RichSvK/Stock_Holder_Composition_Java/assets/87809864/72783c0b-b67e-4c57-a82b-cac5fae66786)

### Database Setup
1. Create database using the commands in `StokDatabase.sql`.

### Program Flow
1. Load the MySQL JDBC driver.
2. Program will ask database name, username, and password with some validation
    * You can check the user and host of the database using "SELECT User, Host FROM mysql.user;" command in MySQL.
    * If the user input "0", the program will exit.
      <br>
      ![image](https://github.com/RichSvK/Stock_Holder_Composition_Java/assets/87809864/3f02ee92-9f74-4f45-a5fb-42c996437337)
      <br>   
    * If the inputted database name, username, or password are incorrect, the connection is failed and the program will prompt the user to input them again.
      <br>
      ![image](https://github.com/RichSvK/Stock_Holder_Composition_Java/assets/87809864/e5c229d7-5342-4398-9a5a-c7830444848d)
      <br>
    * If the inputted database name, username, and password are correct, the connection will be established.
      <br>
      ![image](https://github.com/RichSvK/Stock_Holder_Composition_Java/assets/87809864/75ae70a2-a239-4285-b544-6a03c43757df)
      <br>  
3.  After successfully login, the program will display the main menu.
    <br>
    ![image](https://github.com/RichSvK/Stock_Holder_Composition_Java/assets/87809864/0c7f1230-0758-4038-942e-8a45ca019852)
    <br><br>

    - If user inputted invalid value, the program will prompt the user to input them again
      <br>
      ![image](https://github.com/RichSvK/Stock_Holder_Composition_Java/assets/87809864/8a2fd251-12a3-482f-afde-0496a402a625)
      <br>

    - If the input is "1", the user enters the export menu:
      * If the user input 0, the program will return to the main menu.
      * If the user inputs an invalid or unavailable stock name, the program will prompt them to input again.
      * If the input is a valid stock name available in the database, the program will export a "stockName.csv" file to the "Output" folder.
        <br>
        ![image](https://github.com/RichSvK/Stock_Holder_Composition_Java/assets/87809864/ddeaf13a-7e6b-4344-9093-b55fcc2e48c7)
        <br>
        ![image](https://github.com/RichSvK/Stock_Holder_Composition_Java/assets/87809864/fb2dc76a-a429-44af-bcd1-e79cd7ced3d6)
        <br>
      * Program will go back to main menu
      <br>
  
    - If the input is 2, the user enters the insert menu and displays all files in the "Data" folder:
      * If the user input 0, the program will return to main menu
      * If the user inputs an invalid or out of range value, the program will prompt user to input again.
      * If the input is valid but the data is already inserted in the database, the program will display an exception of duplicate entry and return to the main menu.
      For example, I already insert "Desember.txt" to my database. If I want to insert it "Desember.txt" again to my database, it will be rejected because of duplicate entry of primary key
        <br>
        ![image](https://github.com/RichSvK/Stock_Holder_Composition_Java/assets/87809864/5f8f9b93-af60-449a-9840-42d183007b1f)
        <br>
      * If the input is valid and the data is yet to be inserted in the database, the program will insert the data from the txt file original format from KSEI to the database.
        <br>
        ![image](https://github.com/RichSvK/Stock_Holder_Composition_Java/assets/87809864/5bac3c9f-684d-4e42-9b29-ec68e202b123)
        <br>
      * Program will go back to main menu

    - If the input is 3, the program will return to login menu
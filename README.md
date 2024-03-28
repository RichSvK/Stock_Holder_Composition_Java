## Indonesia Stock Holding Composition with JDBC

#### Stock Market  : Indonesia
#### Data Source   : KSEI
#### Link          : https://www.ksei.co.id/archive_download/holding_composition?setLocale=en-US

#### Windows Operating System
#### Software used in developing this program
* Java 20.0.2
* MySQL 8.3.0
* Text Editor: Visual Studio Code

#### Program Description
This program can export a CSV file containing the holder composition of scriptless shares for a chosen stock to a folder name "Output". Additionally, it provides functionality to insert data, in its original format txt file sourced from KSEI, into the database. The program stores the source txt file in a dedicated folder named "Data".

#### Program Preparation
1. Install all software used like Java and MySQL
2. Install MySQL Connector from https://dev.mysql.com/downloads/connector/j/
3. Add MySQL Connector jar file to reference Library
   <br>
   ![image](https://github.com/RichSvK/Stock_Holder_Composition_Java/assets/87809864/72783c0b-b67e-4c57-a82b-cac5fae66786)
   <br>
5. Make Database using the command in StokDatabase.sql
6. Run the program

#### Program Flow
1. Program will load the driver
2. Program will ask database name, username, and password with some validation
    * You can check the user and host of the database using "SELECT User, Host FROM mysql.user;" command in MySQL
    * If the user input "0" then the program is finished
      <br>
      ![image](https://github.com/RichSvK/Stock_Holder_Composition_Java/assets/87809864/3f02ee92-9f74-4f45-a5fb-42c996437337)
      <br>   
    * If the inputted database name, username, or password is wrong then the connection is failed, the program will ask user to input again
      <br>
      ![image](https://github.com/RichSvK/Stock_Holder_Composition_Java/assets/87809864/e5c229d7-5342-4398-9a5a-c7830444848d)
      <br>
    * If the inputted database name, username, and password is correct then the connection will be established
      <br>
      ![image](https://github.com/RichSvK/Stock_Holder_Composition_Java/assets/87809864/75ae70a2-a239-4285-b544-6a03c43757df)
      <br>  
3. After successfully login, program will go to the main menu
    <br>
    ![image](https://github.com/RichSvK/Stock_Holder_Composition_Java/assets/87809864/0c7f1230-0758-4038-942e-8a45ca019852)
    <br>
  a. If user inputted invalid value, the program will ask user to input again
    <br>
    ![image](https://github.com/RichSvK/Stock_Holder_Composition_Java/assets/87809864/8a2fd251-12a3-482f-afde-0496a402a625)
    <br>
  b. If the input is 1, user will enter export menu
    * If the user input 0, the program will back to main menu
    * If the user input invalid and not available in database, the program will ask user to input again
    * If the user input valid stock name and availabale in database, the program will export the stockName.csv file
      <br>
      ![image](https://github.com/RichSvK/Stock_Holder_Composition_Java/assets/87809864/ddeaf13a-7e6b-4344-9093-b55fcc2e48c7)
      <br>
    * The csv will be exported to "Output" folder
      <br>
      ![image](https://github.com/RichSvK/Stock_Holder_Composition_Java/assets/87809864/fb2dc76a-a429-44af-bcd1-e79cd7ced3d6)
      <br>
    * Program will go back to main menu
      <br>
  c. If the input is 2, user will enter insert menu and display all file in the "Data" folder
    * If the user input 0, the program will go back to main menu
    * If the user input invalid and out of range, the program will ask user to input again
    * If the user input valid in range but the data is already inserted in database, the program will give exception of duplicate entry and go back to main Menu
      For example, I already insert "Desember.txt" to my database. If I want to insert it "Desember.txt" again to my database, it will be rejected because of duplicate entry of primary key
      <br>
      ![image](https://github.com/RichSvK/Stock_Holder_Composition_Java/assets/87809864/5f8f9b93-af60-449a-9840-42d183007b1f)
      <br>
    * If the user input valid in range and data is yet to be inserted in database, the program will insert the data from the txt file original format from KSEI to database
      <br>
      ![image](https://github.com/RichSvK/Stock_Holder_Composition_Java/assets/87809864/5bac3c9f-684d-4e42-9b29-ec68e202b123)
      <br>
    * Program will go back to main menu
  d. If the input is 3, the program will go back to login menu
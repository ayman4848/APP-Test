# APP-Test

This Java program connects to an Oracle database using JDBC (Java Database Connectivity). It allows users to insert, search, update, and display faculty details. <br>

import java.sql.*; — Imports JDBC classes required for database connectivity.<br>
Class.forName("oracle.jdbc.OracleDriver"); — Loads the Oracle JDBC driver.<br>
DriverManager.getConnection(URL, USER, PASSWORD); — Connects Java to the Oracle database using the URL, username, and password.<br>
createTable(); — Creates the FACULTY table if it does not already exist.<br>
PreparedStatement — Executes SQL queries to insert, search, and update faculty records.<br>
ResultSet — Retrieves faculty details from the database.<br>

Main functions<br>

Insert Faculty: Adds faculty ID, name, department, designation, and salary.<br>
Search Faculty: Finds faculty details using faculty ID.<br>
Update Designation: Changes a faculty member's designation.<br>
Update Salary: Updates a faculty member's salary.<br>
Display by Department: Shows faculty belonging to a particular department.<br>
Display All Faculty: Displays all faculty records.<br>

package DataAccess;

import ClassesOfTables.Employee;
import DataStructure.LinkedList;
import javafx.scene.chart.XYChart;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EmployeeDAO2 {
    private static Connection connection;

    public EmployeeDAO2() throws SQLException {
        String url = "jdbc:mysql://127.0.0.1:3306/croissanthouse";
        String username = "root";
        String password = "1211482amaal";
        connection = DriverManager.getConnection(url, username, password);
    }

    // LOGIN
    // Authenticate employee using username and password
    public boolean authenticate(String username, String password) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM Employee WHERE Name = ? AND Password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count") > 0;
                }
            }
        }
        return false;
    }

    // LOGIN
    // Check if the user is a Manager or Employee
    public String getUserRole(String username) throws SQLException {
        String query = "SELECT ManagerID FROM Employee WHERE Name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Integer managerID = resultSet.getObject("ManagerID", Integer.class);
                    return (managerID == null) ? "Manager" : "Employee";
                }
            }
        }
        return "Unknown";
    }

    // Read employees from the database
    public ArrayList<Employee> readEmployees() throws SQLException {
    	ArrayList<Employee>  employeeList = new ArrayList<Employee> ();
        String query = "SELECT * FROM Employee";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getInt("ID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Email"),
                        resultSet.getString("Password"),
                        resultSet.getString("Phone"),
                        resultSet.getDouble("Salary"),
                        resultSet.getObject("ManagerID") == null ? null : resultSet.getInt("ManagerID")
                );
                employeeList.addLast(employee);
            }
        } catch (SQLException e) {
            System.err.println("Error in readEmployees: " + e.getMessage());
            throw e; // Rethrow to trigger the alert
        }
        return employeeList;
    }


    // Add a new employee
    public void addEmployee(Employee employee)  {
    	try {
        String query = "INSERT INTO Employee (ID, Name, Email, Password, Phone, Salary, ManagerID) VALUES (?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, employee.getId());
            preparedStatement.setString(2, employee.getName());
            preparedStatement.setString(3, employee.getEmail());
            preparedStatement.setString(4, employee.getPassword());
            preparedStatement.setString(5, employee.getPhone());
            preparedStatement.setDouble(6, employee.getSalary());
            if (employee.getManagerId() == null) {
                preparedStatement.setNull(7, Types.INTEGER);
            } else {
                preparedStatement.setInt(7, employee.getManagerId());
            }
            preparedStatement.executeUpdate();
        }
    	}catch(SQLException u) {
    		 u.printStackTrace(); // Log the exception
    	}
    }
    int maxid=0;
    // get last id 
	public int getMaxId() {
		
		String queryformaxint = "SELECT MAX(ID) AS maxID FROM Employee";
		try {
			Statement stmt=connection.createStatement();
	        ResultSet rs = stmt.executeQuery(queryformaxint);
	        if (rs.next()) {  // Move cursor to the first row
	            maxid = rs.getInt("maxID");
	        }
	        //connection.close();
	        stmt.close();
		}catch( SQLException o) {}
		return maxid;
	}

    // Update an existing employee
    public void updateEmployee(Employee employee) throws SQLException {
        String query = "UPDATE Employee SET Name = ?, Email = ?, Password = ?, Phone = ?, Salary = ?, ManagerID = ? WHERE ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getEmail());
            preparedStatement.setString(3, employee.getPassword());
            preparedStatement.setString(4, employee.getPhone());
            preparedStatement.setDouble(5, employee.getSalary());
            if (employee.getManagerId() == null) {
                preparedStatement.setNull(6, Types.INTEGER); // This tells the database to insert a NULL value into the ManagerID column, which is of type INTEGER in your SQL table.
            } else {
                preparedStatement.setInt(6, employee.getManagerId());
            }
            preparedStatement.setInt(7, employee.getId());
            preparedStatement.executeUpdate();
        }
    }

    // Delete an employee by ID
    public void deleteEmployeeById(int id) throws SQLException {
        if (isManager(id)) {
           throw new IllegalArgumentException("ASSIGN HIS EMPLOYEES TO DIFFERENT MANAGER THEN DELETE HIM !!");
        }
        String query = "DELETE FROM Employee WHERE ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    
   

    public boolean isManager(int id) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM Employee WHERE ID = ? AND ManagerID IS NULL";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count") > 0; // Returns true if the ID belongs to a manager
                }
            }
        }
        return false; // If no result is found, the ID does not belong to a manager
    }
    
 

    // Check if an employee exists by ID
    public boolean employeeExists(int id) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM Employee WHERE ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count") > 0;
                }
            }
        }
        return false;
    }

    public int getEmployeeID(String username) throws SQLException {
        String query = "SELECT ID FROM Employee WHERE Name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt ("ID");
            }
        }
        return -1; // Return -1 if the employee is not found
    }

    public int getTotalEmployees() throws SQLException {
        String query = "SELECT COUNT(*) AS TotalEmployees FROM Employee";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt("TotalEmployees");
            }
        }
        return 0;
    }
    
    //================== Statistics===========================
    public static double[] getSalaryStatistics() {

        String query = "SELECT AVG(Salary) AS avg_salary, MIN(Salary) AS min_salary, MAX(Salary) AS max_salary FROM Employee";
        double[] stats = new double[3];

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                stats[0] = resultSet.getDouble("avg_salary");
                stats[1] = resultSet.getDouble("min_salary");
                stats[2] = resultSet.getDouble("max_salary");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stats;
    }
    
public Map<String, Integer> getOrdersByEmployee() {
        

        String query = "SELECT Name, (SELECT COUNT(OrderID) FROM Orders WHERE Employee_ID = ID) AS orderCount " +
                       "FROM Employee ORDER BY orderCount DESC";

        Map<String, Integer> orderData = new HashMap<>();

        try (
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String employeeName = resultSet.getString("Name");
                int orderCount = resultSet.getInt("orderCount");
                orderData.put(employeeName, orderCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderData;
    }

	public XYChart.Series<String, Number> getOrdersByCategory() {
	    String query = "SELECT Category.Name AS categoryName, COUNT(OrderDetails.OrderID) AS orderCount " +
	                   "FROM Category " +
	                   "JOIN Items ON Category.CategoryID = Items.CategoryID " +
	                   "JOIN OrderDetails ON Items.ItemID = OrderDetails.ItemID " +
	                   "GROUP BY Category.Name ORDER BY orderCount DESC";
	
	    XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
	    dataSeries.setName("Orders by Category");
	
	    try (
	         PreparedStatement preparedStatement = connection.prepareStatement(query);
	         ResultSet resultSet = preparedStatement.executeQuery()) {
	
	        while (resultSet.next()) {
	            String categoryName = resultSet.getString("categoryName");
	            int orderCount = resultSet.getInt("orderCount");
	            dataSeries.getData().add(new XYChart.Data<>(categoryName, orderCount));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	
	    return dataSeries;
	}


    // Close the database connection
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}

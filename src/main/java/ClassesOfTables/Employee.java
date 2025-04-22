package ClassesOfTables;

import java.sql.SQLException;

import DataAccess.EmployeeDAO2;

public class Employee {
    private int id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private double salary; // Updated field
    private Integer managerId;

    public Employee(int id, String name, String email, String password, String phone, double salary, Integer managerId) {
    	 this.id = id;
         setName(name); 
         setEmail(email);
         this.password = password;
         setPhone(phone);
         this.salary = salary;
         this.managerId = managerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
    	 // Regular expression for validating a name (letters and spaces only)
        String nameRegex = "^[a-zA-Z\\s]+$";

         // Validate the name against the regex
         if (name.matches(nameRegex)) {
             // Capitalize the first character of each word
             String[] words = name.trim().split("\\s+");
             StringBuilder formattedName = new StringBuilder();
             for (String word : words) {
                 formattedName.append(Character.toUpperCase(word.charAt(0)))
                              .append(word.substring(1).toLowerCase())
                              .append(" ");
             }
             this.name =formattedName.toString().trim();
         }
         else
        	 throw new IllegalArgumentException("Name should only contain letters and spaces !!");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
    	// Regular expression for validating an email address
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if( !email.matches(emailRegex))
        	throw new IllegalArgumentException("the email format is not valid !!");
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if ( phone.trim().length() != 10) 
           throw new IllegalArgumentException("phone number should be 10 digits length only !!");
        else if(!phone.startsWith("05"))
        	throw new IllegalArgumentException("phone number should start with '05' !!");
        else if(!phone.matches("\\d{10}"))
        	throw new IllegalArgumentException("phone number should only contain digits !!");
        this.phone = phone;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
    	EmployeeDAO2 e;
		try {
			e = new EmployeeDAO2();
		
	     if(!e.isManager(managerId))
			throw new IllegalArgumentException("The employee you are assigning as a manager is NOT a manager !!");
		else
    	if(!e.employeeExists(managerId))
    		throw new IllegalArgumentException("The employee you are assigning as a manager does not exist !!");
         this.managerId = managerId;
    	} catch (SQLException o) {
			// TODO Auto-generated catch block
			o.printStackTrace();
		}
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", salary=" + salary + // Include salary
                ", managerId=" + managerId +
                '}';
    }
}

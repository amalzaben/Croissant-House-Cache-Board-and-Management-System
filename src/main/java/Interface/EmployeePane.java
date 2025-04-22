package Interface;

import ClassesOfTables.Employee;
import DataAccess.EmployeeDAO;
import DataAccess.EmployeeDAO2;
import DataStructure.LinkedList;
import DataStructure.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EmployeePane {
    private BorderPane pane;
    private TableView<Employee> employeeTable=new TableView<Employee>();
    private ArrayList<Employee> employeeList;
    private Employee temp;
    private ObservableList<Employee> employeeObservableList;
    int maxid =0;

    public EmployeePane() {
        pane = new BorderPane();
        pane.setStyle("-fx-background-color: white;");
        employeeObservableList = FXCollections.observableArrayList();
		loadEmployees();
        pane.setCenter(getEmployeeMangement());
        
    }

    private VBox getEmployeeMangement()  {
    	VBox vbox = new VBox(20);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(10));
		vbox.setStyle("-fx-background-color:white;");
		
		employeeTable.setMaxWidth(1300);
		employeeTable.setMaxHeight(900);
		employeeTable.setStyle("-fx-border-color:red;-fx-border-Width:3;-fx-text-fill: white;-fx-font-weight: bold;");
		
		TableColumn<Employee, Integer> idColumn = new TableColumn<>("ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		idColumn.setMinWidth(150); 
		idColumn.setStyle("-fx-alignment: CENTER;");

		TableColumn<Employee, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameColumn.setMinWidth(220); 
		nameColumn.setStyle("-fx-alignment: CENTER;");

		TableColumn<Employee, String> emailColumn = new TableColumn<>("Email");
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
		emailColumn.setMinWidth(220); 
		emailColumn.setStyle("-fx-alignment: CENTER;");

		TableColumn<Employee, String> passwordColumn = new TableColumn<>("Password");
		passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
		passwordColumn.setMinWidth(190); 
		passwordColumn.setStyle("-fx-alignment: CENTER;");

		TableColumn<Employee, String> phoneColumn = new TableColumn<>("Phone");
		phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
		phoneColumn.setMinWidth(200); 
		phoneColumn.setStyle("-fx-alignment: CENTER;");

		TableColumn<Employee, Double> salaryColumn = new TableColumn<>("Salary");
		salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
		salaryColumn.setMinWidth(150); 
		salaryColumn.setStyle("-fx-alignment: CENTER;");

		TableColumn<Employee, Integer> managerIdColumn = new TableColumn<>("Manager ID");
		managerIdColumn.setCellValueFactory(new PropertyValueFactory<>("managerId"));
		managerIdColumn.setMinWidth(150); 
		managerIdColumn.setStyle("-fx-alignment: CENTER;");

		employeeTable.getColumns().addAll(idColumn, nameColumn, emailColumn, passwordColumn, phoneColumn, salaryColumn, managerIdColumn);
        
		employeeTable.setItems(employeeObservableList);
		
		//========== search box=================================
		HBox searchbox=new HBox(20);
		searchbox.setAlignment(Pos.TOP_LEFT);
		searchbox.setPadding(new Insets(0,100,0,100));
		
		Button search = new Button("Search for Employee");
		search.setStyle("-fx-background-color: rgb(230, 0, 0); -fx-text-fill: white; -fx-border-color: white; -fx-font-weight: bold; -fx-font-size: 20px;-fx-background-radius: 30; -fx-border-radius: 30;");
		search.setPrefWidth(230);
		search.setPrefHeight(50);
		search.setOnMouseEntered(e -> search.setTranslateY(search.getTranslateY() - 7));
		search.setOnMouseExited(e -> search.setTranslateY(search.getTranslateY() + 7));
		
		 TextField searchField=new TextField();
		 searchField.setStyle("-fx-border-color: rgb(230, 0, 0); -fx-border-width: 2;-fx-pref-width: 250px;-fx-pref-height: 40px; -fx-font-size: 20px;");
		 
		searchbox.getChildren().addAll(search,searchField);
		
		Button add = new Button("Add Employee");
		add.setStyle("-fx-background-color: rgb(230, 0, 0); -fx-text-fill: white; -fx-border-color: white; -fx-font-weight: bold; -fx-font-size: 20px;-fx-background-radius: 30; -fx-border-radius: 30");
		add.setPrefWidth(230);
		add.setPrefHeight(50);
		add.setOnMouseEntered(e -> add.setTranslateY(add.getTranslateY() - 7));
		add.setOnMouseExited(e -> add.setTranslateY(add.getTranslateY() + 7));
		
		Button update = new Button("Update Employee");
		update.setStyle("-fx-background-color: rgb(230, 0, 0); -fx-text-fill: white; -fx-border-color: white; -fx-font-weight: bold; -fx-font-size: 20px;-fx-background-radius: 30; -fx-border-radius: 30;");
		update.setPrefWidth(230);
		update.setPrefHeight(50);
		update.setOnMouseEntered(e -> update.setTranslateY(update.getTranslateY() - 7));
		update.setOnMouseExited(e -> update.setTranslateY(update.getTranslateY() + 7));
		
		Button refresh = new Button("Refreash");
		refresh.setStyle("-fx-background-color: rgb(230, 0, 0); -fx-text-fill: white; -fx-border-color: white; -fx-font-weight: bold; -fx-font-size: 20px;-fx-background-radius: 30; -fx-border-radius: 30;");
		refresh.setPrefWidth(150);
		refresh.setPrefHeight(50);
		refresh.setOnMouseEntered(e -> refresh.setTranslateY(refresh.getTranslateY() - 7));
		refresh.setOnMouseExited(e -> refresh.setTranslateY(refresh.getTranslateY() + 7));
		
		Button remove = new Button("Remove Employee");
		remove.setStyle("-fx-background-color: rgb(230, 0, 0); -fx-text-fill: white; -fx-border-color: white; -fx-font-weight: bold; -fx-font-size: 20px;-fx-background-radius: 30; -fx-border-radius: 30;");
		remove.setPrefWidth(230);
		remove.setPrefHeight(50);
		remove.setOnMouseEntered(e -> remove.setTranslateY(remove.getTranslateY() - 7));
		remove.setOnMouseExited(e -> remove.setTranslateY(remove.getTranslateY() + 7));
		
		HBox buttinshbox=new HBox(20);
		buttinshbox.setAlignment(Pos.TOP_LEFT);
		buttinshbox.setPadding(new Insets(0,100,0,100));
		buttinshbox.getChildren().addAll(add,update,remove,refresh);
        
		//============== set on action ===================================
		
		employeeTable.setOnMouseClicked(event -> {
	        if (event.getClickCount() == 2) {
	            Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
	            if (selectedEmployee != null) {
	                temp=selectedEmployee;
	                searchField.setText(temp.getId()+"");
	            }
	        }
	    });
        
		add.setOnAction(e->{
			addEmployeeStage();
		});
		update.setOnAction(e->{
			if (temp != null) {
			updateEmployeeStage();
			}else
				getAlert("NO EMPLOYEE SELECTED !!");
		});
		search.setOnAction(e->{
			try {
			updateEmployeeOBList();
			if(!searchField.getText().isEmpty()) {
				temp=searchForEmployee(Integer.parseInt(searchField.getText()));
				if(temp!=null) {
					for (Employee employee : employeeObservableList) {
	                    if (employee.getId()==temp.getId()) {
	                    	employeeObservableList.remove(employee);
	                    	employeeObservableList.add(0, employee);
	                    	employeeTable.setRowFactory(tv -> new TableRow<>() {
	                            @Override
	                            protected void updateItem(Employee item, boolean empty) {
	                                super.updateItem(item, empty);
	                                if (item == null || empty) {
	                                    setStyle(""); 
	                                } else if (item.getId()==temp.getId()) {
	                                    setStyle("-fx-background-color: rgb(250, 80, 80)\r\n"
	                                    		+ "; -fx-font-weight: bold;"); 
	                                } else {
	                                    setStyle(""); // Reset non-matching rows
	                                }
	                            }
	                    	});
	                    	employeeTable.refresh(); 
	                    	break;
				        }
					}
				}
				else {
					getAlert("THE EMPLOYEE ID YOU ARE SEARCHING FOR IS NOT VALID");
				}
			}
			}catch(Exception o) {}
		});
		
	refresh.setOnAction(e->{
		updateEmployeeOBList();
		searchField.setText("");
		employeeTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);
                setStyle(""); // Reset all rows to their original style
            }
        });
        employeeTable.refresh(); 
		
	});
	remove.setOnAction(e->{
		if(temp!=null) {
			Alert alert=new Alert(Alert.AlertType.WARNING);
			alert.setTitle("WARNING");
			alert.setContentText("YOU ARE TRYING TO REMOVE THIS EMPLOYEE FROM THE SYSTEM , PRESS CONFIRM TO PROCEED !");
			alert.setHeaderText("Please Pay Attention !!");
			ButtonType confirm= new ButtonType("confirm");
			alert.getDialogPane().getButtonTypes().clear();
			alert.getDialogPane().getButtonTypes().add(confirm);
			alert.setResultConverter(dialogButton -> {
	            if (dialogButton == confirm) {
					try {
						EmployeeDAO2 employeeDAO2 = new EmployeeDAO2();
						employeeDAO2.deleteEmployeeById(temp.getId());
						//employeeDAO2.closeConnection();
						employeeList.remove(temp);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						//e1.printStackTrace();
					}catch(IllegalArgumentException j) {getAlert(j.getMessage());}
	                 updateEmployeeOBList();
	                 searchField.setText("");
	            }
			return null;
		    });
			alert.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
			alert.showAndWait();
		}
	});
        
        vbox.getChildren().addAll(searchbox,employeeTable,buttinshbox);
		return vbox;
    }


    private void loadEmployees() {
        try {
            EmployeeDAO2 employeeDAO = new EmployeeDAO2();
            employeeList = (ArrayList<Employee>) employeeDAO.readEmployees();
            updateEmployeeOBList();
            employeeDAO.closeConnection();
        } catch (SQLException e) {
            getAlert( "Could not load employee data.");
        }
    }



   
    private void updateEmployeeStage() {

    	VBox vbox = new VBox(20);
    	vbox.setAlignment(Pos.CENTER);

    	Label header = new Label("Update Employee Information");
    	header.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
    	header.setFont(Font.font("Times New Roman", 20));

    	HBox headbox = new HBox();
    	headbox.setAlignment(Pos.CENTER);
    	headbox.setPadding(new Insets(10));
    	headbox.getChildren().add(header);
    	headbox.setStyle("-fx-background-color: rgb(230, 0, 0);");

    	String style = "-fx-text-fill: rgb(230, 0, 0); -fx-font-weight: bold;";
    	Label name = new Label("Full Name");
    	TextField namef = new TextField();
    	Label id = new Label("ID Number");
    	Label idf = new Label();
    	idf.setText("ID number");  // ID will be shown here (can't be edited)
    	Label email = new Label("Email");
    	TextField emailf = new TextField();
    	Label phone = new Label("Phone Number");
    	TextField phonef = new TextField();
    	Label salary = new Label("Salary");
    	TextField salaryf = new TextField();
    	Label managerId = new Label("Manager ID");
    	TextField managerIdf = new TextField();
    	Label password = new Label("Password");
    	TextField passwordf = new TextField();

    	// setStyles
    	name.setStyle(style);
    	id.setStyle(style);
    	email.setStyle(style);
    	phone.setStyle(style);
    	salary.setStyle(style);
    	managerId.setStyle(style);
    	password.setStyle(style);
    	namef.setStyle("-fx-border-color: rgb(230, 0, 0);");
    	emailf.setStyle("-fx-border-color: rgb(230, 0, 0);");
    	idf.setStyle(style);
    	phonef.setStyle("-fx-border-color: rgb(230, 0, 0);");
    	salaryf.setStyle("-fx-border-color: rgb(230, 0, 0);");
    	managerIdf.setStyle("-fx-border-color: rgb(230, 0, 0);");
    	passwordf.setStyle("-fx-border-color: rgb(230, 0, 0);");

    	GridPane gpane = new GridPane();
    	gpane.add(id, 0, 0);
    	gpane.add(name, 0, 1);
    	gpane.add(email, 0, 2);
    	gpane.add(phone, 0, 3);
    	gpane.add(salary, 0, 4);
    	gpane.add(managerId, 0, 5);
    	gpane.add(password, 0, 6);
    	gpane.add(idf, 1, 0);
    	gpane.add(namef, 1, 1);
    	gpane.add(emailf, 1, 2);
    	gpane.add(phonef, 1, 3);
    	gpane.add(salaryf, 1, 4);
    	gpane.add(managerIdf, 1, 5);
    	gpane.add(passwordf, 1, 6);
    	gpane.setAlignment(Pos.CENTER);
    	gpane.setVgap(20);
    	gpane.setHgap(20);

    	Button update = new Button("      UPDATE     ");
    	update.setStyle("-fx-background-color: rgb(230, 0, 0); -fx-text-fill: white; -fx-border-color: white; -fx-font-weight: bold;-fx-font-size: 14px;-fx-background-radius: 30; -fx-border-radius: 30;");
    	update.setOnMouseEntered(e -> update.setTranslateY(update.getTranslateY() - 7));
    	update.setOnMouseExited(e -> update.setTranslateY(update.getTranslateY() + 7));

    	vbox.getChildren().addAll(headbox, gpane, update);
    	BorderPane borderpane = new BorderPane();
    	borderpane.setCenter(vbox);
    	//===================
    	if (temp != null) {
    	    idf.setText(temp.getId() + "");               // Set ID
    	    namef.setText(temp.getName());                 // Set Name
    	    emailf.setText(temp.getEmail());               // Set Email (added Email field)
    	    phonef.setText(temp.getPhone());               // Set Phone
    	    salaryf.setText(temp.getSalary() + "");        // Set Salary
    	    if (temp.getManagerId() != null) {             // Set Manager ID (if available)
    	        managerIdf.setText(temp.getManagerId() + "");
    	    } else {
    	        managerIdf.setText("");                    // If no manager, leave blank
    	    }
    	    passwordf.setText(temp.getPassword());         // Set Password

    	   
    	}
    	update.setOnAction(e->{
    		if(!namef.getText().isEmpty() &&		   
		    !passwordf.getText().isEmpty() &&
		    !salaryf.getText().isEmpty() &&
		    (managerIdf.getText().isEmpty() || managerIdf.getText().matches("\\d+"))) {
				Alert alert=new Alert(Alert.AlertType.WARNING);
				alert.setTitle("WARNING");
				alert.setContentText("YOU ARE TRYING TO UPDATE THIS EMPLOYEE INFORMATION , PRESS CONFIRM TO PROCEED !");
				alert.setHeaderText("Please Pay Attention !!");
				ButtonType confirm= new ButtonType("confirm");
				alert.getDialogPane().getButtonTypes().clear();
				alert.getDialogPane().getButtonTypes().add(confirm);
				alert.setResultConverter(dialogButton -> {
		            if (dialogButton == confirm) {
		            	try {
		            	Employee employee=new Employee(
		            			Integer.parseInt(idf.getText()),       // ID
		                	    namef.getText(),                       // Name
		                	    emailf.getText(),                      // Email
		                	    passwordf.getText(),                   // Password
		                	    phonef.getText(),                      // Phone
		                	    Double.parseDouble(salaryf.getText()), // Salary
		                	    managerIdf.getText().isEmpty() ? null : Integer.parseInt(managerIdf.getText()) // Manager ID
		                	);
							EmployeeDAO2 employeeDAO2 = new EmployeeDAO2();
							employeeDAO2.updateEmployee(employee);
							//employeeDAO2.closeConnection();
							temp.setName( namef.getText());
							temp.setPhone(phonef.getText());
							temp.setSalary(Double.parseDouble(salaryf.getText()));
							temp.setPassword(passwordf.getText());
							temp.setEmail(emailf.getText());
							temp.setManagerId(managerIdf.getText().isEmpty() ? null : Integer.parseInt(managerIdf.getText()));
							updateEmployeeOBList();
							employeeTable.refresh();
						}catch(IllegalArgumentException i) {
							getAlert(i.getMessage());
						}
		            	catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		            }
				return null;
			    });
				alert.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
				alert.showAndWait();
			}
			else
				getAlert("REQUIRED INFO ARE MISSING , PLEASE FILL ALL FIELDS THEN PRESS THE BUTTON !!");
    	});

    	Stage updateEmployee = new Stage();
    	Scene scene = new Scene(borderpane, 350, 500);
    	updateEmployee.setTitle("Update Employee Information");
    	updateEmployee.setScene(scene);
    	updateEmployee.show();
	}


   

    
    //================= add Stage ============================
    private void addEmployeeStage() {

    	VBox vbox = new VBox(20);
    	vbox.setAlignment(Pos.CENTER);

    	Label header = new Label("Add New Employee");
    	header.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
    	header.setFont(Font.font("Times New Roman", 20));

    	HBox headbox = new HBox();
    	headbox.setAlignment(Pos.CENTER);
    	headbox.setPadding(new Insets(10));
    	headbox.getChildren().add(header);
    	headbox.setStyle("-fx-background-color: rgb(230, 0, 0);");

    	String style = "-fx-text-fill: rgb(230, 0, 0); -fx-font-weight: bold;";

    	// Labels and input fields
    	Label name = new Label("Full Name");
    	TextField namef = new TextField();

    	Label id = new Label("ID Number");
    	Label idf = new Label("id number");

    	Label email = new Label("Email");
    	TextField emailf = new TextField();

    	Label password = new Label("Password");
    	TextField passwordf = new TextField();

    	Label phone = new Label("Phone Number");
    	TextField phonef = new TextField();

    	Label salary = new Label("Salary");
    	TextField salaryf = new TextField();

    	Label managerId = new Label("Manager ID");
    	TextField managerIdf = new TextField();

    	// Styles for fields
    	name.setStyle(style);
    	id.setStyle(style);
    	email.setStyle(style);
    	password.setStyle(style);
    	phone.setStyle(style);
    	salary.setStyle(style);
    	managerId.setStyle(style);

    	namef.setStyle("-fx-border-color: rgb(230, 0, 0);");
    	idf.setStyle(style);
    	emailf.setStyle("-fx-border-color: rgb(230, 0, 0);");
    	passwordf.setStyle("-fx-border-color: rgb(230, 0, 0);");
    	phonef.setStyle("-fx-border-color: rgb(230, 0, 0);");
    	salaryf.setStyle("-fx-border-color: rgb(230, 0, 0);");
    	managerIdf.setStyle("-fx-border-color: rgb(230, 0, 0);");

    	// GridPane to organize fields
    	GridPane gpane = new GridPane();
    	gpane.add(id, 0, 0);
    	gpane.add(name, 0, 1);
    	gpane.add(email, 0, 2);
    	gpane.add(password, 0, 3);
    	gpane.add(phone, 0, 4);
    	gpane.add(salary, 0, 5);
    	gpane.add(managerId, 0, 6);

    	gpane.add(idf, 1, 0); 
    	gpane.add(namef, 1, 1);
    	gpane.add(emailf, 1, 2);
    	gpane.add(passwordf, 1, 3);
    	gpane.add(phonef, 1, 4);
    	gpane.add(salaryf, 1, 5);
    	gpane.add(managerIdf, 1, 6);

    	gpane.setAlignment(Pos.CENTER);
    	gpane.setVgap(20);
    	gpane.setHgap(20);

    	// Add button
    	Button add = new Button("       ADD      ");
    	add.setStyle("-fx-background-color: rgb(230, 0, 0); -fx-text-fill: white; -fx-border-color: white; -fx-font-weight: bold;-fx-font-size: 14px;-fx-background-radius: 30; -fx-border-radius: 30;");
    	add.setOnMouseEntered(e -> add.setTranslateY(add.getTranslateY() - 7));
    	add.setOnMouseExited(e -> add.setTranslateY(add.getTranslateY() + 7));

    	EmployeeDAO2 employeeDAO2;
		try {
			employeeDAO2 = new EmployeeDAO2();
			maxid = employeeDAO2.getMaxId() + 1; 
			idf.setText(maxid+"");// Fetch max ID and increment by 1
			 System.out.println(maxid+"");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		add.setOnAction(e -> {
		    try {
		        // Validate required fields
		        if (!namef.getText().isEmpty() &&
		            !passwordf.getText().isEmpty() &&
		            !salaryf.getText().isEmpty() &&
		            (managerIdf.getText().isEmpty() || managerIdf.getText().matches("\\d+"))) {
		        	EmployeeDAO2 employeeDAO1 = new EmployeeDAO2();

		            // Create an Employee object with the provided input
		            Employee employee = new Employee(
		                maxid,                                 // ID
		                namef.getText(),                       // Name
		                emailf.getText(),                      // Email
		                passwordf.getText(),                   // Password
		                phonef.getText(),                      // Phone
		                Double.parseDouble(salaryf.getText()), // Salary
		                managerIdf.getText().isEmpty() ? null : Integer.parseInt(managerIdf.getText()) // Manager ID
		            );

		            // Add employee to the database
		            employeeDAO1.addEmployee(employee);

		            // Close the connection
		            employeeDAO1.closeConnection();

		            // Add employee to the list and update the UI
		            employeeList.addLast(employee);
		            updateEmployeeOBList();
		            employeeTable.refresh();

		            // Update max ID and clear input fields
		            maxid++;
		            idf.setText(String.valueOf(maxid));
		            namef.clear();
		            emailf.clear();
		            phonef.clear();
		            salaryf.clear();
		            passwordf.clear();
		            managerIdf.clear();
		        } else {
		            // Alert for missing or invalid information
		            getAlert("REQUIRED INFO ARE MISSING, PLEASE FILL ALL FIELDS CORRECTLY THEN PRESS THE BUTTON!");
		        }
		    } catch (NumberFormatException nfe) {
		        getAlert("Invalid salary or manager ID format. Please enter valid numeric values.");
		    }catch(IllegalArgumentException i) {
		    	getAlert(i.getMessage());
		    } catch (SQLException sqlEx) {
		        sqlEx.printStackTrace();
		        getAlert("Error while adding the employee to the database. Please try again.");
		    } catch (Exception ex) {
		        ex.printStackTrace();
		        getAlert("An unexpected error occurred. Please try again.");
		    }
		});
		
		
		vbox.getChildren().addAll(headbox,gpane,add);
		BorderPane borderpane=new BorderPane();
		borderpane.setCenter(vbox);
		
		Stage addMajor = new Stage();
		Scene scene=new Scene(borderpane,350,450); 
		addMajor.setTitle("Add New Employee");
		addMajor.setScene(scene);
		addMajor.show();
	}

    
    public BorderPane getPane() {
        return pane;
    }
    
	///////////////// get alert ////////////////////////
	private void getAlert(String str) {
		Alert alert=new Alert(Alert.AlertType.ERROR);
		alert.setTitle("ERORR");
		alert.setContentText(str);
		alert.setHeaderText("ERROR ALERT");
		alert.showAndWait();
	}
    
    private void updateEmployeeOBList() {
    	employeeObservableList.clear();
    	employeeObservableList.addAll(employeeList);
		employeeTable.refresh();
	}
    
    private Employee searchForEmployee(int id) {
		for(int i=0;i<employeeList.size();i++) {
			if(employeeList.get(i).getId()==id) {
				return employeeList.get(i);
			}
		}
		return null;
	}
}

package Interface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import DataAccess.ClientDAO;
import DataAccess.EmployeeDAO;
import DataAccess.EmployeeDAO2;
import DataAccess.OrderDAO;
import DataAccess.SupplierDAO;
import DataStructure.LinkedList;
import DataStructure.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class DashboardPane {
    private GridPane gridPane;
    private static Label totalRevenueLabel;
    private static Label totalEmployeesLabel;
    private static Label totalClientsLabel;
    private static Label totalSuppliersLabel;    private BarChart<String, Number> barChart;
    private PieChart pieChart;
    private BarChart<String, Number> categoryChart=getCatagorystat();
    private BarChart salarychart=getsalarychart();
    private BarChart totalorders=getTotalOrdersByEmployee();

    private static ObservableList<PieChart.Data> pieChartData;
    private static XYChart.Series<String, Number> barChartSeries;
    private XYChart.Series<String, Number> lineChartSeries;

    public DashboardPane() {
        gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setStyle("-fx-background-color: white");

        // Initialize observable data structures
        //pieChartData = FXCollections.observableArrayList();
       // barChartSeries = new XYChart.Series<>();
        lineChartSeries = new XYChart.Series<>();

        // Initialize charts
  
        //lineChart = createLineChart();

        // Add charts to the grid pane
        gridPane.add(totalorders, 0, 0);
        gridPane.add(salarychart, 1, 0);
        gridPane.add(categoryChart, 0, 1);
       // gridPane.add(lineChart, 0, 1, 2, 1);

        VBox statsBox = createStatsBox();
        gridPane.add(statsBox, 2, 0, 1, 2); // Spans 2 rows

        gridPane.setHgap(50);
        gridPane.setAlignment(Pos.CENTER);


        // Load initial data
    
        refreshOrderChart();
        refreshSalaryChart();
    }

    private VBox createStatsBox() {
        VBox statsBox = new VBox(65);
        statsBox.setPadding(new Insets(20));
        statsBox.setAlignment(Pos.TOP_CENTER);

        // Total Revenue
        totalRevenueLabel = createStatLabel("Total Revenue", "0.00");

        // Total Employees
        totalEmployeesLabel = createStatLabel("Number of Employees", "0");

        // Total Clients
        totalClientsLabel = createStatLabel("Number of Clients", "0");

        // Total Suppliers
        totalSuppliersLabel = createStatLabel("Number of Suppliers", "0");

        // Add all labels to the VBox
        statsBox.getChildren().addAll(totalRevenueLabel, totalEmployeesLabel, totalClientsLabel, totalSuppliersLabel);

        // Load statistics data
        refreshStats();

        return statsBox;
    }

    private Label createStatLabel(String title, String value) {
        Label label = new Label(title + "\n" + value);
        label.getStyleClass().add("statistics-square");
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setPrefSize(200, 100);
        return label;
    }

    public static void refreshStats() {
        try {
            // Total Revenue
            OrderDAO orderDAO = new OrderDAO();
            double totalRevenue = orderDAO.getTotalRevenue();
            totalRevenueLabel.setText("Total Revenue\n" + String.format("%.2f", totalRevenue));
            orderDAO.closeConnection();

            // Total Employees
            EmployeeDAO employeeDAO = new EmployeeDAO();
            int totalEmployees = employeeDAO.getTotalEmployees();
            totalEmployeesLabel.setText("Number of Employees\n" + totalEmployees);
            employeeDAO.closeConnection();

            // Total Clients
            ClientDAO clientDAO = new ClientDAO();
            int totalClients = clientDAO.getTotalClients();
            totalClientsLabel.setText("Number of Clients\n" + totalClients);
            clientDAO.closeConnection();

            // Total Suppliers
            SupplierDAO supplierDAO = new SupplierDAO();
            int totalSuppliers = supplierDAO.getTotalSuppliers();
            totalSuppliersLabel.setText("Number of Suppliers\n" + totalSuppliers);
            supplierDAO.closeConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GridPane getGridPane() {
        return gridPane;
    }



    private LineChart<String, Number> createLineChart() {
        LineChart<String, Number> chart = new LineChart<>(new CategoryAxis(), new NumberAxis());
        chart.setTitle("Daily Revenue Trends");
        lineChartSeries.setName("Daily Revenue");
        chart.getData().add(lineChartSeries); // Add the series to the chart
        return chart;
    }



    public void refreshLineChart() {
        lineChartSeries.getData().clear(); // Clear existing data

        try {
            OrderDAO orderDAO = new OrderDAO();
            LinkedList data = orderDAO.getDailyRevenue();
            Node current = data.getFront();

            while (current != null) {
                Object[] dailyData = (Object[]) current.getElement();
                lineChartSeries.getData().add(new XYChart.Data<>((String) dailyData[0], (Double) dailyData[1]));
                current = current.getNext();
            }

            orderDAO.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private BarChart getsalarychart() {
    	EmployeeDAO2 w;
		try {
			w = new EmployeeDAO2();
			double[] stats = w.getSalaryStatistics();
			// Create axes
			CategoryAxis xAxis = new CategoryAxis();
			xAxis.setLabel("Metric");
			
			NumberAxis yAxis = new NumberAxis();
			yAxis.setLabel("Salary");
			
			// Create BarChart
			BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
			barChart.setTitle("Salary Statistics");
			
			// Add data to the chart
			XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
			dataSeries.setName("Salaries");
			dataSeries.getData().add(new XYChart.Data<>("Average", stats[0]));
			dataSeries.getData().add(new XYChart.Data<>("Minimum", stats[1]));
			dataSeries.getData().add(new XYChart.Data<>("Maximum", stats[2]));
			
			barChart.getData().add(dataSeries);
			return barChart;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

    }
    
    private BarChart getTotalOrdersByEmployee() {
    	EmployeeDAO2 w;
		try {
			w = new EmployeeDAO2();
			Map<String, Integer> orderData = w.getOrdersByEmployee();
			
			// Create axes
			CategoryAxis xAxis = new CategoryAxis();
			xAxis.setLabel("Employee");
			
			NumberAxis yAxis = new NumberAxis();
			yAxis.setLabel("Number of Orders");
			
			// Create BarChart
			BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
			barChart.setTitle("Total Orders by Employee");
			
			// Add data to the chart
			XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
			dataSeries.setName("Orders");
			
			for (Map.Entry<String, Integer> entry : orderData.entrySet()) {
				dataSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
			}
			
			barChart.getData().add(dataSeries);
			return barChart;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return barChart;
    }
    ///////////// refreshing methods /////////////////////////////
    public void refreshOrderChart() {
    	EmployeeDAO2 w;
		try {
			w = new EmployeeDAO2();
			Map<String, Integer> orderData = w.getOrdersByEmployee();
			totalorders.getData().clear();
			XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
			dataSeries.setName("Orders");
			
			for (Map.Entry<String, Integer> entry : orderData.entrySet()) {
				dataSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
			}
			
			totalorders.getData().add(dataSeries);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void refreshSalaryChart() {
    	EmployeeDAO2 w;

			try {
				w = new EmployeeDAO2();
				double[] salaryData = w.getSalaryStatistics();
				salarychart.getData().clear();
				
				XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
				dataSeries.setName("Salaries");
				
				dataSeries.getData().add(new XYChart.Data<>("Average", salaryData[0]));
				dataSeries.getData().add(new XYChart.Data<>("Minimum", salaryData[1]));
				dataSeries.getData().add(new XYChart.Data<>("Maximum", salaryData[2]));
				
				salarychart.getData().add(dataSeries);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
    }
	public BarChart getCatagorystat() {
		 // Initialize charts
	   EmployeeDAO2 t;
	try {
		t = new EmployeeDAO2();
		CategoryAxis categoryXAxis = new CategoryAxis();
		categoryXAxis.setLabel("Category");
		
		NumberAxis categoryYAxis = new NumberAxis();
		categoryYAxis.setLabel("Number of Orders");
		
		categoryChart = new BarChart<>(categoryXAxis, categoryYAxis);
		categoryChart.setTitle("Orders by Category");
		XYChart.Series<String, Number> dataSeries = t.getOrdersByCategory();
		categoryChart.getData().add(dataSeries);
		t.closeConnection();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return categoryChart;
	}
}

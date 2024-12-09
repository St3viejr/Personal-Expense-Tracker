package Project_v2;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.chart.PieChart;

public class ChartScreen extends MainApp {

    private Budget budget;
    private Stage stage;
    private User user;

    // Constructor to initialize with Budget and User
    public ChartScreen(Stage stage, User user, Budget budget) {
        this.stage = stage;
        this.user = user;
        this.budget = budget;
    }

    // Method to generate and display the PieCharts
    @SuppressWarnings("unused")
    public Scene createChartScene() {
        // First PieChart - Initial Budget (Static Income vs Expenses)
        double initialIncome = budget.getTotalIncome(); // Use static budget values
        double initialExpenses = budget.getTotalExpenses();

        PieChart.Data incomeData = new PieChart.Data("Income: $" + initialIncome, initialIncome);
        PieChart.Data expenseData = new PieChart.Data("Expenses: $" + initialExpenses, initialExpenses);

        PieChart initialPieChart = new PieChart();
        initialPieChart.getData().addAll(incomeData, expenseData);
        initialPieChart.setTitle("Estimated Budget");
        initialPieChart.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

        // Remove legend for the initial PieChart
        initialPieChart.setLegendVisible(false);

        // Custom colors for the "Estimated" PieChart slices
        incomeData.getNode().setStyle("-fx-pie-color: #4CAF50;");  // Green for Income
        expenseData.getNode().setStyle("-fx-pie-color: #F44336;"); // Red for Expenses

        // Add savings goal below the estimated chart
        Label savingsGoalLabel = new Label("Savings Goal: $" + String.format("%.2f", budget.getGoalAmount()));
        savingsGoalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Wrap the "Estimated Budget" chart and label in a VBox
        VBox estimatedChartBox = new VBox(10, initialPieChart, savingsGoalLabel);
        estimatedChartBox.setAlignment(Pos.CENTER);

        // Second PieChart - Transactions (Dynamic Total Income vs Total Expenses after transactions)
        double totalIncome = 0;
        double totalExpenses = 0;

        // Calculate total income and expenses based on current transactions
        for (Transaction t : user.getTransactions()) {
            if (t.getType().equals("Income")) {
                totalIncome += t.getAmount();
            } else {
                totalExpenses += t.getAmount();
            }
        }

        PieChart.Data transactionIncomeData = new PieChart.Data("Income: $" + totalIncome, totalIncome);
        PieChart.Data transactionExpenseData = new PieChart.Data("Expenses: $" + totalExpenses, totalExpenses);

        PieChart transactionPieChart = new PieChart();
        transactionPieChart.getData().addAll(transactionIncomeData, transactionExpenseData);
        transactionPieChart.setTitle("Current Transactions");
        transactionPieChart.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");


        // Remove legend for the transactions PieChart
        transactionPieChart.setLegendVisible(false);

        // Custom colors for the "Transactions" PieChart slices
        transactionIncomeData.getNode().setStyle("-fx-pie-color: #3F51B5;");  // Blue for Transaction Income
        transactionExpenseData.getNode().setStyle("-fx-pie-color: #FF9800;"); // Orange for Transaction Expenses

        // Calculate balance
        double balance = totalIncome - totalExpenses;
        Label balanceLabel = new Label("Balance: $" + String.format("%.2f", balance));
        balanceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Wrap the "Current Transactions" chart and label in a VBox
        VBox currentChartBox = new VBox(10, transactionPieChart, balanceLabel);
        currentChartBox.setAlignment(Pos.CENTER);

        // Create the Back button and set its action to go back to the TransactionScreen
        Button backButton = new Button("Back to Transactions");
        backButton.setOnAction(e -> showTransactionScreen());

        // Use HBox to layout the PieCharts side by side
        HBox hbox = new HBox(30, estimatedChartBox, currentChartBox);  // Spacing between the two chart VBoxes
        hbox.setAlignment(Pos.CENTER);  // Center align the charts in the HBox

        // Use BorderPane for the layout
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(backButton);  // Set the back button to the top
        borderPane.setCenter(hbox);  // Set the HBox containing the charts in the center

        // Center the back button horizontally
        BorderPane.setAlignment(backButton, Pos.CENTER);

        // Return the scene with the pie charts and back button
        return new Scene(borderPane, 800, 500);  // Adjust the width and height as needed
    }

    private void showTransactionScreen() {
        // Create a new instance of the TransactionScreen and set it as the scene
        TransactionScreen transactionScreen = new TransactionScreen(stage, user, budget);
        stage.setScene(transactionScreen.createTransactionScene());
    }
}

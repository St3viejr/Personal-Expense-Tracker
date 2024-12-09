package Project_v2;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionScreen extends MainApp {

    private Stage stage;
    private User user;
    private Budget budget;
    private TableView<Transaction> transactionTable;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public TransactionScreen(Stage stage, User user, Budget budget) {
        this.stage = stage;
        this.user = user;
        this.budget = budget;
        this.transactionTable = new TableView<>();
    }

    @SuppressWarnings({ "unchecked", "unused" })
    public Scene createTransactionScene() {
        // Create a VBox to arrange components vertically
        VBox layout = new VBox(10);

        // Label for "Personal Expense Tracker"
        Label titleLabel = new Label("Personal Expense Tracker");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 20px;");

        // Create a HBox for both the title and username label
        HBox headerBox = new HBox(20);  // Spacing between the labels
        headerBox.setAlignment(Pos.CENTER_LEFT); // Align title to the left
        Label userNameLabel = new Label("@" + user.getUsername());
        userNameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        HBox.setHgrow(userNameLabel, Priority.ALWAYS);  // Allow the username label to grow on the right
        headerBox.getChildren().addAll(titleLabel, userNameLabel);  // Add both labels to the HBox

        // Create a Logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> logout());

        // Create UI components for transaction input
        TextField amountField = new TextField();
        TextField descriptionField = new TextField();
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("Income", "Expense");

        // Wrap the amount field in an HBox with a "$" label
        Label dollarLabel = new Label("$");
        HBox amountBox = new HBox(5, dollarLabel, amountField); // 5px spacing
        amountField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(amountField, Priority.ALWAYS);

        // Create buttons for Add and Remove Transaction
        Button addTransactionButton = new Button("Add Transaction");
        Button removeTransactionButton = new Button("Remove Selected Transaction");

        // Add functionality to the Remove Transaction button
        removeTransactionButton.setOnAction(e -> removeSelectedTransaction());

        // Create an HBox for Add and Remove Transaction buttons
        HBox buttonBox = new HBox(10, addTransactionButton, removeTransactionButton); // 10px spacing
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        // Table columns for displaying transactions
        TableColumn<Transaction, String> descriptionColumn = new TableColumn<>("Description");
        TableColumn<Transaction, Double> amountColumn = new TableColumn<>("Amount");
        TableColumn<Transaction, String> typeColumn = new TableColumn<>("Type");
        TableColumn<Transaction, String> dateColumn = new TableColumn<>("Date");
        TableColumn<Transaction, Double> balanceColumn = new TableColumn<>("Balance");

        // Define cell value factories for each column
        amountColumn.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getAmount()).asObject());
        descriptionColumn.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
        typeColumn.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType()));
        dateColumn.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(dateFormat.format(cellData.getValue().getDate()))); // Format date
        balanceColumn.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleDoubleProperty(calculateBalance(cellData.getValue())).asObject());

        // Add columns to the TableView
        transactionTable.getColumns().addAll(dateColumn, descriptionColumn, typeColumn, amountColumn, balanceColumn);

        // Populate TableView with transactions
        updateTransactionTable();

        // Button to view the Pie Chart
        Button viewChartButton = new Button("View Pie Chart");
        viewChartButton.setOnAction(e -> showChartScreen());

        // Add all components to the layout
        layout.getChildren().addAll(
            headerBox, // Add the header box containing both labels
            logoutButton,  // Add the logout button
            new Label("Description of Transaction:"), descriptionField,
            new Label("Amount:"), amountBox,  // Use the HBox containing the dollar sign and amount field
            new Label("Type:"), typeComboBox,
            buttonBox,  // Add the HBox containing Add and Remove buttons
            transactionTable,  // Add TableView to the layout
            viewChartButton  // Add Pie Chart button to the layout
        );

        // Add functionality to the "Add Transaction" button
        addTransactionButton.setOnAction(e -> {
            try {
                // Parse amount
                double amount = Double.parseDouble(amountField.getText());

                // Get description and type
                String description = descriptionField.getText();
                String type = typeComboBox.getValue();

                // Validate inputs
                if (description.isEmpty() || type == null) {
                    showAlert("Error", "Please provide a description and select a type.");
                    return;
                }

                // Create a new transaction
                Transaction transaction = new Transaction(
                    user.getTransactions().size() + 1, // Generate an ID
                    amount,
                    new Date(), // Use the current date
                    description,
                    type);

                // Add the transaction to the user's list
                user.addTransaction(transaction);

                // Update the budget
                if ("Income".equals(type)) {
                    budget.updateBudget(amount, 0); // Add income
                } else if ("Expense".equals(type)) {
                    budget.updateBudget(0, amount); // Add expense
                }

                // Refresh the table and clear input fields
                updateTransactionTable();
                amountField.clear();
                descriptionField.clear();
                typeComboBox.setValue(null);

            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter a valid number for the amount.");
            }
        });

        return new Scene(layout, 800, 500);
    }

    private double calculateBalance(Transaction transaction) {
        double balance = 0;
        for (Transaction t : user.getTransactions()) {
            balance += "Income".equals(t.getType()) ? t.getAmount() : -t.getAmount();
            if (transaction.getTransactionID() == t.getTransactionID()) {
                break; // Stop summing when reaching the current transaction
            }
        }
        return balance;
    }

    private void removeSelectedTransaction() {
        Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();

        if (selectedTransaction != null) {
            user.getTransactions().remove(selectedTransaction);
            updateTransactionTable();
        } else {
            showAlert("Error", "No transaction selected for removal.");
        }
    }

    private void updateTransactionTable() {
        transactionTable.getItems().setAll(user.getTransactions());
    }

    private void logout() {
        User.saveUserData(user, "user_data.ser");
        LoginScreen loginScreen = new LoginScreen(stage);
        stage.setScene(loginScreen.createLoginScene());
    }

    private void showChartScreen() {
        ChartScreen chartScreen = new ChartScreen(stage, user, budget);
        stage.setScene(chartScreen.createChartScene());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

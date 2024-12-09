package Project_v2;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class BudgetScreen extends MainApp {

    private Stage stage;
    private User user;

    public BudgetScreen(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
    }

    @SuppressWarnings("unused")
    public Scene createBudgetScene() {
        // Create input fields
        TextField incomeField = new TextField();
        TextField expenseField = new TextField();
        TextField goalField = new TextField();
        Button setBudgetButton = new Button("Continue");

        // Set button action
        setBudgetButton.setOnAction(e -> {
            try {
                double income = Double.parseDouble(incomeField.getText());
                double expense = Double.parseDouble(expenseField.getText());
                double goal = Double.parseDouble(goalField.getText());
                Budget budget = new Budget(1, goal);  // Assuming budgetID is 1
                budget.setBudget(income, expense, goal); // Pass income, expense, and goal
                showTransactionScreen(budget);
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter valid numbers.");
            }
        });

        // Create HBoxes for each input field with a "$" label
        HBox incomeBox = new HBox(5, new Label("$"), incomeField);
        HBox expenseBox = new HBox(5, new Label("$"), expenseField);
        HBox goalBox = new HBox(5, new Label("$"), goalField);

        // Label for "Personal Expense Tracker"
        Label titleLabel = new Label("Personal Expense Tracker");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 20px;");

        // Create the layout and add components
        VBox budgetLayout = new VBox(10);
        budgetLayout.getChildren().addAll(
            titleLabel, // Add the title label
            new Label("Estimated income per month:"), incomeBox,
            new Label("Estimated expenses per month:"), expenseBox,
            new Label("What is your Goal Amount to save per month?"), goalBox,
            setBudgetButton
        );

        return new Scene(budgetLayout, 800, 500);
    }

    private void showTransactionScreen(Budget budget) {
        TransactionScreen transactionScreen = new TransactionScreen(stage, user, budget);
        stage.setScene(transactionScreen.createTransactionScene());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
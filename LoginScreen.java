package Project_v2;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginScreen extends MainApp {

    private Stage stage;
    private User currentUser;

    public LoginScreen(Stage stage) {
        this.stage = stage;
        this.currentUser = new User(0, "", "");
    }

    @SuppressWarnings("unused")
    public Scene createLoginScene() {
        // Create the login screen UI
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button registerButton = new Button("Register");
        Button loginButton = new Button("Login");
        User loadedUser = User.loadUserData("user_data.ser");
        if (loadedUser != null) {
            currentUser = loadedUser;  // Set the loaded user data
        } else {
            currentUser = new User(1, "", "");  // Default user if no data exists
        }
    
        // Handle registration
        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (!username.isEmpty() && !password.isEmpty()) {
                currentUser.register(username, password);
                showAlert("Success", "Account created. Please log in.");
            } else {
                showAlert("Error", "Username and password cannot be empty.");
            }
        });

        // Handle login
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (currentUser.login(username, password)) {
                // Navigate to the budget screen after login
                showBudgetScreen();
            } else {
                showAlert("Login Failed", "Incorrect username or password.");
            }
        });

        // Label for "Personal Expense Tracker"
        Label titleLabel = new Label("Personal Expense Tracker");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 20px;");

        // Layout for login screen
        VBox loginLayout = new VBox(10);
        loginLayout.getChildren().addAll(
            titleLabel, // Add the title label
            new Label("Username:"), usernameField,
            new Label("Password:"), passwordField, registerButton, loginButton);

        return new Scene(loginLayout, 800, 500);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showBudgetScreen() {
        BudgetScreen budgetScreen = new BudgetScreen(stage, currentUser);
        stage.setScene(budgetScreen.createBudgetScene());
    }
}
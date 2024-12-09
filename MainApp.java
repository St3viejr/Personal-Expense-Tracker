package Project_v2;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainApp extends Application {

    private Stage primaryStage;
    private User currentUser;
    private Budget budget;
    private List<Transaction> transactions;

    public void start(Stage stage) {
        primaryStage = stage;

        // Initialize core components
        currentUser = new User(1, "", "");
        budget = new Budget(1, 0.0); // Initializing with default goal amount
        transactions = new ArrayList<>();

        // Show the Login screen
        LoginScreen loginScreen = new LoginScreen(stage);
        primaryStage.setScene(loginScreen.createLoginScene());
        primaryStage.show();
    }
    // subclass: User
    public class User {
    private int userID;
    private String username;
    private String password;
    private List<Transaction> transactions;  // Store list of transactions

    // Constructor
    public User(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.transactions = new ArrayList<>();
    }

    // Login method (checks if the credentials match)
    public boolean login(String inputUsername, String inputPassword) {
        return username.equals(inputUsername) && password.equals(inputPassword);
    }

    // Register method (set the username and password)
    public void register(String username, String password) {
        this.username = username;
        this.password = password;
        System.out.println("User registered successfully.");
    }

    // Add a transaction to the user's list of transactions
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        System.out.println("Transaction added: " + transaction.getDescription());
    }

    // Get the list of transactions
    public List<Transaction> getTransactions() {
        return transactions;
    }

    // Save the user data and transactions to a file
    public static void saveUserData(User user, String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(user);  // Save the user and transactions
            System.out.println("User data and transactions saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load the user data and transactions from a file
    public static User loadUserData(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (User) in.readObject();  // Load the user and transactions
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Getters and Setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

    // subclass: Budget
    public class Budget {
        private int budgetID;
        private double totalIncome;
        private double totalExpenses;
        private double goalAmount;

        public Budget(int budgetID, double goalAmount) {
            this.budgetID = budgetID;
            this.goalAmount = goalAmount;
            this.totalIncome = 0.0;
            this.totalExpenses = 0.0;
        }

        public void setBudget(double income, double expense, double goalAmount) {
            this.totalIncome = income;
            this.totalExpenses = expense;
            this.goalAmount = goalAmount;
        }

        public void updateBudget(double income, double expense) {
            this.totalIncome += income;
            this.totalExpenses += expense;
        }

        public double getNetGrowth() {
            return totalIncome - totalExpenses;
        }

        public int getBudgetID() {
            return budgetID;
        }

        public double getTotalIncome() {
            return totalIncome;
        }

        public double getTotalExpenses() {
            return totalExpenses;
        }

        public double getGoalAmount() {
            return goalAmount;
        }
    }

    // subclass: Transaction
    public class Transaction {
        private int transactionID;
        private double amount;
        private Date date;
        private String description;
        private String type;

        public Transaction(int transactionID, double amount, Date date, String description, String type) {
            this.transactionID = transactionID;
            this.amount = amount;
            this.date = date;
            this.description = description;
            this.type = type;
        }

        public void editTransaction(double amount, String description, String type) {
            this.amount = amount;
            this.description = description;
            this.type = type;
        }

        public int getTransactionID() {
            return transactionID;
        }

        public double getAmount() {
            return amount;
        }

        public Date getDate() {
            return date;
        }

        public String getDescription() {
            return description;
        }

        public String getType() {
            return type;
        }
    }

    public void addTransaction(double amount, Date date, String description, String type) {
        int transactionID = transactions.size() + 1;
        Transaction transaction = new Transaction(transactionID, amount, date, description, type);
        transactions.add(transaction);

        if ("Income".equals(type)) {
            budget.updateBudget(amount, 0);
        } else if ("Expense".equals(type)) {
            budget.updateBudget(0, amount);
        }
    }

    public void deleteTransaction(int transactionID) {
        transactions.removeIf(t -> t.getTransactionID() == transactionID);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Budget getBudget() {
        return budget;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public class Chart {
        private int chartID;
        private List<String> data;

        //Constructor
        public Chart(int chartID, List<String> data) {
            this.chartID = chartID;
            this.data = data;
    }

        //Methods
        public void generateChart() {
            System.out.println("Chart generated with data: " + data);
        }
        public void updateChart(List<String> newData) {
            this.data = newData;
            System.out.println("Chart updated");
        }
    
        //Getters and Setters
        public int getChartID() {
            return chartID;
        }
        public void setChartID(int chartID) {
            this.chartID = chartID;
        }

        public List<String> getData() {
            return data;
        }

        public void setData(List<String> data) {
            this.data = data;
        }
    }
}


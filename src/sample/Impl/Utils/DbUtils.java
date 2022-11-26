package sample.Impl.Utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import sample.ApplicationController;
import sample.Impl.AccountsImpl;
import sample.Impl.Entities.Account;
import sample.SettingsController;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class DbUtils {

    public static void changeScene(ActionEvent event, String fxmlFile, String title, Account account){

        Parent root = null;
        if(account.getName() != null && account.getPassword() != null){
            try {
                if (fxmlFile.equals("Views/Settings.fxml")){
                    FXMLLoader loader = new FXMLLoader(DbUtils.class.getResource(fxmlFile));
                    root = loader.load();
                    SettingsController Controller = loader.getController();
                    Controller.setoverview(account);
                }
                else {
                    FXMLLoader loader = new FXMLLoader(DbUtils.class.getResource(fxmlFile));
                    root = loader.load();
                    ApplicationController Controller = loader.getController();
                    Controller.setoverview(account);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                root = FXMLLoader.load(DbUtils.class.getResource(fxmlFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void signUpUser(ActionEvent event, String username, String password, String number, String email){
        Connection connection = null;
        PreparedStatement insert = null;
        PreparedStatement checkUserExists = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bank","postgres","rotimi");
            checkUserExists = connection.prepareStatement("SELECT * FROM Account WHERE username = ?");
            checkUserExists.setString(1,username);
            resultSet = checkUserExists.executeQuery();

            if (resultSet.isBeforeFirst()){
                System.out.println("user already exists");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are taken");
                alert.show();
            } else {
                insert = connection.prepareStatement("INSERT INTO account (id, username, password, number, email, balance,membership) VALUES (?,?,?,?,?,?,'BRONZE')");
                Account accounts = new AccountsImpl().setAccount(username, password, email, number);
                insert.setString(1, accounts.getCustomerId());
                insert.setString(2, accounts.getName());
                insert.setString(3, accounts.getPassword());
                insert.setString(4, accounts.getNumber());
                insert.setString(5,accounts.getEmail());
                insert.setInt(6,accounts.getBalance());
                insert.executeUpdate();
                changeScene(event,"Views/Login.fxml","Welcome", accounts);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (insert !=null){
                try {
                    insert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (checkUserExists != null){
                try {
                    checkUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void logInUser(ActionEvent event, String username, String password){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bank","postgres","rotimi");
            preparedStatement = connection.prepareStatement("SELECT password,number,email,balance,membership  FROM Account WHERE username =?");
            preparedStatement.setString(1,username);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()){
                System.out.println("user not found in the database");
                System.out.println(username);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are Incorrect!!!");
                alert.show();
            }else {
                while (resultSet.next()){
                    String retrievePassword = resultSet.getString("password");
                    String number = resultSet.getString("number");
                    String email = resultSet.getString("email");
                    int balance = resultSet.getInt("balance");
                    String membership = resultSet.getString("membership");
                    Account account = new AccountsImpl().getAccount(username,password,email,number,balance,membership);
                    if (retrievePassword.equals(password)){
                        changeScene(event,"Views/Application.fxml", "Application", account);
                    }else{
                        System.out.println("password did not match");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Provided credentials are Incorrect!!!");
                        alert.show();
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                   e.printStackTrace();
                }
            }
            if (preparedStatement !=null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void deposit(ActionEvent event, Account account, int balance, int deposit){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bank","postgres","rotimi");

            int amount = account.getBalance();
            int total = balance + amount;
            if (balance <= deposit){
                preparedStatement = connection.prepareStatement("UPDATE account SET balance = ? WHERE username =?");
                preparedStatement.setInt(1, total);
                preparedStatement.setString(2, account.getName());
                preparedStatement.executeUpdate();
                account.setBalance(total);
                saveTransaction(account.getName(), balance,"CREDIT");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("your account has been credited");
                alert.show();
                changeScene(event, "Views/Application.fxml", "Application", account);
            }
            else{
                System.out.println("your current membership does not permit this transfer");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("your current membership does not permit this transfer");
                alert.show();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (preparedStatement !=null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void withdraw(ActionEvent event, Account account, int balance, int withdraw, String membership){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bank","postgres","rotimi");

            int amount = account.getBalance();
            int total = amount - balance;
            if (total > 0 && balance <= withdraw){
                account.setBalance(total);
                preparedStatement = connection.prepareStatement("UPDATE account SET balance = ? WHERE username =?");
                preparedStatement.setInt(1, total);
                preparedStatement.setString(2, account.getName());
                preparedStatement.executeUpdate();
                saveTransaction(account.getName(), balance, "DEBIT");
                if (membership != null){
                    preparedStatement = connection.prepareStatement("UPDATE account SET membership = ? WHERE username =?");
                    preparedStatement.setString(1, membership);
                    preparedStatement.setString(2, account.getName());
                    preparedStatement.executeUpdate();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("your account details has been changed");
                    alert.show();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("your account has been debited");
                    alert.show();
                    changeScene(event, "Views/Application.fxml", "Application", account);
                }
            }
            else{
                System.out.println("your account cannot process this withdrawal");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("your account cannot process this withdrawal");
                alert.show();
                changeScene(event, "Views/Application.fxml", "Application", account);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (preparedStatement !=null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void updateUserName(ActionEvent event, Account account, String name){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bank","postgres","rotimi");

            if (name != null){
                preparedStatement = connection.prepareStatement("UPDATE account SET username = ? WHERE username =?");
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, account.getName());
                preparedStatement.executeUpdate();
                account.setName(name);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("your account details has been changed");
                alert.show();
                changeScene(event,"Views/Settings.fxml", "settings",account);
            }
            else{
                System.out.println("Invalid parameter was given");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Invalid parameter was given");
                alert.show();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (preparedStatement !=null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void saveTransaction( String username,int balance, String type){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bank","postgres","rotimi");

            preparedStatement = connection.prepareStatement("INSERT INTO transactions (id, username, amount,createddate, t_type) VALUES (?,?,?,?,?)");
            preparedStatement.setString(1, UUID.randomUUID().toString());
            preparedStatement.setString(2, username);
            preparedStatement.setInt(3, balance);
            preparedStatement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(5, type);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (preparedStatement !=null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

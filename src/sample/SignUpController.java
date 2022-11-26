package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sample.Impl.Utils.DbUtils;
import sample.Impl.Entities.Account;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML
    private Button btn_login;

    @FXML
    private Button btn_signup;

    @FXML
    private TextField tf_username;

    @FXML
    private TextField tf_password;

    @FXML
    private TextField tf_email;

    @FXML
    private TextField tf_number;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!tf_username.getText().trim().isEmpty() && !tf_password.getText().trim().isEmpty()){
                    DbUtils.signUpUser(actionEvent, tf_username.getText(), tf_password.getText(),tf_number.getText() ,tf_email.getText());
                }
                else {
                    System.out.println("Please fill information");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in information!!!");
                    alert.show();
                }
            }
        });

        btn_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DbUtils.changeScene(actionEvent, "Views/Login.fxml", "Log  in", new Account());
            }
        });

    }
}

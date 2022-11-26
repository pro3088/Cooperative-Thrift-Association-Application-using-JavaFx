package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.Impl.Enum.Membership;
import sample.Impl.Utils.DbUtils;
import sample.Impl.Entities.Account;

import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationController implements Initializable {

    @FXML
    Label lb_name;

    @FXML
    Label lb_balance;

    @FXML
    Label lb_membership;

    @FXML
    TextField tf_withdraw;

    @FXML
    TextField tf_deposit;

    @FXML
    Button btn_deposit;

    @FXML
    Button btn_withdraw;

    @FXML
    Button btn_logout;

    @FXML
    Button btn_settings;

    Account account;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DbUtils.changeScene(actionEvent, "Views/Login.fxml", "Log  in", new Account());
            }
        });

        btn_settings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DbUtils.changeScene(actionEvent, "Views/Settings.fxml", "Settings", account);
            }
        });

        btn_deposit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DbUtils.deposit(actionEvent,account,Integer.parseInt(tf_deposit.getText()),Membership.valueOf(account.getMembership()).getDeposit());
            }
        });

        btn_withdraw.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DbUtils.withdraw(actionEvent,account,Integer.parseInt(tf_withdraw.getText()), Membership.valueOf(account.getMembership()).getWithdraw(),null);
            }
        });
    }

    public void setoverview(Account account){
        lb_name.setText(account.getName());
        lb_balance.setText("N " + account.getBalance());
        lb_membership.setText(account.getMembership() + " MEMBERSHIP");
        this.account = account;
    }

}

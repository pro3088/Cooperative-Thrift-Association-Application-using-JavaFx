package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sample.Impl.Entities.Account;
import sample.Impl.Enum.Membership;
import sample.Impl.Utils.DbUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML
    Label lb_name;

    @FXML
    Label lb_balance;

    @FXML
    TextField tf_name;

    @FXML
    Label lb_membership;

    @FXML
    Label lb_membershipT;

    @FXML
    Label lb_amount;

    @FXML
    Button btn_deposit;

    @FXML
    Button btn_changeName;

    @FXML
    Button btn_back;

    @FXML
    RadioButton rb_platinum;

    @FXML
    RadioButton rb_silver;

    @FXML
    RadioButton rb_gold;

    Account account;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup toggleGroup = new ToggleGroup();
        rb_gold.setToggleGroup(toggleGroup);
        rb_platinum.setToggleGroup(toggleGroup);
        rb_silver.setToggleGroup(toggleGroup);

        rb_platinum.setSelected(true);

        rb_platinum.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                lb_amount.setText(String.valueOf(Membership.valueOf("PLATINUM").getPay()));
            }
        });

        rb_silver.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                lb_amount.setText(String.valueOf(Membership.valueOf("SILVER").getPay()));
            }
        });

        rb_gold.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                lb_amount.setText(String.valueOf(Membership.valueOf("GOLD").getPay()));
            }
        });

        btn_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DbUtils.changeScene(actionEvent, "Views/Application.fxml", "Log  in", account);
            }
        });

        btn_changeName.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DbUtils.updateUserName(actionEvent, account, tf_name.getText());
            }
        });

        btn_deposit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String toggleName =((RadioButton) toggleGroup.getSelectedToggle()).getText();
                System.out.println(toggleName);
                int amount = Membership.valueOf(toggleName).getPay();
                DbUtils.withdraw(actionEvent,account,amount,100000000,toggleName);
                DbUtils.changeScene(actionEvent, "Views/Application.fxml", "Log  in", account);
                lb_membership.setText(account.getMembership() + " MEMBERSHIP");
                lb_membershipT.setText(account.getMembership() + " MEMBERSHIP");
            }
        });
    }

    public void setoverview(Account account){
        lb_name.setText(account.getName());
        lb_balance.setText("N " + account.getBalance());
        lb_membership.setText(account.getMembership() + " MEMBERSHIP");
        lb_membershipT.setText(account.getMembership() + " MEMBERSHIP");

        lb_amount.setText(String.valueOf(Membership.valueOf("PLATINUM").getPay()));

        switch (account.getMembership()) {
            case "PLATINUM":
                rb_platinum.setDisable(true);
                rb_silver.setSelected(true);
                lb_amount.setText(String.valueOf(Membership.valueOf("GOLD").getPay()));
                break;
            case "SILVER":
                rb_silver.setDisable(true);
                rb_platinum.setDisable(true);
                rb_gold.setSelected(true);
                lb_amount.setText(String.valueOf(Membership.valueOf("GOLD").getPay()));
                break;
            case "GOLD":
                rb_silver.setDisable(true);
                rb_platinum.setDisable(true);
                rb_gold.setDisable(true);
                btn_deposit.setDisable(true);
                lb_amount.setText("you are currently maxed out");
                break;
        }

        this.account = account;
    }

}

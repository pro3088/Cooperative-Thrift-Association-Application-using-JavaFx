package sample.Impl;

import sample.Impl.Entities.Account;

import java.util.UUID;

public class AccountsImpl {
    public Account setAccount(String username, String password, String email, String number){
        Account account = new Account();
        account.setCustomerId(UUID.randomUUID().toString());
        account.setEmail(email);
        account.setName(username);
        account.setPassword(password);
        account.setNumber(number);
        account.setBalance(1000);
        account.setMembership("BRONZE");
        return account;
    }

    public Account getAccount(String username, String password, String email, String number,int balance, String membership){
        Account account = new Account();
        account.setEmail(email);
        account.setName(username);
        account.setPassword(password);
        account.setNumber(number);
        account.setBalance(balance);
        account.setMembership(membership);
        return account;
    }

}

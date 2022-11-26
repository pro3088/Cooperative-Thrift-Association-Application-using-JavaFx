package sample.Impl.Enum;

public enum Membership {
    GOLD(800000,1500000,900000),
    PLATINUM(300000,400000,200000),
    SILVER(500000,800000,700000),
    BRONZE(0,150000,50000);

    private int pay, deposit, withdraw;

    Membership(int pay, int deposit, int withdraw) {
        this.pay = pay;
        this.deposit = deposit;
        this.withdraw = withdraw;
    }

    public int getPay() {
        return pay;
    }

    public int getDeposit() {
        return deposit;
    }

    public int getWithdraw() {
        return withdraw;
    }
}

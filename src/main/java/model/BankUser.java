package model;

public class BankUser {
    private String firstName;
    private String secondName;
    private String pesel;
    private String login;
    private String password;
    private double money;
    private String accountNumber;

    public BankUser(String firstName, String secondName, String pesel, String login, String password, double money, String accountNumber) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.pesel = pesel;
        this.login = login;
        this.password = password;
        this.money = money;
        this.accountNumber = accountNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public String toString() {
        return firstName + ";" + secondName + ";" + pesel + ";" + login + ";" + password + ";" + money + ";" + accountNumber;
    }

}

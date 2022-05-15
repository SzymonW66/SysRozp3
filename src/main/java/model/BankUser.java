package model;

public class BankUser {
    private String firstName;
    private String secondName;
    private String pesel;
    private final String login;
    private final String password;
    private final double money;
    private final long accountNumber;

    public BankUser(String firstName, String secondName, String pesel, String login, String password, double money, long accountNumber) {
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

    @Override
    public String toString() {
        return "BankUser{" +
                "firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", pesel='" + pesel + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", money=" + money +
                ", accountNumber=" + accountNumber +
                '}';
    }
}

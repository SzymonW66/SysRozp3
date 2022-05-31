package bank;

import database.FileManager;
import model.Administrator;
import model.BankUser;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;


public class EchoServerThread implements Runnable {
    protected Socket socket;

    public EchoServerThread(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {
        BufferedReader brinp = null;
        FileManager fileManager = new FileManager();
        ArrayList<BankUser> bankUsers = fileManager.loadBankUsersFromFile();
        Administrator administrator = new Administrator();
        DataOutputStream out = null;
        String threadName = Thread.currentThread().getName();

        try {
            brinp = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()
                    )
            );
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println(threadName + "| Błąd przy tworzeniu strumieni " + e);
            return;
        }


        //główna pętla
        while (true) {
            try {
                String starterMessage = brinp.readLine();
                System.out.println(starterMessage);
                String connected = "Zostałeś połączony poprawnie";
                out.writeBytes(connected + "\n");

            if (starterMessage.equals("Client") ) {
                try {
                    System.out.println("Rozpoczynam logowanie");
                    //odczytanie podanych lini przez serwer dla loginu i hasła
                    String resultTrue = "true";
                    String resultFalse = "false";
                    String login = brinp.readLine();
                    System.out.println("Odczytano login: " + login);
                    String password = brinp.readLine();
                    System.out.println("Odczytano hasło: " + password);
                    String line = login + ";" + password;
                    System.out.println(line);

                    int countTrue = 0;
                    int countFalse = 0;
//problem z wysłaniem wiadomości co przejście pętli
                        for (BankUser users : bankUsers) {
                            if (line.equals(users.getLogin() + ";" + users.getPassword())) {
                                countTrue ++;
                              //  out.writeBytes(resultTrue + "\n\r");
                              //  out.flush();
                              //  System.out.println("Wysłano linię: " + resultTrue);
                             //   break;
                            } else {
                                countFalse ++;
                                //  out.writeBytes(resultFalse + "\n\r");
                              //  out.flush();
                              //  System.out.println("Wysłano linię: " + resultFalse);
                            //    socket.close();
                            //    System.exit(0);
                            }
                        }
                        if(countTrue == 1) {
                            out.writeBytes(resultTrue + "\n\r");
                              out.flush();
                              System.out.println("Wysłano linię: " + resultTrue);
                        }
                        else
                        {
                            out.writeBytes(resultFalse + "\n\r");
                        }


                    System.out.println("Pętla true/false się zakończyła, rozpoczynam głóną pętlę");

                    String downloadString = null;
                    do {
                        downloadString = brinp.readLine();
                        System.out.println("Wybrano numer " + downloadString);

                        switch (downloadString) {
                            case "1":

                                System.out.println("Wybrano opcję wypłaty");
                                String money3 = brinp.readLine();
                                if (money3.length() == 0) {
                                    money3 = brinp.readLine();
                                }
                                double moneyCheck1 = Double.parseDouble(money3);
                                if (moneyCheck1 > 0) {
                                    boolean info = withdrawMoney(line, money3, bankUsers);
                                    if (info == true) {

                                        out.writeBytes("True" + "\n\r");
                                        System.out.println("Zostało wysłane True");
                                        out.flush();
                                        break;
                                    } else {
                                        System.out.println("Za mało pieniędzy na koncie");
                                        out.writeBytes("Za mało pieniędzy na koncie " + "\n\r");
                                        out.flush();
                                        break;
                                    }
                                } else
                                    System.out.println("Podano ujemną wartość");
                                out.writeBytes("Podano ujemną wartość piniędzy, niepowodzenie" + "\n\r");
                                out.flush();
                                break;


                            case "2":
                                String message = "Wpłacono";
                                String message6 = "Niepowodzenie, przy wpłacie. Podano ujemną wartość ";
                                System.out.println("Wybrano opcje wpłaty");
                                String money = brinp.readLine();

                                if (money.length() == 0)
                                    money = brinp.readLine();
                                double moneyCheck = Double.parseDouble(money);
                                if (moneyCheck > 0) {
                                    depositMoney(line, money, bankUsers);
                                    System.out.println("Pieniądze zostały wpłacone wysyłam wiadomość");
                                    out.writeBytes(message + "\n\r");
                                    break;
                                } else
                                    System.out.println("Pieniądze nie zostały wpłacone wysyłam wiadomość");
                                out.writeBytes(message6);


                            case "3":
                                System.out.println("Wybrano opcję przelewu");
                                String money1 = brinp.readLine();
                                if (money1.length() == 0)
                                    money1 = brinp.readLine();
                                String accountNumberOfRecipent = brinp.readLine();
                                if (accountNumberOfRecipent.length() == 0)
                                    accountNumberOfRecipent = brinp.readLine();

                                String message1 = transferMoney(line, money1, accountNumberOfRecipent, bankUsers);

                                if (message1 == "Sukces") {
                                    System.out.println("Przelew wykonano pomyślnie");
                                    out.writeBytes("True" + "\n\r");
                                    out.flush();
                                    break;
                                } else if (message1 == "Za mało pieniędzy na koncie") {
                                    System.out.println("Za mało pieniędzy na koncie, aby wykonać przelew");
                                    out.writeBytes("Za mało pieniędzy na koncie" + "\n\r");
                                    out.flush();
                                    break;
                                } else if (message1 == "Minus money") {
                                    System.out.println("Podano ujemną wartość piniędzy do przlania");
                                    out.writeBytes("Minus money" + "\n\r");
                                    out.flush();
                                    break;
                                } else if (message1 == "Your number") {
                                    System.out.println("Podano swój numer konta");
                                    out.writeBytes("Your number" + "\n\r");
                                    out.flush();
                                    break;
                                }

                            case "4":
                                ArrayList<BankUser> bankUsers1 = fileManager.loadBankUsersFromFile();

                                System.out.println("Wybrano opcję sprawdzenia stanu konta");
                                String message2 = checkAccount(line, bankUsers1);
                                System.out.println(message2);
                                out.writeBytes(message2 + "\n\r");
                                out.flush();
                                System.out.println("Informacja o koncie została wysłana " + message2);
                                break;
                            case "5":
                                socket.close();
                                System.out.println("Wylogowano");
                                //TODO dodać komende do rozłączenia z serwerem
                                break;
                        }
                    } while (downloadString != "5");

                } catch (IOException e) {

                    System.out.println("Błąd wejścia-wyjścia: " + e);
                }

            } else if (starterMessage.equals("Admin")) {
                try {
                    //odczytanie podanych lini przez serwer dla loginu i hasła
                    String resultTrue = "true";
                    String resultFalse = "false";
                    String login = brinp.readLine();
                    System.out.println("Odczytano login: " + login);
                    String password = brinp.readLine();
                    System.out.println("Odczytano hasło: " + password);

                    if (password.equals(administrator.getPassword()) && login.equals(administrator.getLogin())){
                        out.writeBytes(resultTrue + "\n\r");
                        out.flush();
                        System.out.println("Wysłano linię " + resultTrue);
                    }
                    else {
                        out.writeBytes(resultFalse + "\n\r");
                        out.flush();
                        System.out.println("Wysłano linie " + resultFalse);
                    }

                    System.out.println("Pętla true/false się zakończyła, rozpoczynam głóną pętlę");

                    String downloadString = null;
                    do {
                        downloadString = brinp.readLine();
                        System.out.println("Wybrano numer " + downloadString);

                        switch (downloadString) {
                            case "1":
                                System.out.println("Wybrano nr 1 - dodanie nowego użytkownika");
                                String name1 = brinp.readLine();
                                if (name1.length() == 0)
                                    name1 = brinp.readLine();
                                System.out.println(name1);
                                String surname = brinp.readLine();
                                if (surname.length() == 0)
                                    surname = brinp.readLine();
                                System.out.println(surname);
                                String pesel1 = brinp.readLine();
                                if (pesel1.length() == 0)
                                    pesel1 = brinp.readLine();
                                System.out.println(pesel1);
                                String login1 = brinp.readLine();
                                if(login1.length() == 0)
                                    login1 = brinp.readLine();
                                System.out.println(login1);
                                String password1 = brinp.readLine();
                                if (password1.length() == 0)
                                    password1 = brinp.readLine();
                                System.out.println(password1);

                                String message1 = addNewUser(name1, surname, pesel1, login1, password1, bankUsers);

                                if (message1.equals("True")){
                                    System.out.println("Operacja się udała, wysyłam sukces");
                                    out.writeBytes("True" + "\n\r");
                                    out.flush();
                                    break;
                                }
                                else if (message1.equals("False")) {
                                    System.out.println("Coś poszło nie tak");
                                    out.writeBytes("False" + "\n\r");
                                    out.flush();
                                    break;
                                }
                                //metoda dodająca użytkownika

                            case "2":
                                System.out.println("Wybrano nr 2 - edycja imienia");
                                String accountNumberOfRecipent = brinp.readLine();
                                if (accountNumberOfRecipent.length() == 0)
                                    accountNumberOfRecipent = brinp.readLine();
                                String newName = brinp.readLine();
                                if(newName.length() == 0) {
                                    newName = brinp.readLine();
                                }
                                String message2 = changeNameByAdmin(newName, accountNumberOfRecipent, bankUsers);
                                if (message2.equals("Sukces")){
                                    System.out.println("Operacja się udała, wysyłam sukces");
                                    out.writeBytes("Sukces" + "\n\r");
                                    out.flush();
                                    break;
                                }
                                else if (message2.equals("Puste pole")){
                                    out.writeBytes("Puste pole" + "\n\r");
                                    out.flush();
                                    break;
                                }
                                else if (message2.equals("Nie znaleziono")){
                                    out.writeBytes("Nie znaleziono" + "\n\r");
                                    out.flush();
                                    break;
                                }
                            case "3":
                                System.out.println("Wybrano nr 3 - edycja nazwiska");
                                String accountNumberOfRecipent2 = brinp.readLine();
                                if (accountNumberOfRecipent2.length() == 0)
                                    accountNumberOfRecipent2 = brinp.readLine();
                                String newSurname = brinp.readLine();
                                if(newSurname.length() == 0) {
                                    newSurname = brinp.readLine();
                                }
                                String message3 = changeSurnameByAdmin(newSurname, accountNumberOfRecipent2, bankUsers);

                                if (message3.equals("Sukces")){
                                    System.out.println("Operacja się udała, wysyłam sukces");
                                    out.writeBytes("Sukces" + "\n\r");
                                    out.flush();
                                    break;
                                }
                                else if (message3.equals("Puste pole")){
                                    out.writeBytes("Puste pole" + "\n\r");
                                    out.flush();
                                    break;
                                }
                                else if (message3.equals("Nie znaleziono")){
                                    out.writeBytes("Nie znaleziono" + "\n\r");
                                    out.flush();
                                    break;
                                }
                            case "4":
                                System.out.println("Wybrano nr 4 - edycja PESEL");
                                String accountNumberOfRecipent3 = brinp.readLine();
                                if (accountNumberOfRecipent3.length() == 0)
                                    accountNumberOfRecipent3 = brinp.readLine();
                                String newPESEL = brinp.readLine();
                                if(newPESEL.length() == 0) {
                                    newPESEL = brinp.readLine();
                                }
                                String message4 = changePESELByAdmin(newPESEL, accountNumberOfRecipent3, bankUsers);

                                if (message4.equals("Sukces")){
                                    System.out.println("Operacja się udała, wysyłam sukces");
                                    out.writeBytes("Sukces" + "\n\r");
                                    out.flush();
                                    break;
                                }
                                else if (message4.equals("Puste pole")){
                                    out.writeBytes("Puste pole" + "\n\r");
                                    out.flush();
                                    break;
                                }
                                else if (message4.equals("Nie znaleziono")){
                                    out.writeBytes("Nie znaleziono" + "\n\r");
                                    out.flush();
                                    break;
                                }
                            case "5":
                        }


                    } while (downloadString != "5");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static String addNewUser(String name, String lastName, String pesel, String login, String password, ArrayList<BankUser> bankUsers){
        FileManager fileManager = new FileManager();
        if (name != null || lastName != null || pesel != null || login != null || password != null) {
        double money = 0;
        String newAccount = generateNewAccountNumber();
        BankUser bankUser = new BankUser(name, lastName, pesel, login, password, money, newAccount);
        bankUsers.add(bankUser);
        fileManager.saveBankUsersToFile(bankUsers);
        return "True";
        }
        else
            return "False";
    }

    static void depositMoney(String line, String money, ArrayList<BankUser> bankUsers) {
        FileManager fileManager = new FileManager();
        BankUser currentPerson = findPerson(line, bankUsers);
        assert currentPerson != null;
        double money1 = Double.parseDouble(money);
        currentPerson.setMoney(currentPerson.getMoney() + money1);
        int index = bankUsers.indexOf(currentPerson);
        bankUsers.set(index, currentPerson);
        fileManager.saveBankUsersToFile(bankUsers);
    }

    static boolean withdrawMoney(String line, String money, ArrayList<BankUser> bankUsers) {
        FileManager fileManager = new FileManager();
        BankUser currentPerson = findPerson(line, bankUsers);
        assert currentPerson != null;
        double money1 = Double.parseDouble(money);
        if (money1 > 0) {
            if (currentPerson.getMoney() >= money1) {
                currentPerson.setMoney(currentPerson.getMoney() - money1);
                int index = bankUsers.indexOf(currentPerson);
                bankUsers.set(index, currentPerson);
                fileManager.saveBankUsersToFile(bankUsers);
                System.out.println("Zwracam true");
                return true;
            } else {
                System.out.println("Nie zostało wykonane poprawnie, nie posiadasz takich pieniedzy na koncie");
                return false;
            }
        } else
            return false;
    }

    static String transferMoney(String line, String money, String accountNumber, ArrayList<BankUser> bankUsers) {
        String messeageSucces = "Sukces";
        String yourNumber = "Your number";
        String minusMoney = "Minus money";
        String tooLowAmount = "Za mało pieniędzy na koncie";
        FileManager fileManager = new FileManager();
        BankUser currentPerson = findPerson(line, bankUsers);
        BankUser transferRecipient = findPersonByNumberAccount(accountNumber, bankUsers);
        assert currentPerson != null;
        assert transferRecipient != null;
        double money1 = Double.parseDouble(money);
        if (!Objects.equals(accountNumber, currentPerson.getAccountNumber())) {
            if (money1 > 0) {
                if (currentPerson.getMoney() >= money1) {
                    currentPerson.setMoney(currentPerson.getMoney() - money1);
                    transferRecipient.setMoney(transferRecipient.getMoney() + money1);
                    int index = bankUsers.indexOf(currentPerson);
                    int index1 = bankUsers.indexOf(transferRecipient);
                    bankUsers.set(index, currentPerson);
                    bankUsers.set(index1, transferRecipient);
                    fileManager.saveBankUsersToFile(bankUsers);
                    bankUsers = new ArrayList<BankUser> ();
                    System.out.println("Zwracam Sukces");
                    return messeageSucces;
                } else
                    return tooLowAmount;
            } else
                return minusMoney;
        } else
            return yourNumber;
    }

    static String changeNameByAdmin (String newName, String account, ArrayList<BankUser> bankUsers){
        FileManager fileManager = new FileManager();
        String isEmpty = "Puste pole";
        String noAccount = "Nie znaleziono";
        String sukcess = "Sukces";
        BankUser currentPerson = findPersonByNumberAccount(account, bankUsers);
        assert currentPerson != null;
        if (newName != null){
            if (currentPerson != null) {
                currentPerson.setFirstName(newName);
                int index = bankUsers.indexOf(currentPerson);
                bankUsers.set(index, currentPerson);
                fileManager.saveBankUsersToFile(bankUsers);
                return sukcess;
            }
            else {
              return noAccount;
            }
        }
        else {
            return isEmpty;
        }
    }

    static String changeSurnameByAdmin (String newSurname, String account, ArrayList<BankUser> bankUsers){
        FileManager fileManager = new FileManager();
        String isEmpty = "Puste pole";
        String noAccount = "Nie znaleziono";
        String sukcess = "Sukces";
        BankUser currentPerson = findPersonByNumberAccount(account, bankUsers);
        assert currentPerson != null;
        if (newSurname != null){
            if (currentPerson != null) {
                currentPerson.setSecondName(newSurname);
                int index = bankUsers.indexOf(currentPerson);
                bankUsers.set(index, currentPerson);
                fileManager.saveBankUsersToFile(bankUsers);
                return sukcess;
            }
            else {
                return noAccount;
            }
        }
        else {
            return isEmpty;
        }
    }

    static String changePESELByAdmin (String newPESEL, String account, ArrayList<BankUser> bankUsers){
        FileManager fileManager = new FileManager();
        String isEmpty = "Puste pole";
        String noAccount = "Nie znaleziono";
        String sukcess = "Sukces";
        BankUser currentPerson = findPersonByNumberAccount(account, bankUsers);
        assert currentPerson != null;
        if (newPESEL != null){
            if (currentPerson != null) {
                currentPerson.setSecondName(newPESEL);
                int index = bankUsers.indexOf(currentPerson);
                bankUsers.set(index, currentPerson);
                fileManager.saveBankUsersToFile(bankUsers);
                return sukcess;
            }
            else {
                return noAccount;
            }
        }
        else {
            return isEmpty;
        }
    }

    static String checkAccount(String line, ArrayList<BankUser> bankUsers) {
        String info = null;
        BankUser currentPerson = findPerson(line, bankUsers);
        assert currentPerson != null;
        info = currentPerson.toString();
        return info;
    }

    static String generateNewAccountNumber(){
        Random random = new Random();
        int pin = random.nextInt(999999999) - 99999999;
        return pin + "";
    }

    static BankUser findPersonByNumberAccount(String lookingFor, ArrayList<BankUser> bankUsers) {
        BankUser bankUser = null;
        for (BankUser users : bankUsers) {
            if (users.getAccountNumber().equals(lookingFor)) {
                bankUser = users;
            }
        }
        return bankUser;
    }

    static BankUser findPerson(String lookingFor, ArrayList<BankUser> bankUsers) {
        BankUser bankUser = null;
        for (BankUser users : bankUsers) {
            if ((users.getLogin() + ";" + users.getPassword()).equals(lookingFor)) {
                bankUser = users;
            }
        }
        return bankUser;
    }

}
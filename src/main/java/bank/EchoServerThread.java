package bank;

import database.FileManager;
import model.BankUser;

import java.io.*;
import java.net.*;
import java.util.ArrayList;


public class EchoServerThread implements Runnable {
    protected Socket socket;

    public EchoServerThread(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {
        BufferedReader brinp = null;
        FileManager fileManager = new FileManager();
        ArrayList<BankUser> bankUsers = fileManager.loadBankUsersFromFile();
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
                //odczytanie podanych lini przez serwer dla loginu i hasła
                String resultTrue = "true";
                String resultFalse = "false";
                String login = brinp.readLine();
                System.out.println("Odczytano login: " + login);
                String password = brinp.readLine();
                System.out.println("Odczytano hasło: " + password);
                String line = login + ";" + password;
                System.out.println(line);

                for (BankUser users : bankUsers) {
                    if (line.equals(users.getLogin() + ";" + users.getPassword())) {
                        out.writeBytes(resultTrue + "\n\r");
                        out.flush();
                        System.out.println("Wysłano linię: " + resultTrue);
                        break;
                    } else {
                        out.writeBytes(resultFalse + "\n\r");
                        out.flush();
                        System.out.println("Wysłano linię: " + resultFalse);
                    }
                }
                System.out.println("Pętla true/false się zakończyła, rozpoczynam głóną pętlę");

                String downloadString = null;
                do {
                    downloadString = brinp.readLine();
                    System.out.println("Wybrano numer " + downloadString);

                    switch (downloadString) {
                        case "1":
                            System.out.println("Wybrano opcję wypłaty");
                            double money3 = Double.parseDouble(brinp.readLine());
                            String message3 = withdrawMoney(line, money3, bankUsers);
                            out.writeBytes(message3 + "\n\r");
                            out.flush();
                            break;
                        case "2":
                            System.out.println("Wybrano opcje wpłaty");
                            double money = Double.parseDouble(brinp.readLine());
                            String message = depositMoney(line, money, bankUsers);
                            out.writeBytes(message + "\n\r");
                            break;
                        case "3":
                            System.out.println("Wybrano opcję przelewu");
                            double money1 = Double.parseDouble(brinp.readLine());
                            long accountNumber = Long.parseLong(brinp.readLine());
                            String message1 = transferMoney(line, money1, accountNumber, bankUsers);
                            out.writeBytes(message1 + "\n\r");
                            break;
                        case "4":
                            //TODO Tutaj za drugim razem daje informacje do client
                            System.out.println("Wybrano opcję sprawdzenia stanu konta");
                            String message2 = checkAccount(line, bankUsers);
                            out.writeBytes(message2 + "\n");
                            out.flush();
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
        }
    }

    static String depositMoney(String line, double money, ArrayList<BankUser> bankUsers) {
        BankUser currentPerson = findPerson(line, bankUsers);
        assert currentPerson != null;
        currentPerson.setMoney(currentPerson.getMoney() + money);
        int index = bankUsers.indexOf(currentPerson);
        bankUsers.set(index, currentPerson);
        //   bankUsers.
//zapisać nowego usera do Stringa i dodać do pliku txt


        return "Pieniądze zostały wpłacone, saldo zwiększyło się o " + money + " zł";
    }

    static String withdrawMoney(String line, double money, ArrayList<BankUser> bankUsers) {
        BankUser currentPerson = findPerson(line, bankUsers);
        assert currentPerson != null;
        currentPerson.setMoney(currentPerson.getMoney() - money);
        int index = bankUsers.indexOf(currentPerson);
        bankUsers.set(index, currentPerson);
//zapisać nowego usera do Stringa i dodać do pliku txt
        return "Pieniądze zostały przelane, saldo zwiększyło się o " + money + " zł";
    }

    static String transferMoney(String line, double money, long accountNumber, ArrayList<BankUser> bankUsers) {
        BankUser currentPerson = findPerson(line, bankUsers);
        assert currentPerson != null;
        for (BankUser users : bankUsers) {
            if (users.getAccountNumber() == (accountNumber)) {
                currentPerson.setMoney(currentPerson.getMoney() - money);
                //dokończyć te metody
            }
        }
        currentPerson.setMoney(currentPerson.getMoney() - money);
        int index = bankUsers.indexOf(currentPerson);
        bankUsers.set(index, currentPerson);

        return "Pieniądze zostały przelane, saldo zwiększyło się o " + money + " zł";
    }

    static String checkAccount(String line, ArrayList<BankUser> bankUsers) {
        BankUser currentPerson = findPerson(line, bankUsers);
        assert currentPerson != null;
        return currentPerson.toString();
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
    //TODO zrobić metodę do zapisu danych po edycji do pliku - Kuba
    //TODO dokończyć metody edycji danych - Szymon i Kuba
    //TODO analogicznie zrobić panel administratora w panelu tym będzie potrzebna metoda do generowania unikalnego numeru konta, a także kolejny plik który będzie zczytywał dane administratora - Szymon

    //serwer na pare kilentów zrobiony
    //switche działają
    //logowanie działa


}
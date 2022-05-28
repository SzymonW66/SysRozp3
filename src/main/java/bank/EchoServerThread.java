package bank;

import database.FileManager;
import model.BankUser;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Objects;


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
                            //  String message3 = "Wypłacono";
                            System.out.println("Wybrano opcję wypłaty");
                            String money3 = brinp.readLine();
                            if (money3.length() == 0) {
                                money3 = brinp.readLine();
                            }
                            boolean info = withdrawMoney(line, money3, bankUsers);
                            if (info == true) {
                                System.out.println("pierwszy warunek " + withdrawMoney(line, money3, bankUsers));
                                out.writeBytes("True" + "\n\r");
                                System.out.println("Zostało wysłane True");
                                out.flush();
                                break;
                            } else {
                                System.out.println("Za mało pieniędzy na koncie");
                                out.writeBytes("False" + "\n\r");
                                out.flush();
                                break;
                            }

                        case "2":
                            String message = "Wpłacono";
                            System.out.println("Wybrano opcje wpłaty");
                            String money = brinp.readLine();
                            if (money.length() == 0)
                                money = brinp.readLine();
                            depositMoney(line, money, bankUsers);
                            System.out.println("Pieniądze zostały wpłacone wysyłam wiadomość");
                            out.writeBytes(message + "\n\r");
                            break;
                        case "3":
                            System.out.println("Wybrano opcję przelewu");
                            String money1 = brinp.readLine();
                            if (money1.length() == 0)
                                money1 = brinp.readLine();
                            String accountNumberOfRecipent = brinp.readLine();
                            if (accountNumberOfRecipent.length() == 0)
                                accountNumberOfRecipent = brinp.readLine();

                            boolean message1 = transferMoney(line, money1, accountNumberOfRecipent, bankUsers);

                            if (message1 == true) {
                                System.out.println("Przelew wykonano pomyślnie");
                                out.writeBytes("True" + "\n\r");
                                out.flush();
                                break;

                            } else
                                System.out.println("Przelew nie został wykonany pomyślnie, sprawdź numer konta i czy wpisałeś ilość pieniedzy bez poprzedającego go znaku minus");
                            out.writeBytes("False" + "\n\r");
                            out.flush();
                            break;


                        case "4":
                            System.out.println("Wybrano opcję sprawdzenia stanu konta");
                            String message2 = checkAccount(line, bankUsers);
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
        }
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
        if (money1 > 0){
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
        }

else
    return false;
    }

    static boolean transferMoney(String line, String money, String accountNumber, ArrayList<BankUser> bankUsers) {
        FileManager fileManager = new FileManager();
        BankUser currentPerson = findPerson(line, bankUsers);
        BankUser transferRecipient = findPersonByNumberAccount(accountNumber, bankUsers);
        assert currentPerson != null;
        assert transferRecipient != null;
        double money1 = Double.parseDouble(money);
        if(!Objects.equals(accountNumber, currentPerson.getAccountNumber())){
            if (money1 > 0) {
                if (currentPerson.getMoney() >= money1) {
                    currentPerson.setMoney(currentPerson.getMoney() - money1);
                    transferRecipient.setMoney(transferRecipient.getMoney() + money1);
                    int index = bankUsers.indexOf(currentPerson);
                    int index1 = bankUsers.indexOf(transferRecipient);
                    bankUsers.set(index, currentPerson);
                    bankUsers.set(index1, transferRecipient);
                    fileManager.saveBankUsersToFile(bankUsers);
                    System.out.println("Zwracam true");
                    return true;
                } else
                    return false;
            }
            else
                return false;
        }
        else
            return false;
    }
// zastanowić się nad zmianą typu na String i obsługiwać potem w kliencie to co się tam odjaniepawla
 //   static boolean doesExist(String lookingFor,)

    static String checkAccount(String line, ArrayList<BankUser> bankUsers) {
        String info = null;
        BankUser currentPerson = findPerson(line, bankUsers);
        assert currentPerson != null;
        info = currentPerson.toString();
        return info;
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
    //TODO GŁÓWNE
    //TODO dokończyć metody edycji danych - Szymon i Kuba
    //TODO analogicznie zrobić panel administratora w panelu tym będzie potrzebna metoda do generowania unikalnego numeru konta, a także kolejny plik który będzie zczytywał dane administratora - Szymon

    //TODO POBOCZNE

    //TODO zrobić case 3 (przelew, naprawić wszystko pod String pozna money)

    //serwer na pare kilentów zrobiony
    //switche działają
    //logowanie działa
    // działa case 2 (wpłata)
    // działa case 4 (informacje o koncie)
    // działa case 1 (wypłata)
}
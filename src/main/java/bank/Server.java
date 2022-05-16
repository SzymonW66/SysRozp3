package bank;

import database.FileManager;
import model.BankUser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    public static void main(String[] args) throws FileNotFoundException {
        FileManager fileManager = new FileManager();
        ArrayList<BankUser> bankUsers = fileManager.loadBankUsersFromFile();
        System.out.println(bankUsers);
        ServerSocket serverSocket = null;
        Socket socket = null;
        BufferedReader brinp = null;
        DataOutputStream out = null;
        try {
            //stworzenie nowego socketa (jednego)
            serverSocket = new ServerSocket(6666);
        } catch (IOException e) {
            System.out.println(
                    "Błąd przy tworzeniu gniazda serwerowego.");
            System.exit(-1);
        }
        System.out.println("Inicjalizacja gniazda zakończona...");
        System.out.println("Parametry gniazda: " + serverSocket);
        while (true) {
            try {
                System.out.println("Trwa oczekiwanie na połączenie...");
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println(e);
                System.exit(-1);
            }
            System.out.println("Nadeszło połączenie...");
            System.out.println("Parametry połączenia: " + socket);
            try {
                System.out.println("Inicjalizacja strumieni...");
                brinp = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()
                        )
                );
                out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                System.out.println("Błąd przy tworzeniu strumieni.");
                System.exit(-1);
            }
            System.out.println("Zakończona inicjalizacja strumieni...");
            System.out.println("Rozpoczęcie pętli głównej...");

            //pętla główna przyjmująca komunikaty od Client
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
                            System.out.println("Wysłano linię: " + resultTrue);
                            break;
                        } else {
                            out.writeBytes(resultFalse + "\n\r");
                            System.out.println("Wysłano linię: " + resultFalse);
                            break;
                        }
                    }
                    int number = Integer.parseInt(brinp.readLine());


                    do {
                        switch (number) {
                            case 1:
                                System.out.println("Wybrano opcję wypłaty");
                            case 2:
                                System.out.println("Wybrano opcje wpłaty");
                                //metoda
                                //zwrot i gituwa
                            case 3:
                                System.out.println("Wybrano opcję przelewu");
                            case 4:
                                System.out.println("Wybrano opcję sprawdzenia stanu konta");
                            case 5:
                                System.out.println("Wylogowano");
                        }
                    } while ()
//                    1-przelew
//                            2-inna komenda
//
//                    while(true) //switch
//                        switch
//                            case 1
//                                czekaj na dane potrzebne do 1
//                                    br.read line --> extra
//
//                                case 2
//                                    czeakj na dane potrzebne do 2
//                                        br.ed extra
//
//                if (line == null || "quit".equals(line)) {
//                   // socket.close();
//                    System.out.println("Zakończenie pracy z klientem...");
//                    break;
//                }
//                out.writeBytes(line + "\n\r");
//                System.out.println("Wysłano linię: " + line);
            } catch (IOException e) {
                System.out.println("Błąd wejścia-wyjścia: " + e);

            }
             }
        }
    }

    //...........
    static String depositMoney(String line, double money, ArrayList<BankUser> bankUsers) {
    BankUser currentPerson = findPerson(line, bankUsers);
        assert currentPerson != null;
        currentPerson.setMoney(currentPerson.getMoney() + money);


    }



   static BankUser findPerson(String lookingFor, ArrayList<BankUser> bankUsers) {
         for (BankUser users: bankUsers) {
            if((users.getLogin() + ";" + users.getPassword()).equals(lookingFor)) {
                return users;
            }
        }
        return null;
    }
}


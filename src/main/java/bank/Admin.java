package bank;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import static bank.Client.printOptions;

public class Admin {
    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int port = 0;
        try {
            port = new Integer("6666").intValue();
        } catch (NumberFormatException e) {
            System.out.println("Nieprawidłowy argument: port");
            System.exit(-1);
        }
        //Inicjalizacja gniazda klienckiego
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(host, port);
        } catch (UnknownHostException e) {
            System.out.println("Nieznany host.");
            System.exit(-1);
        } catch (ConnectException e) {
            System.out.println("Połączenie odrzucone.");
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("Błąd wejścia-wyjścia: " + e);
            System.exit(-1);
        }
        System.out.println("Połączono z " + clientSocket);

        //Deklaracje zmiennych strumieniowych

        BufferedReader brSockInp = null;
        BufferedReader brLocalInp = null;
        DataOutputStream out = null;

        //Utworzenie strumieni
        try {
            out = new DataOutputStream(clientSocket.getOutputStream());
            brSockInp = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            brLocalInp = new BufferedReader(
                    new InputStreamReader(System.in));
        } catch (IOException e) {
            System.out.println("Błąd przy tworzeniu strumieni: " + e);
            System.exit(-1);
        }
        //Pętla główna klienta
        while (true) {
            //wysłanie że jest to aplikacja administratora i ma się uruchomić odpowiedni switch
            String message = "Admin";
            out.writeBytes(message + "\n");
            out.flush();

            System.out.println("Witaj w progamie Bankowym!");
            System.out.println("Podaj swój login: ");
            String username = brLocalInp.readLine();
            System.out.println("Podaj swoje hasło: ");
            String password = brLocalInp.readLine();
            boolean validate = false;
            validate = adminLogin(username, password, out, brLocalInp, brSockInp, clientSocket);

            if (validate == true) {

                try {
                    String option = null;
                    do {
                        printOptions();
                        option = brLocalInp.readLine();
                        out.writeBytes(option + "\n\r");
                        out.flush();
                        System.out.println("Wysyłam do serwera numer: " + option);
                        switch (option) {
                            case "1":
                                System.out.println("Wybrano opcję dodania nowego klienta ");
                                System.out.println("Podaj imie nowego klienta:  ");
                            case "2":
                                System.out.println("Wybrano opcję edycji imienia danego klienta");
                                System.out.println("Podaj numer konta klienta którego chcesz edytować");

                            case "3":
                                System.out.println("Wybrano opcję edycji nazwiska danego klietna");
                                System.out.println("Podaj numer konta klienta którego chcesz edytować");

                            case "4":
                                System.out.println("Wybrano opcję edycję numeru PESEL danego klienta");
                                System.out.println("Podaj numer konta klienta którego chcesz edytować");


                        }

                    } while (option != "5");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean adminLogin(String username, String password, DataOutputStream out, BufferedReader
            brLocalInp, BufferedReader brSockInp, Socket clientSocket) {
        boolean result = false;
        String line = null;
        try {
            if (password != null && username != null) {
                System.out.println("Wysyłam: " + username + " " + password);
                out.writeBytes(username + '\n');
                out.writeBytes(password + '\n');
                out.flush();
                line = brSockInp.readLine();
                System.out.println("Otrzymano : " + line);

            }
            if (password == null || username == null) {
                System.out.println("Kończenie pracy...");
                clientSocket.close();
                System.exit(0);
            }
        } catch (IOException e) {
            System.out.println("Błąd wejścia-wyjścia: " + e);
            System.exit(-1);
        }
        if (line.equals("true")) {
            result = true;
        } else if (line.equals("false")) {
            result = false;
        }

        return result;
    }

    public static void printOptions() {
        System.out.println("1- Dodaj nowego klienta");
        System.out.println("2- Edytuj imię danego klienta ");
        System.out.println("3- Edytuj nazwisko danego klienta");
        System.out.println("4- Edytuj PESEL danego klienta");
    }


}


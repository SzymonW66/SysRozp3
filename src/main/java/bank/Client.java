package bank;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) {
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
        String line = null;
        String info = null;
        String username = null; //Zmienna dla loginu
        String password = null; //Zmienna dla hasła
        int option = 0;
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
            //pobranie danych logowania, wysłania na serwer i odebranie true albo false
          boolean validate = userLogin(username, password, line, out, brLocalInp, brSockInp, clientSocket);
            if (validate == true) {
                System.out.println("1 - Wypłać pieniądze");
                System.out.println("2 - Wpłać pieniądze");
                System.out.println("3 - Wykonaj przelew");
                System.out.println("4 - Sprawdź stan konta");
                System.out.println("5 - Exit");
                System.out.println("Proszę wybrać jedną z opcji: ");
                try {
                   option = Integer.parseInt(brLocalInp.readLine());

                } catch (IOException e) {
                    e.printStackTrace();
                }

                do{
                    switch (option) {
                        case 1:
                            System.out.println("Wybrano opcję wypłaty");
                            try {
                                out.writeBytes(String.valueOf(option + '\n'));
                                System.out.println("Wysyłam numer" + option);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println("Podaj ile chcesz wypłacić pieniędzy ");
                            try {
                                double money = Double.parseDouble(brLocalInp.readLine());
                                out.writeBytes(String.valueOf(money));
                                out.flush();
                                info = brSockInp.readLine();
                                System.out.println("Otrzymano wiadomość: " + info);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 2:
                            System.out.println("Wybrano opcję wpłaty");
                            try {
                                out.writeBytes(String.valueOf(option + '\n'));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println("Podaj ile chcesz wpłacić pieniędzy ");
                            try {
                                double money1 = Double.parseDouble(brLocalInp.readLine());
                                out.writeBytes(String.valueOf(money1));
                                out.flush();
                                info = brSockInp.readLine();
                                System.out.println("Otrzymano wiadomość: " + info);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 3:
                            System.out.println("Wybrano opcję przelewu");
                            try {
                                out.writeBytes(String.valueOf(option + '\n'));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            
                            try {
                                long account = Long.parseLong(brLocalInp.readLine());
                                out.writeBytes(String.valueOf(account));
                                System.out.println("Podaj sumę pieniędzy jaką chcesz jej przelać");
                                double money1 = Double.parseDouble(brLocalInp.readLine());
                                out.writeBytes(String.valueOf(money1));
                                out.flush();
                                info = brSockInp.readLine();
                                System.out.println("Otrzymano wiadomość: " + info);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 4:
                            System.out.println("Wybrano opcję sprawdzenia stanu konta");
                            try {
                                out.writeBytes(String.valueOf(option + '\n'));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                info = brSockInp.readLine();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println("Otrzymano wiadomość: " + info);

                            break;
                        case 5:
                            System.out.println("Wylogowano");

                            break;
                    }
                } while (option != 5);


            }
            else
            {
                System.out.println("Niepoprawne dane logowania");
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }

        }


    }

    public static Boolean userLogin(String username, String password, String line, DataOutputStream out, BufferedReader brLocalInp, BufferedReader brSockInp, Socket clientSocket) {
        boolean result = false;
        try {
            System.out.println("Witaj w progamie Bankowym!");
            System.out.println("Podaj swój login: ");
            username = brLocalInp.readLine();
            System.out.println("Podaj swoje hasło: ");
            password = brLocalInp.readLine();
            if (password != null && username != null) {
                System.out.println("Wysyłam: " + username + " " + password);
                out.writeBytes(username + '\n');
                out.writeBytes(password + '\n');
                out.flush();
            }
            if (password == null || username == null) {
                System.out.println("Kończenie pracy...");
                clientSocket.close();
                System.exit(0);
            }

            line = brSockInp.readLine();
            System.out.println("Otrzymano : " + line);
            if (line == "true") {
                result = true;
            }
        } catch (IOException e) {
            System.out.println("Błąd wejścia-wyjścia: " + e);
            System.exit(-1);
        }


        return true;
    }


}


package bank;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
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
        String line = null;
        String info = null;
        String username = null; //Zmienna dla loginu
        String password = null; //Zmienna dla hasła
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
            System.out.println("Witaj w progamie Bankowym!");
            System.out.println("Podaj swój login: ");
            username = brLocalInp.readLine();
            System.out.println("Podaj swoje hasło: ");
            password = brLocalInp.readLine();
            boolean validate = false;
            validate = userLogin(username, password, out, brLocalInp, brSockInp, clientSocket);

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
                                System.out.println("Wybrano opcję wypłaty");
                                System.out.println("Podaj ile chcesz wypłacić pieniędzy ");
                                try {
                                    double money = Double.parseDouble(brLocalInp.readLine());
                                    out.writeBytes(money + "\n");
                                    out.flush();
                                    String info6 = brSockInp.readLine();
                                    System.out.println(info6.length());
                                    if (info6.length() == 0) {
                                        info6 = brSockInp.readLine();
                                    }
                                    System.out.println("Otrzymano wiadomość: " + info6);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "2":
                                System.out.println("Wybrano opcję wpłaty");
                                System.out.println("Podaj ile chcesz wpłacić pieniędzy ");
                                try {
                                    double money1 = Double.parseDouble(brLocalInp.readLine());
                                    out.writeBytes(money1 + "\n");
                                    out.flush();
                                    String info2 = brSockInp.readLine();
                                    System.out.println(info2.length());
                                    if (info2.length() == 0) {
                                        info2 = brSockInp.readLine();
                                    }
                                    System.out.println(info2);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "3":
                                System.out.println("Wybrano opcję przelewu");
                                try {
                                    System.out.println("Podaj ile chcesz przleać pieniędzy");
                                    double money1 = Double.parseDouble(brLocalInp.readLine());
                                    out.writeBytes(money1 + "\n");
                                    out.flush();
                                    System.out.println("Podaj numer konta odbiory przelewu");
                                    String accountNumber = brLocalInp.readLine();
                                    out.writeBytes(accountNumber + "\n");
                                    out.flush();

                                    String info3 = brSockInp.readLine();
                                    System.out.println(info3.length());
                                    if (info3.length() == 0) {
                                        info3 = brSockInp.readLine();
                                    }
                                    if (info3.equals("True")){
                                        System.out.println("Przelew został wykonany poprawnie");
                                        break;
                                    }
                                    else if (info3.equals("False")) {
                                        System.out.println("Przelew nie został wykonany pomyślnie, sprawdź numer konta i czy wpisałeś ilość pieniedzy bez poprzedającego go znaku minus");
                                        break;
                                    }



                                }catch (Exception e){
                                    System.out.println("bład");
                                }

                                break;
                            case "4":
                                System.out.println("Wybrano opcję sprawdzenia stanu konta");
                                String info4 = brSockInp.readLine();
                                //System.out.println(info4.length()); Zrobione przez Pana
                                if (info4.length() == 0)
                                    info4 = brSockInp.readLine();
                                System.out.println("Informacje o Twoim koncie: " + info4);
                                break;
                            case "5":
                                System.out.println("Wylogowano");
                                break;

                        }
                    } while (option != "5");


                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }


            } else {
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

    public static boolean userLogin(String username, String password, DataOutputStream out, BufferedReader brLocalInp, BufferedReader brSockInp, Socket clientSocket) {
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
        System.out.println("1 - Wypłać pieniądze");
        System.out.println("2 - Wpłać pieniądze");
        System.out.println("3 - Wykonaj przelew");
        System.out.println("4 - Sprawdź stan konta");
        System.out.println("5 - Exit");
        System.out.println("Proszę wybrać jedną z opcji: ");
    }


}


package bank;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLOutput;

import static bank.Client.printOptions;

public class Admin {
    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int port = 0;
        try {
            port = new Integer("6667").intValue();
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
            System.out.println(message);
            System.out.println("Wysłano informację że jest to aplikacja administratora");
            String info9 = brSockInp.readLine();
            System.out.println(info9);

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
                                System.out.println("Podaj imie nowego klienta: ");
                                String name1 = brLocalInp.readLine();
                                out.writeBytes(name1 + "\n");
                                out.flush();
                                System.out.println("Podaj nazwisko nowego klienta");
                                String surname1 = brLocalInp.readLine();
                                out.writeBytes(surname1 + "\n");
                                out.flush();
                                System.out.println("Podaj PESEL nowego klienta");
                                String pesel1 = brLocalInp.readLine();
                                out.writeBytes(pesel1 + "\n");
                                out.flush();
                                System.out.println("Podaj login dla nowego klienta");
                                String login1 = brLocalInp.readLine();
                                out.writeBytes(login1 + "\n");
                                out.flush();
                                System.out.println("Podaj hasło dla nowego klienta");
                                String password1 = brLocalInp.readLine();
                                out.writeBytes(password1 + "\n");
                                out.flush();
                                String info1 = brSockInp.readLine();
                                System.out.println(info1.length());
                                if (info1.length() == 0) {
                                    info1 = brSockInp.readLine();
                                }
                                System.out.println(info1);
                                if (info1.equals("True")) {
                                    System.out.println("Dodano nowego użytkownika");
                                    break;
                                } else if (info1.equals("False")) {
                                    System.out.println("Zmiana zakończona niepomyślnie, upewnij się że wszędzie zostały podane informacje, a nie puste linie ");
                                    break;
                                }


                            case "2":
                                System.out.println("Wybrano opcję edycji imienia danego klienta");
                                System.out.println("Podaj numer konta klienta którego chcesz edytować");
                                String accountNumber2 = brLocalInp.readLine();
                                out.writeBytes(accountNumber2 + "\n");
                                out.flush();
                                System.out.println("Podaj nowe imię klienta");
                                String name2 = brLocalInp.readLine();
                                out.writeBytes(name2 + "\n");
                                out.flush();
                                String info2 = brSockInp.readLine();
                                System.out.println(info2.length());
                                if (info2.length() == 0) {
                                    info2 = brSockInp.readLine();
                                }
                                System.out.println(info2);
                                if (info2.equals("Sukces")) {
                                    System.out.println("Zmiana zakończona pomyślnie");
                                    break;
                                } else if (info2.equals("Puste pole")) {
                                    System.out.println("Zmiana zakończona niepomyślnie, podano puste pole zamiast nowego imienia");
                                    break;
                                } else if (info2.equals("Nie znaleziono")) {
                                    System.out.println("Zmiana zakończona niepomyślnie, nie znaleziono numeru konta spórbuj ponownie");
                                    break;
                                }

                            case "3":
                                System.out.println("Wybrano opcję edycji nazwiska danego klietna");
                                System.out.println("Podaj numer konta klienta którego chcesz edytować");
                                String accountNumber3 = brLocalInp.readLine();
                                out.writeBytes(accountNumber3 + "\n");
                                out.flush();
                                System.out.println("Podaj nowe imię klienta");
                                String name3 = brLocalInp.readLine();
                                out.writeBytes(name3 + "\n");
                                out.flush();
                                String info3 = brSockInp.readLine();
                                System.out.println(info3.length());
                                if (info3.length() == 0) {
                                    info3 = brSockInp.readLine();
                                }
                                if (info3.equals("Sukces")) {
                                    System.out.println("Zmiana zakończona pomyślnie");
                                    break;
                                } else if (info3.equals("Puste pole")) {
                                    System.out.println("Zmiana zakończona niepomyślnie, podano puste pole zamiast nowego imienia");
                                } else if (info3.equals("Nie znaleziono")) {
                                    System.out.println("Zmiana zakończona niepomyślnie, nie znaleziono numeru konta spórbuj ponownie");
                                }

                            case "4":
                                System.out.println("Wybrano opcję edycję numeru PESEL danego klienta");
                                System.out.println("Podaj numer konta klienta którego chcesz edytować");
                                String accountNumber4 = brLocalInp.readLine();
                                out.writeBytes(accountNumber4 + "\n");
                                out.flush();
                                System.out.println("Podaj nowe imię klienta");
                                String name4 = brLocalInp.readLine();
                                out.writeBytes(name4 + "\n");
                                out.flush();
                                String info4 = brSockInp.readLine();
                                System.out.println(info4.length());
                                if (info4.length() == 0) {
                                    info4 = brSockInp.readLine();
                                }
                                if (info4.equals("Sukces")) {
                                    System.out.println("Zmiana zakończona pomyślnie");
                                    break;
                                } else if (info4.equals("Puste pole")) {
                                    System.out.println("Zmiana zakończona niepomyślnie, podano puste pole zamiast nowego imienia");
                                    break;
                                } else if (info4.equals("Nie znaleziono")) {
                                    System.out.println("Zmiana zakończona niepomyślnie, nie znaleziono numeru konta spórbuj ponownie");
                                    break;
                                }

                            case "5":
                                System.out.println("Koniec na dziś, baj baj");
                                clientSocket.close();
                                System.exit(0);
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
        System.out.println("5- Exit");
    }


}


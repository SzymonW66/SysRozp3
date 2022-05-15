package database;

import bank.Client;
import model.BankUser;

import java.io.*;
import java.util.ArrayList;

public class FileManager {
    private final static String FILE_PATH = "src/main/resources/database.txt";

    public ArrayList<BankUser> loadBankUsersFromFile() {
        ArrayList<BankUser> bankUserArrayList = new ArrayList<>();
        File file = new File(FILE_PATH);
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String bankUsersString;
            while ((bankUsersString = bufferedReader.readLine()) != null) {
                bankUserArrayList.add(createBankUserFromString(bankUsersString));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bankUserArrayList;
    }

    private BankUser createBankUserFromString(String txtText) {
        String[] split = txtText.split(";");
        String firstName = split[0];
        String lastName = split[1];
        String pesel = split[2];
        String login = split[3];
        String password = split[4];
        long accountNumber = Long.parseLong(split[5]);
        double money = Double.parseDouble(split[6]);
        return new BankUser(firstName, lastName, pesel, login, password, money, accountNumber);
    }
}


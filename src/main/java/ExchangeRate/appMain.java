package ExchangeRate;

import java.awt.event.ActionEvent;
import java.io.Console;
import java.util.Map;
import java.util.*;


/**
 * This class is currently used for giving example how to use the CurrencyMap and ExchangeHistory Class.
 * This class will be updated to user interface handler.
 */
public class appMain {

    private static final String USER1 = "admin";
    private static final String USER2 = "normal user";
    private static final String USER3 = "user";
    private static final String PASSWORD = "password";


    public static void main(String[] args) {
        // Load currency data from JSON file
        String filePath = "exchangeRateDatabase.json"; // Specify the path to your JSON file
        List<CurrencyMap> currencyList = CurrencyMap.loadExchangeRates(filePath);
        Console myObj = System.console();

        boolean flag = true;
        while (flag) {
            System.out.println("Enter username: ");
            String username = myObj.readLine();

            if ((username.equalsIgnoreCase(USER2) || username.equalsIgnoreCase(USER3))) {
                System.out.println("Enter password: ");
                String password = myObj.readLine();
                if (password.equals(PASSWORD)) {
                    User user = new User(currencyList, "normal user", "password");
                    System.out.println("Login successful!");
                    System.out.println("Choose one of the following options (enter number):");
                    System.out.println("1) View Exchange History");
                    System.out.println("2) Convert Currency");
                    System.out.println("3) Exit");
                    String option = myObj.readLine();
                    if (option.equals("1") || option.equals("View Exchange History")) {
                        user.printCurrencyTable();
                    } else if (option.equals("2") || option.equals("Convert Currency")) {
                        System.out.println("Enter Currency Code (From): ");
                        String currencyFrom = myObj.readLine();
                        System.out.println("Enter Amount: ");
                        String amount = myObj.readLine();
                        System.out.println("Enter Currency Code (To): ");
                        String currencyTo = myObj.readLine();

                        user.printSummaryConvertCurrency(currencyFrom, Float.parseFloat(String.valueOf(amount)), currencyTo);
                        //launch(args);
                    } else if (option.equals("3")) {
                        System.exit(0);
                    }
                }

            } else if (username.equalsIgnoreCase(USER1)) {
                Admin admin = new Admin(currencyList, "admin", "");

                System.out.println("Login successful!");
                System.out.println("Choose one of the following options (enter number):");
                System.out.println("1) View Exchange History");
                System.out.println("2) Add Currency");
                System.out.println("3) Update Currency");
                System.out.println("4) Exit");
                String option = myObj.readLine();

                if (option.equals("1")) {
                    admin.printCurrencyTable();
                } else if (option.equals("2")) {
                    System.out.println("Write Currency Code (you want to add): ");
                    String currencyCode = myObj.readLine();
                    System.out.println("Write its respective currency name: ");
                    String currencyName = myObj.readLine();
                    admin.addACurrency(currencyCode, currencyName);
                    admin.getACurrency(currencyCode);
                } else if (option.equals("3")) {
                    System.out.println("Enter Currency Code (From): ");
                    String currencyFrom = myObj.readLine();
                    System.out.println("Enter Target Currency Code (To): ");
                    String currencyTo = myObj.readLine();
                    System.out.println("Enter Updated Exchange Rate: ");
                    String exchangeRate = myObj.readLine();
                    admin.updateExchangeRate(currencyFrom, currencyTo, Double.parseDouble(exchangeRate));

                } else if (option.equals("4")) {
                    System.exit(0);
                } else {
                    System.out.println("Try Again.");
                }


            } else {
                System.out.println("Login failed. Please check your credentials.");
            }
        }

    }

    /**
     * @param startDate
     * @param endDate
     * @param rateHistoryList Helper function to use to find the list of rates between the specified start date and end date.
     *                        The rateHistoryList is a list of exchange rate history between a currency and specified target currency.
     * @return List of rates between the specified dates in the history database
     */
    private static List<Double> findRateHistory(String startDate, String endDate, List<ExchangeHistory> rateHistoryList) {
        List<Double> rates = new ArrayList<>();
        for (ExchangeHistory history : rateHistoryList) {
            String date = history.getDate();
            // Check if the date falls within the specified range (inclusive)
            if (date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0) {
                rates.add(history.getRate());
            }
        }
        return rates;
    }
}
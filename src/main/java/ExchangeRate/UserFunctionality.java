package ExchangeRate;

import java.util.*;

public class UserFunctionality {

    public static void main(String[] args) {
        String databasePath = "exchangeRateDatabase.json";
        String popularCurrencyPath = "popularCurrencies.json";

        currencyConversion(databasePath); //test
    }

    public static void currencyConversion(String databasePath) {
        List<CurrencyMap> currencyList = CurrencyMap.loadExchangeRates(databasePath);

        if (currencyList == null || currencyList.isEmpty()) {
            System.out.println("No exchange rates found.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Amount to convert: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("From currency: ");
        String fromCurrency = scanner.nextLine().toUpperCase();

        System.out.print("To currency: ");
        String toCurrency = scanner.nextLine().toUpperCase();

        // finds map for fromCurrency from the loaded currencyList
        CurrencyMap fromCurrencyMap = findCurrencyMap(currencyList, fromCurrency);
        if (fromCurrencyMap == null) {
            System.out.println(fromCurrency + " not found.");
            return;
        }

        // get exchange rate from original to target currency
        Double exchangeRate = fromCurrencyMap.getExchangeRateNumber(toCurrency);
        if (exchangeRate == null) {
            System.out.println("No exchange rate from " + fromCurrency + " to " + toCurrency + ".");
            return;
        }

        double convertedAmount = amount * exchangeRate; // convert amount
        // display conversion
        System.out.printf("%.2f %s = %.2f %s.", amount, fromCurrency, convertedAmount, toCurrency);

    }

    // find map for given currency symbol
    private static CurrencyMap findCurrencyMap(List<CurrencyMap> currencyList, String currencySymbol) {
        for (CurrencyMap currency : currencyList) {
            if (currency.getCurrencySymbol().equals(currencySymbol)) {
                return currency;
            }
        }
        return null;
    }
}

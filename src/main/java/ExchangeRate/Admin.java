package ExchangeRate;

import java.util.*;

public class Admin {
    private List<CurrencyMap>Currencies;
    private String username;
    private String password;

    public Admin(List<CurrencyMap> Currencies, String username, String password) {
        this.Currencies = Currencies;
        this.username = username;
        this.password = password;
    }

    public List<CurrencyMap> getCurrencies() {
        return Currencies;
    }

    public void setCurrencies(List<CurrencyMap> Currencies) {
        this.Currencies = Currencies;
    }

    /**
     * @param CurrencyName
     * @param CurrencyCode
     * Add a currency into the Currency Map list, will create a new empty hashmap.
     * Currency Name is the specified currency name to be added into the list
     * Currency Code is the currency code of the specified currency.
     */
    public void addACurrency(String CurrencyName, String CurrencyCode) {
        Map<String, List<ExchangeHistory>> newExchangeHistory = new HashMap<>();
        Map<String, Double> newExchangeRate = new HashMap<>();
        CurrencyMap newCurrency = new CurrencyMap(CurrencyName, CurrencyCode, newExchangeRate ,newExchangeHistory);
        Currencies.add(newCurrency);
    }

    /**
     * @param currencyCode
     * @return
     */
    public CurrencyMap getACurrency (String currencyCode) {
        CurrencyMap currency = null;
        for (CurrencyMap currencyMap : Currencies) {
            if (currencyMap.getCurrencySymbol().equals(currencyCode)) {
                currency = currencyMap;
            }
        }
        return currency;
    }

    /**
     * @param currencyCode
     * @param rate
     * Update the exchange rate between two currency
     */
    public void updateExchangeRate(String currencyCode, String targetCurrency, double rate) {
        CurrencyMap currency = getACurrency(currencyCode);
        currency.updateExchangeRate(targetCurrency, rate);
    }

    /**
     * @param currencyCode
     * @param targetCurrencyCode
     * Get the exchange rate between two currency
     * @return The currency exchange rate between the specified currency and target currency
     */
    public Double getExchangeRate(String currencyCode, String targetCurrencyCode) {
        CurrencyMap currency = getACurrency(currencyCode);
        return currency.getExchangeRate().get(targetCurrencyCode);
    }

    /**
     * @param currencyCode
     * @param targetCurrencyCodes
     * Get the exchange rates in a currency map according to the order of targetCurrencyCodes
     * @return List of rates with the order of targetCurrencyCodes
     */
    public List<Double> getExchangeRates(String currencyCode, List<String> targetCurrencyCodes) {
        CurrencyMap currency = getACurrency(currencyCode);
        List<Double> rates = new ArrayList<>();
        for (String targetCurrencyCode : targetCurrencyCodes) {
            if (currency.getExchangeRate().containsKey(targetCurrencyCode)) {
                rates.add(currency.getExchangeRate().get(targetCurrencyCode));
            }
        }
        return rates;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param currencyCode
     * @param targetCurrencyCode
     *
     * @return get the latest trend between the two currencies
     */
    public String getLatestTrend(String currencyCode, String targetCurrencyCode) {
        CurrencyMap currency = getACurrency(currencyCode);
        return currency.getLatestTrend(targetCurrencyCode);
    }

    /**
     *
     * @param startDate
     * @param endDate
     * @param targetCurrencyCode
     * Helper function to use to find the list of rates between the specified start date and end date.
     * The rateHistoryList is a list of exchange rate history between a currency and specified target currency.
     * @return List of rates between the specified dates in the history database
     */
    public List<Double> findRateHistory(String startDate, String endDate,String currencyCode, String targetCurrencyCode) {
        List<Double> rates = new ArrayList<>();
        List<Double> output = new ArrayList<>();
        CurrencyMap currency = getACurrency(currencyCode);
        List<ExchangeHistory> rateHistoryList;
        rateHistoryList = currency.getRateHistoryBetweenCurrency(targetCurrencyCode);

        for (ExchangeHistory history : rateHistoryList) {
            String date = history.getDate();
            // Check if the date falls within the specified range (inclusive)
            if (date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0) {
                rates.add(history.getRate());
            }
        }

        PrintSummary print = new PrintSummary(rates);
        output.add(print.min());
        output.add(print.max());
        output.add(print.avg());
        output.add(print.median());
        output.add(print.stddev());
        System.out.println(output.size());
        output.addAll(rates);
        return output;
    }

    /**
     * @param currency
     * @param targetCurrency
     * Helper function to convert the ExchangeHistory class to string.
     */
    public void printExchangeHistory(CurrencyMap currency, String targetCurrency) {
        List<ExchangeHistory> historyList = currency.getExchangeHistory().get(targetCurrency);
        if (historyList != null) {
            for (ExchangeHistory history : historyList) {
                System.out.println("Target Currency: " + history.getTargetCurrency());
                System.out.println("Date: " + history.getDate());
                System.out.println("Rate: " + history.getRate());
                System.out.println("Direction: " + history.getDirection());
                System.out.println("Note: " + history.getNote());
                System.out.println();
            }
        } else {
            System.out.println("No exchange history found for " + targetCurrency + ".");
        }
    }

    public List<Double> printSummaryConvertCurrency(String currency, double amount, String targetCurrency){
        // Logic for converting currency goes here
        CurrencyMap currencyMap = null;
        Double exchangeRate = 0.00;
        List<Double> output = new ArrayList<>();

        List<CurrencyMap> currencyList = this.getCurrencies();
        ArrayList<Double> rateList = new ArrayList<>();

        if (currencyList != null) {
            for (CurrencyMap curr : currencyList) {
                if (curr.getCurrencySymbol().equals(currency)) {
                    currencyMap = curr;
                    rateList.add(currencyMap.getExchangeRateNumber(targetCurrency));
                    break;
                }
            }
        }
        if (currencyMap != null){
            Map<String, Double> exchangeRateMap = currencyMap.getExchangeRate();
            for (Map.Entry<String, Double> entry : exchangeRateMap.entrySet()) {
                if(entry.getKey().equals(targetCurrency)){
                    exchangeRate = entry.getValue();
                }
            }
        }

        // Calculate converted amount
        output.add(exchangeRate);
        double convertedAmount = amount * exchangeRate;
        output.add(convertedAmount);

        System.out.println("Conversion Rate (" + currency + " to " + targetCurrency + "): " + exchangeRate);
        System.out.println("Converted Amount (" + currency + " to " + targetCurrency + "): " + convertedAmount);
        System.out.println(output.size());
        return output;

    }

    public List<String> popularCurrenciesList(){
        // Initialize the dictionary to track currency counts
        Map<String, Integer> countDict = new HashMap<>();

        // Loop through the currencies
        for (CurrencyMap currency : Currencies) {
            String symbol = currency.getCurrencySymbol();
            countDict.put(symbol, countDict.getOrDefault(symbol, 0) + 1);
        }

        // Sort the countDict by values in decreasing order
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(countDict.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        List<String> popularcurrencies = new ArrayList<>();

        for(int i = 0; i < 4; i++){
            popularcurrencies.add(sortedEntries.get(i).getKey());
        }
        return popularcurrencies;
    }

    public void printCurrencyTable() {

        List<String> currenciesList = popularCurrenciesList();
        // Header row
        System.out.printf("%-10s", "From/To ");
        for(int i = 0; i < 4; i++){
            System.out.printf("%-15s", currenciesList.get(i));
        }
        System.out.println();


        // Table body
        for (String fromCurrency : currenciesList) {
            System.out.printf("%-10s", fromCurrency); // Row label (from currency)
            for (String toCurrency : currenciesList) {
                if (fromCurrency.equals(toCurrency)) {
                    System.out.printf("%-15s", "-"); // No exchange for same currency
                } else {
                    // Get the exchange rate and trend
                    Double exchangeRate = this.getExchangeRate(fromCurrency, toCurrency);
                    String trend = this.getLatestTrend(fromCurrency, toCurrency);
                    System.out.printf("%-15s", formatTrend(exchangeRate, trend));
                }
            }
            System.out.println();

        }

    }

    // Helper method to format the exchange rate with trend symbols
    public String formatTrend(Double rate, String direction) {
        if (rate == null || rate < 0  || direction == null) {
            return "N/A";
        }
        String symbol = direction.equals("Increased") ? "\u2191" : direction.equals("Decreased") ? "\u2193" : "-";
        return String.format("%.2f %s (%s)", rate, symbol, direction.substring(0, 1));
    }
}



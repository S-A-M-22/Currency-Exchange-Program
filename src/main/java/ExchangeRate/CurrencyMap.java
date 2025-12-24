package ExchangeRate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.io.FileWriter;

/**
 * This class is a Custom Map to stores a Currency and its parameter such as name and its symbol. It also stores, two
 * Maps to store the exchange history between the currency and target currency and store the current exchange rate
 * between the two currencies. List of Exchange Histories will have description of the target currency, date, note and
 * its direction's trend.
 */
public class CurrencyMap {
    private String currencyName;
    private String currencySymbol;
    private Map<String, List<ExchangeHistory>> exchangeHistory = new HashMap<>();
    private Map<String, Double> exchangeRate = new HashMap<>();

    /**
     *
     * @param currencyName
     * @param currencySymbol
     * @param exchangeRate
     * @param exchangeHistory
     * Constructor method for CurrencyMap class
     */
    public CurrencyMap(String currencyName, String currencySymbol,
                       Map<String, Double> exchangeRate, Map<String, List<ExchangeHistory>> exchangeHistory) {
        this.currencyName = currencyName;
        this.currencySymbol = currencySymbol;
        this.exchangeHistory = exchangeHistory;
        this.exchangeRate = exchangeRate;
    }

    // Getter and Setter methods
    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public Map<String, Double> getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Map<String, Double> exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Double getExchangeRateNumber(String currencyName) {
        return exchangeRate.get(currencyName);
    }

    public void setExchangeHistory(Map<String, List<ExchangeHistory>> exchangeHistory) {
        this.exchangeHistory = exchangeHistory;
    }

    public Map<String, List<ExchangeHistory>> getExchangeHistory() {
        return exchangeHistory;
    }

    /**
     *
     * @param targetCurrency
     * Get the list of Exchange history between two currency, For example:
     * If the CurrencyMap is Australian Dollars, and the target currency is USD,
     * return the list of exchange rate history between the two
     * @return List of Exchange rate History
     */
    public List<ExchangeHistory> getRateHistoryBetweenCurrency(String targetCurrency) {
        List<ExchangeHistory> rateHistoryList = new ArrayList<>();

        // Check if the target currency exists in the exchange history map
        if (exchangeHistory.containsKey(targetCurrency)) {
            // Add all ExchangeHistory objects to the list
            rateHistoryList.addAll(exchangeHistory.get(targetCurrency));
        }

        return rateHistoryList;
    }

    /**
     *
     * @param TargetCurrency
     * Get the latest trend or trajectory of the exchange rate between the two currency
     * For Example:
     * If there is an update for AUD to USD and the trend is upward, it should return increased as it's direction
     * @return String of the latest trend
     */
    public String getLatestTrend(String TargetCurrency) {
        String latestTrend = "";
        int last_index= 0;
        for (Map.Entry<String, List<ExchangeHistory>> entry : exchangeHistory.entrySet()) {
            if (entry.getKey().equals(TargetCurrency)) {
                last_index = entry.getValue().size();
                // get the latest direction from exchangeHistory
                latestTrend = entry.getValue().get(last_index-1).getDirection();
                break;
                }
            }
        return latestTrend;
    }


    /**
     *
     * @param targetCurrency
     * @param newRate
     *
     * Update the exchange rate between the Currency and Target Currency.
     * The function would also add the update into its Exchange History database.
     */
    public void updateExchangeRate(String targetCurrency, Double newRate) {
        // Get the old rate for the target currency
        Double oldRate = this.exchangeRate.get(targetCurrency);
        String direction;

        // Determine the direction of the rate change
        if (oldRate == null) {
            direction = "New";
        } else if (newRate > oldRate) {
            direction = "Increased";
        } else if (newRate < oldRate) {
            direction = "Decreased";
        } else {
            direction = "Neutral";
        }

        // Create a new ExchangeHistory entry
        ExchangeHistory newEntry = new ExchangeHistory(
                targetCurrency,
                LocalDate.now().toString(),
                newRate,
                "Updated rate to " + targetCurrency,
                direction
        );

        // Update or create the history list for the target currency
        exchangeHistory.computeIfAbsent(targetCurrency, k -> new ArrayList<>()).add(newEntry);

        // Update the exchange rate map with the new rate
        this.exchangeRate.put(targetCurrency, newRate);
    }

    /**
     *
     * @param filePath
     * The function is to load the Exchange Rates from the json file database.
     * It can also be used to initialise the program for the first time.
     * @return List of the Currency and its parameters provided in json files
     */
    public static List<CurrencyMap> loadExchangeRates(String filePath) {
        List<CurrencyMap> currencyList = new ArrayList<>();
        StringBuilder jsonData = new StringBuilder();

        try (FileReader reader = new FileReader(filePath)) {
            int ch;
            while ((ch = reader.read()) != -1) {
                jsonData.append((char) ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        JSONArray jsonArray = new JSONArray(jsonData.toString());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonCurrency = jsonArray.getJSONObject(i);

            String currencyName = jsonCurrency.getString("currencyName");
            String currencySymbol = jsonCurrency.getString("currencySymbol");

            JSONArray jsonHistoryArray = jsonCurrency.getJSONArray("exchangeHistory");
            Map<String, List<ExchangeHistory>> exchangeHistoryMap = new HashMap<>();
            for (int j = 0; j < jsonHistoryArray.length(); j++) {
                JSONObject jsonHistory = jsonHistoryArray.getJSONObject(j);
                ExchangeHistory history = fromJson(jsonHistory); // Use fromJson method to create ExchangeHistory object

                // Add history to the map
                String targetCurrency = history.getTargetCurrency();
                exchangeHistoryMap.computeIfAbsent(targetCurrency, k -> new ArrayList<>()).add(history);
            }

            JSONObject jsonExchangeRates = jsonCurrency.getJSONObject("exchangeRate");
            Map<String, Double> exchangeRate = new HashMap<>();
            for (String key : jsonExchangeRates.keySet()) {
                exchangeRate.put(key, jsonExchangeRates.getDouble(key));
            }

            currencyList.add(new CurrencyMap(currencyName, currencySymbol, exchangeRate, exchangeHistoryMap));
        }
        return currencyList;
    }

    /**
     *
     * @param newCurrencyName
     * @param newCurrencySymbol
     * @param initialExchangeRates
     * @param filePath
     * adds new currencies to the exchange rate json database
     */
    public static void addCurrency(String newCurrencyName, String newCurrencySymbol, Map<String, Double> initialExchangeRates, String filePath) {

        List<CurrencyMap> currencyList = loadExchangeRates(filePath);
        if (currencyList == null) {
            currencyList = new ArrayList<>();
        }
        // check if currency in list
        for (CurrencyMap currency : currencyList) {
            if (currency.getCurrencySymbol().equals(newCurrencySymbol)) {
                System.out.println("Currency " + newCurrencySymbol + " already exists.");
                return; // return if symbol exists
            }
        }

        // set up initial exchange history for new currency
        Map<String, List<ExchangeHistory>> newExchangeHistory = new HashMap<>();
        for (String targetCurrency : initialExchangeRates.keySet()) { // returns set of keys in map
            List<ExchangeHistory> historyList = new ArrayList<>();
            ExchangeHistory initialHistory = new ExchangeHistory(
                    targetCurrency,
                    LocalDate.now().toString(),  // current date
                    initialExchangeRates.get(targetCurrency),
                    "First time initialization of exchange rate to " + targetCurrency,
                    "New"
            );
            historyList.add(initialHistory);
            newExchangeHistory.put(targetCurrency, historyList);
        }

        // create the newCurrency object
        CurrencyMap newCurrency = new CurrencyMap(newCurrencyName, newCurrencySymbol, initialExchangeRates, newExchangeHistory);
        currencyList.add(newCurrency); // add new currency to currencyList to be saved
        saveExchangeRates(filePath, currencyList); // save to json file
    }


    /**
     *
     * @param filePath
     * @param currencyList
     * Save the current List of Currency and its parameters and history into json file for database use and keep
     * tracks of its updates.
     */
    public static void saveExchangeRates(String filePath, List<CurrencyMap> currencyList) {
        JSONArray jsonArray = new JSONArray();

        for (CurrencyMap currency : currencyList) {
            JSONObject jsonCurrency = new JSONObject();
            jsonCurrency.put("currencyName", currency.getCurrencyName());
            jsonCurrency.put("currencySymbol", currency.getCurrencySymbol());

            JSONArray jsonHistoryArray = new JSONArray();
            for (Map.Entry<String, List<ExchangeHistory>> entry : currency.getExchangeHistory().entrySet()) {
                for (ExchangeHistory history : entry.getValue()) {
                    jsonHistoryArray.put(toJson(history)); // Use toJson method to convert ExchangeHistory object to JSON
                }
            }
            jsonCurrency.put("exchangeHistory", jsonHistoryArray);

            JSONObject jsonExchangeRates = new JSONObject();
            for (Map.Entry<String, Double> entry : currency.getExchangeRate().entrySet()) {
                jsonExchangeRates.put(entry.getKey(), entry.getValue());
            }
            jsonCurrency.put("exchangeRate", jsonExchangeRates);

            jsonArray.put(jsonCurrency);
        }

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(jsonArray.toString(4)); // Format JSON with indentation for readability
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param history
     * Helper function for saveExchangeRates, convert ExchangeHistory into json object format to use for saving into
     * json file.
     * @return Json format of the exchange history
     */
    private static JSONObject toJson(ExchangeHistory history) {
        JSONObject json = new JSONObject();
        json.put("targetCurrency", history.getTargetCurrency());
        json.put("date", history.getDate());
        json.put("rate", history.getRate());
        json.put("note", history.getNote());
        json.put("direction", history.getDirection());
        return json;
    }

    /**
     *
     * @param json
     * Helper function for loadExchangeRates, convert json file into ExchangeHistory to be used for the CurrencyMap
     * class.
     * @return ExchangeHistory format of the exchange history between two currencies
     */
    private static ExchangeHistory fromJson(JSONObject json) {
        ExchangeHistory newEntry = new ExchangeHistory(
                json.getString("targetCurrency"),
                json.getString("date"),
                json.getDouble("rate"),
                json.optString("note", ""),
                json.optString("direction", "Neutral")

        );
        return newEntry;
    }
}





package test;

import static org.junit.jupiter.api.Assertions.*;

import ExchangeRate.ExchangeHistory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ExchangeRate.CurrencyMap;

import java.util.*;

/**
 * The CurrencyMapTest class contains unit tests for the CurrencyMap class.
 * It validates functionalities such as loading and saving exchange rates,
 * printing exchange history, updating exchange rates, and using getters and setters.
 */
public class CurrencyMapTest {

    private String filePath = "exchangeRateDatabase.json";
    private List<CurrencyMap> currencyList;

    /**
     * Sets up the test environment before each test.
     * Loads exchange rates from a JSON file and verifies that the loaded list
     * is not null.
     */
    @BeforeEach
    public void setUp() {
        currencyList = CurrencyMap.loadExchangeRates(filePath);
        assertNotNull(currencyList, "Please provide exchange rate list");
    }

    /**
     * Tests loading exchange rates from a file.
     * Ensures that the list of currencies is not null or empty after loading
     * from the provided file.
     */
    @Test
    void testLoadExchangeRates() {
        assertNotNull(currencyList);
        assertFalse(currencyList.isEmpty(), "Currency list should not be empty.");
    }

    /**
     * Tests the printing of exchange history.
     * Verifies that the exchange history of each currency in the list is not null
     * and that all required fields are correctly allocated.
     */
    @Test
    void testPrintExchangeHistory() {
        // Verify that exchange history data is correctly printed
        for (CurrencyMap currency : currencyList) {
            assertNotNull(currency.getCurrencyName());
            assertNotNull(currency.getCurrencySymbol());
            assertNotNull(currency.getExchangeRate());

            for (Map.Entry<String, List<ExchangeHistory>> entry : currency.getExchangeHistory().entrySet()) {
                assertNotNull(entry.getKey());
                for (ExchangeHistory history : entry.getValue()) {
                    assertNotNull(history.getTargetCurrency());
                    assertNotNull(history.getDate());
                    assertNotNull(history.getRate());
                    assertNotNull(history.getNote());
                    assertNotNull(history.getDirection());
                }
            }
        }
    }

    /**
     * Tests the updating of exchange rates for a specific currency.
     * Verifies that the latest exchange rates and exchange history are correctly updated.
     */
    @Test
    void testUpdateExchangeRates() {
        // Find AUD currency and update rates
        CurrencyMap audCurrency = null;
        for (CurrencyMap currency : currencyList) {
            if (currency.getCurrencySymbol().equals("AUD")) {
                audCurrency = currency;
                break;
            }
        }
        assertNotNull(audCurrency, "AUD currency not found in the list of currencies.");

        // Update exchange rates and verify
        audCurrency.updateExchangeRate("USD", 0.5);
        audCurrency.updateExchangeRate("USD", 0.6);
        audCurrency.updateExchangeRate("USD", 0.7);
        audCurrency.updateExchangeRate("JPY", 95.00);

        assertEquals(0.7, audCurrency.getExchangeRate().get("USD"),
                "Latest AUD to USD exchange rate should be 0.7");
        assertEquals(95.00, audCurrency.getExchangeRate().get("JPY"),
                "Latest AUD to JPY exchange rate should be 95.00");
        // Check if rates are updated correctly
        List<ExchangeHistory> usdHistory = audCurrency.getRateHistoryBetweenCurrency("USD");
        assertEquals(4, usdHistory.size(), "USD exchange rate history should have 3 entries.");

        // Test getting the latest trend
        String latestTrend = audCurrency.getLatestTrend("USD");
        assertNotNull(latestTrend, "Latest trend should not be null.");
        assertEquals("Increased", latestTrend, "Latest trend should be increased.");
    }

    /**
     * Tests the saving of exchange rates to a new json file.
     */
    @Test
    void testSaveExchangeRates() {
        // Save the updated currency list to a new file
        String newJsonPath = "testUpdatedDatabase.json";
        CurrencyMap.saveExchangeRates(newJsonPath, currencyList);

        // Reload the saved data to verify
        List<CurrencyMap> updatedCurrencyList = CurrencyMap.loadExchangeRates(newJsonPath);
        assertNotNull(updatedCurrencyList, "Failed to load updated exchange rates from file.");
        assertEquals(currencyList.size(), updatedCurrencyList.size(), "Currency list sizes should match after saving.");
    }

    /**
     * Tests the getter and setter methods for CurrencyMap
     */
    @Test
    void testGettersAndSetters() {
        // Test getters
        CurrencyMap audCurrency = null;
        for (CurrencyMap currency : currencyList) {
            if (currency.getCurrencySymbol().equals("AUD")) {
                audCurrency = currency;
                break;
            }
        }
        assertEquals("Australian Dollar", audCurrency.getCurrencyName());
        assertEquals("AUD", audCurrency.getCurrencySymbol());
        assertEquals(0.672, audCurrency.getExchangeRateNumber("USD"));

        // Test setters
        audCurrency.setCurrencyName("Singapore Dollar");
        audCurrency.setCurrencySymbol("SGD");
        audCurrency.setExchangeRate(new HashMap<>(Map.of("USD", 0.767, "JPY", 109.63,
                "AUD", 1.15, "EUR", 0.693)));
        audCurrency.setExchangeHistory(new HashMap<>(Map.of("USD", new ArrayList<>(Collections.singletonList(
                new ExchangeHistory("USD", "2024-09-03", 0.85, "Updated rate", "Increased")
        )))));

        // Validate setters
        assertEquals("Singapore Dollar", audCurrency.getCurrencyName());
        assertEquals("SGD", audCurrency.getCurrencySymbol());
        assertEquals(0.767, audCurrency.getExchangeRateNumber("USD"));
        assertEquals(109.63, audCurrency.getExchangeRateNumber("JPY"));
        assertEquals(1.15, audCurrency.getExchangeRateNumber("AUD"));
        assertEquals(0.693, audCurrency.getExchangeRateNumber("EUR"));

        // Validate updated exchange history
        List<ExchangeHistory> updatedUsdHistory = audCurrency.getRateHistoryBetweenCurrency("USD");
        assertEquals(1, updatedUsdHistory.size());
        assertEquals("2024-09-03", updatedUsdHistory.get(0).getDate());
        assertEquals("Updated rate", updatedUsdHistory.get(0).getNote());
    }

    /**
     * Tests the addition of a new currency.
     */
    @Test
    void testAddCurrency() {
        Map<String, Double> testRates = new HashMap<>();
        testRates.put("USD", 1.30);
        testRates.put("EUR", 1.18);
        testRates.put("JPY", 185.94);
        testRates.put("AUD", 1.96);

        CurrencyMap.addCurrency("British Pound", "GBR", testRates, filePath);
        //unfinished
    }

}

package test;

import static org.junit.jupiter.api.Assertions.*;

import ExchangeRate.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

// create fixed list to test PrintCurrencyTable
class TestAdmin extends Admin {
    public TestAdmin(List<CurrencyMap> currencies, String username, String password) {
        super(currencies, username, password);
    }

    @Override
    public List<String> popularCurrenciesList() {
        return Arrays.asList("USD", "EUR", "AUD", "JPY");
    }
}

public class AdminTest {

    private Admin admin;
    private List<CurrencyMap> currencies;
    private CurrencyMap usdCurrency;
    private CurrencyMap eurCurrency;
    private CurrencyMap audCurrency;

    /**
     * Sets up the test environment before each test
     * Initializes the Admin object with mock currency data for USD, EUR, and AUD
     */
    @BeforeEach
    public void setUp() {
        // create mock currency data
        Map<String, Double> usdRates = new HashMap<>();
        Map<String, Double> eurRates = new HashMap<>();
        Map<String, Double> audRates = new HashMap<>();
        usdRates.put("EUR", 0.91);
        usdRates.put("AUD", 1.49);
        eurRates.put("USD", 1.11);
        eurRates.put("AUD", 1.68);
        audRates.put("USD", 0.67);
        audRates.put("EUR", 0.51);

        Map<String, List<ExchangeHistory>> usdHistory = new HashMap<>();
        Map<String, List<ExchangeHistory>> eurHistory = new HashMap<>();
        Map<String, List<ExchangeHistory>> audHistory = new HashMap<>();

        usdHistory.put("EUR", new ArrayList<>(List.of(new ExchangeHistory("EUR", "2024-09-11", 0.91, "Initial rate", "New"))));
        usdHistory.put("AUD", new ArrayList<>(List.of(new ExchangeHistory("AUD", "2024-09-11", 1.49, "Initial rate", "New"))));
        eurHistory.put("EUR", new ArrayList<>(List.of(new ExchangeHistory("USD", "2024-09-11", 1.11, "Initial rate", "New"))));
        eurHistory.put("AUD", new ArrayList<>(List.of(new ExchangeHistory("AUD", "2024-09-11", 1.68, "Initial rate", "New"))));
        audHistory.put("USD", new ArrayList<>(List.of(new ExchangeHistory("USD", "2024-09-11", 0.67, "Initial rate", "New"))));
        audHistory.put("EUR", new ArrayList<>(List.of(new ExchangeHistory("EUR", "2024-09-11", 0.51, "Initial rate", "New"))));

        CurrencyMap usdCurrency = new CurrencyMap("United States Dollar", "USD", usdRates, usdHistory);
        CurrencyMap eurCurrency = new CurrencyMap("Euro", "EUR", eurRates, eurHistory);
        CurrencyMap audCurrency = new CurrencyMap("Australian Dollar", "AUD", audRates, audHistory);

        currencies = new ArrayList<>();
        currencies.add(usdCurrency);
        currencies.add(eurCurrency);
        currencies.add(audCurrency);

        // create mock admin object
        admin = new Admin(currencies, "admin", "password");
    }

    /**
     * Tests the addACurrency method
     * Verifies that a new currency (British Pound) is added successfully,
     * and checks that the currency's name and symbol are correctly set.
     */
    @Test
    public void testAddACurrency() {
        // test adding a new currency
        admin.addACurrency("British Pound", "GBP");

        CurrencyMap gbpCurrency = admin.getACurrency("GBP");
        assertNotNull(gbpCurrency, "GBP currency should be added.");
        assertEquals("British Pound", gbpCurrency.getCurrencyName(), "Currency name should be British Pound.");
        assertEquals("GBP", gbpCurrency.getCurrencySymbol(), "Currency symbol should be GBP.");
    }

    /**
     * Tests the getACurrency method
     * Verifies that an existing currency (USD) can be retrieved, and checks
     * that its name is correctly returned.
     */
    @Test
    public void testGetACurrency() {
        // test getting an existing currency
        CurrencyMap usdCurrency = admin.getACurrency("USD");
        assertNotNull(usdCurrency, "USD currency should exist.");
        assertEquals("United States Dollar", usdCurrency.getCurrencyName(), "Currency name should be United States Dollar.");
    }

    /**
     * Tests the updateExchangeRate method
     * Verifies that the exchange rate between USD and EUR is correctly updated
     * and that the updated rate is accurately retrieved.
     */
    @Test
    public void testUpdateExchangeRate() {
        // test updating the exchange rate between USD and EUR
        admin.updateExchangeRate("USD", "EUR", 0.9);

        Double updatedRate = admin.getExchangeRate("USD", "EUR");
        assertEquals(0.9, updatedRate, "The updated USD to EUR exchange rate should be 0.9.");
    }

    /**
     * Tests the getExchangeRate method
     * Verifies that the exchange rate between USD and AUD is retrieved correctly.
     */
    @Test
    public void testGetExchangeRate() {
        // test getting the exchange rate between USD and AUD
        Double rate = admin.getExchangeRate("USD", "AUD");
        assertNotNull(rate, "Exchange rate between USD and AUD should exist.");
        assertEquals(1.49, rate, "USD to AUD exchange rate should be 1.49.");
    }

    /**
     * Tests the getExchangeRates method
     * Verifies that exchange rates for multiple target currencies
     */
    @Test
    public void testGetExchangeRates() {
        // test getting exchange rates for multiple currencies
        List<String> targetCurrencies = Arrays.asList("EUR", "AUD");
        List<Double> rates = admin.getExchangeRates("USD", targetCurrencies);

        assertNotNull(rates, "Rates list should not be null.");
        assertEquals(2, rates.size(), "List should have exchange rates for two currencies.");
        assertEquals(0.91, rates.get(0), "USD to EUR exchange rate should be 0.91.");
        assertEquals(1.49, rates.get(1), "USD to AUD exchange rate should be 1.49.");
    }

    /**
     * Tests the getLatestTrend method
     * Verifies that the latest trend for the exchange rate between USD and AUD
     * is retrieved and that the trend is accurately described as "New."
     */
    @Test
    public void testGetLatestTrend() {
        // test getting the latest trend for USD to AUD
        String trend = admin.getLatestTrend("USD", "AUD");

        assertNotNull(trend, "Trend for USD to AUD should exist.");
        assertEquals("New", trend, "The latest trend for USD to AUD should be 'New'.");
    }

    /**
     * Tests the findRateHistory method
     * Verifies that the historical exchange rate between USD and EUR is found
     * within a specified date range.
     */
    @Test
    public void testFindRateHistory() {
        // test finding rate history for USD to EUR
        List<Double> rateHistory = admin.findRateHistory("2024-01-01", "2024-09-15", "USD", "EUR");

        assertNotNull(rateHistory, "Rate history list should not be null.");
        assertEquals(6, rateHistory.size(), "Rate history list should contain one entry.");
        assertEquals(0.91, rateHistory.get(0), "The rate in history should be 0.91.");
    }

    /**
     * Tests the printExchangeHistory method.
     * Verifies that the exchange rate history between USD and EUR is printed
     * correctly, matching the expected output format.
     */
    @Test
    public void testPrintExchangeHistory() {
        // Redirect System.out to capture printed output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        CurrencyMap usdCurrency = admin.getACurrency("USD");

        try {
            this.admin.printExchangeHistory(usdCurrency, "EUR");
            String expectedOutput =
                    "Target Currency: EUR\n" +
                            "Date: 2024-09-11\n" +
                            "Rate: 0.91\n" +
                            "Direction: New\n" +
                            "Note: Initial rate";

            String actualOutput = outputStream.toString();

            // Trim and normalize line endings to ensure consistent comparison
            String normalizedExpected = expectedOutput.trim().replace("\r\n", "\n").replace("\r", "\n");
            String normalizedActual = actualOutput.trim().replace("\r\n", "\n").replace("\r", "\n");

            assertEquals(normalizedExpected, normalizedActual, "The printed exchange history should match the expected output.");
        } finally {
            // Reset System.out to its original state
            System.setOut(System.out);
        }
    }

    /**
     * Tests the printSummaryConvertCurrency method
     * Verifies that the summary of currency conversion between USD and EUR is
     * printed correctly, and checks that the converted amount is accurately
     * calculated.
     */
    @Test
    public void testPrintSummaryConvertCurrency() {
        // redirect System.out to read printed output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        try {
            // test random amount of 100 USD to EUR
            List<Double> result = admin.printSummaryConvertCurrency("USD", 100.0, "EUR");

            String actualOutput = outputStream.toString();

            assertTrue(actualOutput.contains("Conversion Rate (USD to EUR): 0.91"), "Should print the correct exchange rate.");
            assertTrue(actualOutput.contains("Converted Amount (USD to EUR): 91.0"), "Should print the correct converted amount.");

            assertEquals(2, result.size(), "The output list should contain 2 elements.");
            assertEquals(0.91, result.get(0), 0.001, "The exchange rate should be 0.91.");
            assertEquals(91.0, result.get(1), 0.001, "The converted amount should be 91.0.");
        } finally {
            System.setOut(System.out);
        }
    }

    /**
     * Tests the popularCurrenciesList method
     * Verifies that the most popular currencies are retrieved and that the
     * order of popularity is correct.
     */
    @Test
    public void testPopularCurrenciesList() {
        // add more mock currency data for testing popularity
        admin.addACurrency("Euro", "EUR"); // duplicate to increase popularity
        admin.addACurrency("Euro", "EUR");
        admin.addACurrency("Euro", "EUR");
        admin.addACurrency("Australian Dollar", "AUD");
        admin.addACurrency("Australian Dollar", "AUD");
        admin.addACurrency("US Dollar", "USD");
        admin.addACurrency("Japanese Yen", "JPY");

        // test getting list of popular currencies
        List<String> popularCurrencies = admin.popularCurrenciesList();

        assertNotNull(popularCurrencies, "Popular currencies list should not be null.");
        assertEquals(4, popularCurrencies.size(), "Popular currencies list should contain 4 currencies.");

        assertEquals("EUR", popularCurrencies.get(0), "EUR should be the most popular currency.");
        assertEquals("AUD", popularCurrencies.get(1), "AUD should be the second most popular currency.");
        assertEquals("USD", popularCurrencies.get(2), "USD should be the third most popular currency.");
        assertEquals("JPY", popularCurrencies.get(3), "JPY should be the fourth most popular currency.");
    }


    /**
     * Tests the formatTrend method
     * Verifies that exchange rate trends are correctly formatted as "Increased,"
     * "Decreased," or "Stable," and ensures proper handling of null input.
     */
    @Test
    public void testFormatTrend() {
        // test with different combinations of rate and trend
        Admin admin1 = new Admin(currencies,"admin1","");
        String increased = admin1.formatTrend(1.23, "Increased");
        String decreased = admin1.formatTrend(0.91, "Decreased");
        String stable = admin1.formatTrend(1.0, "Stable");
        String resultNull = admin1.formatTrend(null, null);

        assertEquals("1.23 ↑ (I)", increased, "Formatted trend should show an increase symbol.");
        assertEquals("0.91 ↓ (D)", decreased, "Formatted trend should show a decrease symbol.");
        assertEquals("1.00 - (S)", stable, "Formatted trend should show a '-' symbol.");
        assertEquals("N/A", resultNull, "Formatted trend should return N/A when input is null.");
    }

}

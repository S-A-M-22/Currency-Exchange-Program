package test;

import static org.junit.jupiter.api.Assertions.*;

import ExchangeRate.PrintSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * The PrintSummaryTest class contains unit tests for the PrintSummary class.
 * This test suite verifies the correctness of the summary statistics functions
 */
public class PrintSummaryTest {

    private List<Double> evenRates = new ArrayList<>();
    private List<Double> oddRates = new ArrayList<>();

    /**
     * Sets up the test environment before each test
     * Initializes lists of even and odd rates for testing so that the median
     * method can be correctly tested
     */
    @BeforeEach
    public void setUp() {
        evenRates.add(1.0);
        evenRates.add(2.0);
        evenRates.add(3.0);
        evenRates.add(4.0);
        evenRates.add(5.0);
        evenRates.add(6.0);
        evenRates.add(7.0);
        evenRates.add(8.0);

        oddRates.add(1.0);
        oddRates.add(2.0);
        oddRates.add(3.0);
        oddRates.add(4.0);
        oddRates.add(5.0);
        oddRates.add(6.0);
        oddRates.add(7.0);
        oddRates.add(8.0);
        oddRates.add(9.0);
    }

    /**
     * Tests the summary statistics methods with an even number of rates.
     * Verifies the correctness of the min, max, avg, median, and stddev
     * methods when applied to a list of even number of elements.
     * The rates range from 1.0 to 8.0.
     */
    @Test
    void testEven() {
        PrintSummary summary = new PrintSummary(evenRates);
        //test min
        Double expected_min = 1.0;
        assertEquals(expected_min, summary.min());

        //test max
        Double expected_max = 8.0;
        assertEquals(expected_max, summary.max());

        //test average
        Double expected_avg = 4.5;
        assertEquals(expected_avg, summary.avg());

        //test median
        Double expected_median = 4.5;
        assertEquals(expected_median, summary.median());

        //test stddev
        Double expected_stddev = 2.45;
        assertEquals(expected_stddev, summary.stddev());

    }

    /**
     * Tests the summary statistics methods with an odd number of rates.
     * Verifies the correctness of the min, max, avg, median, and stddev
     * methods when applied to a list of even number of elements.
     * The rates range from 1.0 to 9.0.
     */
    @Test
    void testOdd() {
        PrintSummary summary = new PrintSummary(oddRates);
        //test min
        Double expected_min = 1.0;
        assertEquals(expected_min, summary.min());

        //test max
        Double expected_max = 9.0;
        assertEquals(expected_max, summary.max());

        //test average
        Double expected_avg = 5.0;
        assertEquals(expected_avg, summary.avg());

        //test median
        Double expected_median = 5.0;
        assertEquals(expected_median, summary.median());

        //test stddev
        Double expected_stddev = 2.74;
        assertEquals(expected_stddev, summary.stddev());

    }

    /**
     * Tests the getter and setter methods for the rates list.
     */
    @Test
    void testGetterAndSetter() {
        PrintSummary summary = new PrintSummary(evenRates);
        List<Double> newRates = Arrays.asList(90.1, 92.3, 5.6);
        summary.setRates(newRates);
        assertEquals(newRates, summary.getRates());

    }

}



package ExchangeRate;

import java.util.*;
import java.lang.Math;

/**
 * The Print Summary class provides a way to compute and display various
 * statistical summaries for a list of exchange rates.
 */
public class PrintSummary {
    private List<Double> rates = new ArrayList<>();

    /**
     * Constructs a object with the specified list of rates.
     *
     * @param rates a list of Double values representing exchange rates.
     */
    public PrintSummary(List<Double> rates) {
        this.rates = rates;
    }

    /**
     * Returns the current list of exchange rates.
     *
     * @return list of exchange rates.
     */
    public List<Double> getRates() {
        return this.rates;
    }

    /**
     * Sets the list of exchange rates.
     *
     * @param rates a list of Double values representing exchange rates.
     */
    public void setRates(List<Double> rates) {
        this.rates = rates;
    }

    /**
     * Returns the minimum exchange rate in the list.
     *
     * @return the minimum value from the list
     */
    public Double min() {
        return Collections.min(getRates());
    }

    /**
     * Returns the maximum exchange rate in the list.
     *
     * @return the maximum value from the list
     */
    public Double max() {
        return Collections.max(getRates());
    }

    /**
     * Returns the average (mean) of the exchange rates.
     *
     * @return the average value of the exchange rates,
     * or 0 if the list is empty.
     */
    public Double avg() {
        Double sum = 0.0;
        for (Double rate : getRates()) {
            sum += rate;
        }
        return sum / getRates().size();
    }

    /**
     * Returns the median of the exchange rates.
     *
     * @return the median value of the exchange rates
     */
    public Double median() {
        List<Double> sortedRates = new ArrayList<>(getRates());
        Collections.sort(sortedRates);
        int size = sortedRates.size();

        if (size == 0) {
            return 0.0;
        }
        if (size % 2 != 0) {
            return sortedRates.get(size / 2);
        } else {
            return (sortedRates.get(size / 2 - 1) + sortedRates.get(size / 2)) / 2;
        }
    }

    /**
     * Returns the standard deviation of the exchange rates, rounded to two decimal places.
     *
     * @return the standard deviation of the exchange rates
     */
    public Double stddev() {
        List<Double> rates = getRates();
        int size = rates.size();

        if (size == 0) {
            return 0.0;
        }
        Double average = avg();
        Double sum = 0.0;

        for (Double rate : rates) {
            sum += Math.pow(rate - average, 2);
        }

        Double stddev = Math.sqrt(sum / (size - 1));
        return Math.round(stddev * 100.0) / 100.0;
    }
}

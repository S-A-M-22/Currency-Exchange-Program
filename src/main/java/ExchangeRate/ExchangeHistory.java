package ExchangeRate;


import org.json.JSONObject;
import java.util.*;

/**
 * This class is used for storing information about the exchange rate everytime there is an update to the exchange rate.
 * The class store the target currency symbol, the date of the update, the updated rate, the note of why its updated,
 * and the trend direction of the update
 */
public class ExchangeHistory {
    private String targetCurrency;
    private String date;
    private Double rate;
    private String note;
    private String direction;

    // Constructors, Getters, and Setters...
    public ExchangeHistory(String targetCurrency, String date, Double rate, String note, String direction) {
        this.targetCurrency = targetCurrency;
        this.date = date;
        this.rate = rate;
        this.note = note;
        this.direction = direction;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

}

package test;

import static org.junit.jupiter.api.Assertions.*;

import ExchangeRate.ExchangeHistory;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.time.LocalDate;

public class ExchangeHistoryTest {
    private String date = LocalDate.now().toString();
    private ExchangeRate.ExchangeHistory exchangeHistory = new ExchangeRate.ExchangeHistory("USD", date,0.67
            , "Initialise exchange rate", "Neutral");

    /**
     * Tests the getter and setter methods of the ExchangeHistory class.
     * Verifies that the initial values set through the constructor are correct,
     * then modifies each field using the setter methods and verifies that the updated
     * values are returned correctly by the getter methods.
     */
    @Test
    void testSetterandGetter() {
        assertEquals(date, exchangeHistory.getDate());
        assertEquals("USD", exchangeHistory.getTargetCurrency());
        assertEquals("Initialise exchange rate", exchangeHistory.getNote());
        assertEquals(0.67, exchangeHistory.getRate());
        assertEquals("Neutral", exchangeHistory.getDirection());

        String newDate = "2024-09-25";
        String newExchangeRate = "JPY";
        String newNote = "Neutral";
        Double newRate = 0.72;
        String newDirection = "Increased";

        exchangeHistory.setDate(newDate);
        exchangeHistory.setTargetCurrency(newExchangeRate);
        exchangeHistory.setNote(newNote);
        exchangeHistory.setRate(newRate);
        exchangeHistory.setDirection(newDirection);

        assertEquals(newDate, exchangeHistory.getDate());
        assertEquals(newExchangeRate, exchangeHistory.getTargetCurrency());
        assertEquals(newNote, exchangeHistory.getNote());
        assertEquals(newRate, exchangeHistory.getRate());
        assertEquals(newDirection, exchangeHistory.getDirection());

    }
}
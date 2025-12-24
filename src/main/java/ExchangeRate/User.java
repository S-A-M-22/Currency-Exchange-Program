package ExchangeRate;

//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.Label;
//import javafx.scene.layout.GridPane;
//import javafx.scene.text.Font;
//import javafx.scene.text.Text;
//import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class User extends Admin {

    public User(List<CurrencyMap> Currencies, String username, String password) {
        super(Currencies, username, password);
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public void updateExchangeRate(String currencyCode, String targetCurrency, double rate) { ; }

    @Override
    public void addACurrency(String CurrencyName, String CurrencyCode) { ; }


}

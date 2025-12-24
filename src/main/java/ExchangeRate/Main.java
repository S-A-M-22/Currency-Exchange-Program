package ExchangeRate;

import java.util.*;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * This class is currently used for giving example how to use the CurrencyMap and ExchangeHistory Class.
 * This class will be updated to user interface handler.
 */
public class Main extends Application {

    private static final String USER1 = "admin";
    private static final String USER2 = "normal user";
    private static final String USER3 = "user";
    private static final String PASSWORD = "password";
    private static final String ADMIN_PSWD = "superpassword";
    String filePath = "exchangeRateDatabase.json"; // Specify the path to your JSON file
    List<CurrencyMap> currencyList = CurrencyMap.loadExchangeRates(filePath);

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage mainStage) {

        Text sceneTitle = new Text("Login Page");
        Font font = Font.font("Arial", 24); // Create a new font object
        sceneTitle.setFont(font);
        mainStage.setOnCloseRequest(event -> {
            // Save the current state of the exchange rates when closing the application
            CurrencyMap.saveExchangeRates(filePath, currencyList);
        });
        // Show the window.
        mainStage.show();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.add(sceneTitle, 0, 1, 2, 1);
        // Create labels for username and password fields.
        Label username = new Label("Username: ");
        grid.add(username, 0, 3);
        Label password = new Label("Password: ");
        grid.add(password, 0, 4);
        // Create text input fields for username and password.
        TextField usernameField = new TextField();
        grid.add(usernameField, 1, 3);
        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 4);
        // Create a "Login" button.
        grid.setVgap(20);
        Button loginButton = new Button("Login");
        grid.add(loginButton, 1, 5);
        // Create the scene and set it in the stage.
        Scene scene = new Scene(grid, 600, 600);
        mainStage.setScene(scene);
        // Set an action for the "Login" button to validate the credentials.
        loginButton.setOnAction(e -> {
            String enteredUsername = usernameField.getText();
            String enteredPassword = passwordField.getText();
            if (enteredUsername.equalsIgnoreCase(USER1) && enteredPassword.equals(ADMIN_PSWD)) {
                loginButton.setText("Login successful!");
                Admin admin = new Admin(currencyList, "admin", "");
                adminInterface(mainStage, admin);
            } else if (enteredUsername.equalsIgnoreCase(USER2) || enteredUsername.equalsIgnoreCase(USER3)) {
                grid.setHgap(20);
                grid.setVgap(20);
                if (enteredPassword.equals(PASSWORD)) {
                    loginButton.setText("Login successful!");
                    User user = new User(currencyList, "user", "password");
                    userInterface(mainStage, user);
                } else {
                    loginButton.setText("Login failed. Please check your credentials.");
                }

            } else {
                loginButton.setText("Login failed. Please check your credentials.");
            }

        });
        
    }

    // Admin Interface
    private void adminInterface(Stage stage, Admin admin) {

        Text sceneTitle = new Text("PROFILE: ADMIN");
        Font font = Font.font("Arial", 24); // Create a new font object
        sceneTitle.setFont(font);
        // Show the window.
        stage.show();

        // Create a grid pane to organize labels and text fields
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(30);
        grid.add(sceneTitle, 1, 1);

        // Button to add currency
        Button addCurrencyButton = new Button("Add New Currency");
        grid.add(addCurrencyButton, 1, 3);
        addCurrencyButton.setOnAction(e -> addNewCurrency(admin));

        // Button to update exchange rates
        Button updateRatesButton = new Button("Update Exchange Rates");
        grid.add(updateRatesButton, 1, 4);
        updateRatesButton.setOnAction(e -> updateExchangeRates(admin));

        // Button to view exchange history summary
        Button convertButton = new Button("Convert Currencies");
        grid.add(convertButton, 1, 5);
        convertButton.setOnAction(e -> showConversionInterface(admin));

        // Button to view exchange history summary
        Button viewHistoryButton = new Button("View Popular Currencies");
        grid.add(viewHistoryButton, 1, 6);
        viewHistoryButton.setOnAction(e -> showPopularCurrencies(admin));

        Button viewExchangeHistoryButton = new Button("View History");
        grid.add(viewExchangeHistoryButton, 1, 7);
        viewExchangeHistoryButton.setOnAction(e -> viewExchangeHistory(admin));

        Scene adminScene = new Scene(grid, 600, 600);
        stage.setScene(adminScene);
        stage.show();
    }

    // Normal User Interface
    private void userInterface(Stage stage, User user) {

        Text sceneTitle = new Text("PROFILE: USER");
        Font font = Font.font("Arial", 24); // Create a new font object
        sceneTitle.setFont(font);

        stage.show();

        // Create a grid pane to organize labels and text fields
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(30);
        grid.setVgap(30);
        grid.add(sceneTitle, 1, 1);

        // Button to convert currency
        Button convertCurrencyButton = new Button("Convert Currency");
        grid.add(convertCurrencyButton, 1, 2);
        convertCurrencyButton.setOnAction(e -> showConversionInterface(user));

        // Button to view popular currencies
        Button viewPopularCurrenciesButton = new Button("View Popular Currencies");
        grid.add(viewPopularCurrenciesButton, 1, 3);
        viewPopularCurrenciesButton.setOnAction(e -> showPopularCurrencies(user));

        Button viewExchangeHistoryButton = new Button("View History");
        grid.add(viewExchangeHistoryButton, 1, 4);
        viewExchangeHistoryButton.setOnAction(e -> viewExchangeHistory(user));

        Scene userScene = new Scene(grid, 600, 600);
        stage.setScene(userScene);
        stage.show();
    }

    // Conversion Interface for Users
    private void showConversionInterface(Admin user) {
        // Show the window.
        Stage convertStage = new Stage();
        convertStage.setTitle("CONVERT CURRENCIES");

        // Create a grid pane to organize labels and text fields
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(20);
        grid.setHgap(10);

        Label currencyCode = new Label("Write Currency <ISO 4217> Code (From): ");
        TextField currencyField = new TextField();
        grid.add(currencyCode, 0, 0);
        grid.add(currencyField, 1, 0);

        Label amount = new Label("Enter Amount: ");
        TextField amtField = new TextField();
        grid.add(amount, 0, 1);
        grid.add(amtField, 1, 1);

        Label currencyTo = new Label("Enter Currency <ISO 4217> Code (To): ");
        TextField currencyToField = new TextField();
        grid.add(currencyTo, 0, 2);
        grid.add(currencyToField, 1, 2);

        Button submitButton = new Button("Submit");
        grid.add(submitButton, 1, 3);

        submitButton.setOnAction(e -> {
            String FromCurrency = currencyField.getText().trim().toUpperCase();
            String ToCurrency = currencyToField.getText().trim().toUpperCase();
            String amt = amtField.getText().trim();

            // Clear existing labels before adding new ones
            grid.getChildren().removeIf(node -> GridPane.getRowIndex(node) >= 0);  // Adjust row index as needed

            // Check if the fields are empty
            if (FromCurrency.isEmpty() || ToCurrency.isEmpty() || amt.isEmpty()) {
                Label warning = new Label("Please enter both the currency codes and the amount.");
                grid.add(warning, 1, 3);
            } else if (user.getACurrency(FromCurrency) == null || user.getACurrency(ToCurrency) == null) {
                Label warning = new Label("The currency doesn't exist in our data");
                grid.add(warning, 1, 3);
            }

            // Try parsing the exchange rate, handling the exception
            try {
                double convertAmt = Double.parseDouble(amt);

                // Update the exchange rate using the admin object
                List<Double> out = user.printSummaryConvertCurrency(FromCurrency, convertAmt, ToCurrency);

                // Display the updated rate
                Text title = new Text("CONVERSION SUMMARY");
                title.setFont(Font.font("Arial", 24));
                grid.add(title, 0, 1);
                Label writeRate = new Label("Conversion Rate (" + FromCurrency + " to " + ToCurrency + "): " + out.get(0));
                Label convertedAmount = new Label("Converted Amount (" + FromCurrency + " to " + ToCurrency + "): " + out.get(1));
                grid.add(writeRate, 0, 2);
                grid.add(convertedAmount, 0, 3);

            } catch (NumberFormatException ex) {
                Label warning = new Label("Invalid exchange rate. Please enter a valid number.");
                grid.add(warning, 2, 3);
            }

        });

        convertStage.close();

        // Set the scene
        Scene scene = new Scene(grid, 600, 700);
        convertStage.setScene(scene);
        convertStage.show();


    }

    // Display Popular Currencies (4x4 grid)
    private void showPopularCurrencies(Admin admin) {
        // Logic for displaying the 4x4 grid with most popular currencies
        List<String> currenciesList = admin.popularCurrenciesList();

        // Logic for adding a new currency
        Stage tableStage = new Stage();
        tableStage.setTitle("Popular Currencies Table");

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);

        Text title = new Text("Popular Currencies Table");
        title.setFont(Font.font("Arial", 24));
        title.setTextAlignment(TextAlignment.CENTER);

        // Create a grid pane to organize labels and text fields
        GridPane table = new GridPane();
        table.setAlignment(Pos.BOTTOM_CENTER);

        // Add the header labels (From/To, AUD, SGD, US, EU)
        Label corner = new Label("From/To");
        corner.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)
        ));
        corner.setMinWidth(100);
        corner.setMinHeight(50);
        corner.setAlignment(Pos.CENTER);
        table.add(corner, 0, 0); // Top left corner

        for (int i = 0; i < currenciesList.size(); i++) {
            Label colheading = new Label(currenciesList.get(i));
            colheading.setBorder(new Border(
                    new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)
            ));
            colheading.setMinWidth(100);
            colheading.setMinHeight(50);
            colheading.setAlignment(Pos.CENTER);
            table.add(colheading, i + 1, 0); // Column headers

            Label rowheading = new Label(currenciesList.get(i));
            rowheading.setBorder(new Border(
                    new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)
            ));

            rowheading.setMinWidth(100);
            rowheading.setMinHeight(50);
            rowheading.setAlignment(Pos.CENTER);
            table.setBorder(new Border(
                    new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)
            ));
            table.add(rowheading, 0, i + 1); // Row headers

        }

        // Add the data to the grid
        for (int i = 0; i < currenciesList.size(); i++) {
            Label val;
            for (int j = 0; j < currenciesList.size(); j++) {
                if (currenciesList.get(i).equals(currenciesList.get(j))) {
                    val = new Label("-");
                } else {
                    Double exchangeRate = admin.getExchangeRate(currenciesList.get(i), currenciesList.get(j));
                    String trend = admin.getLatestTrend(currenciesList.get(i), currenciesList.get(j));
                    val = new Label(admin.formatTrend(exchangeRate, trend));
                }

                val.setMinWidth(100);
                val.setMinHeight(50);
                val.setAlignment(Pos.CENTER);
                val.setBorder(new Border(
                        new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)
                ));
                table.add(val, j + 1, i + 1);
            }
        }

        // Add the title and table to the VBox
        vbox.getChildren().addAll(title, table);

        Scene scene = new Scene(vbox, 600, 700);
        tableStage.setScene(scene);
        tableStage.show();

    }

    // Admin: Add New Currency
    private void addNewCurrency(Admin admin) {
        // Logic for adding a new currency
        Stage addCurrencyStage = new Stage();
        addCurrencyStage.setTitle("Add New Currency");

        // Create a grid pane to organize labels and text fields
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        Label currencyCode = new Label("Enter Currency <ISO 4217> Code (you want to add): ");
        TextField currencyField = new TextField();
        grid.add(currencyCode, 0, 0);
        grid.add(currencyField, 1, 0);

        Label currencyName = new Label("Enter its respective currency name: ");
        TextField nameField = new TextField();
        grid.add(currencyName, 0, 1);
        grid.add(nameField, 1, 1);

        Button submitButton = new Button("Submit");
        grid.add(submitButton, 1, 2);

        //Logic to add the new currency to the database
        submitButton.setOnAction(e -> {
            if (currencyField.getText().isEmpty() || nameField.getText().isEmpty()) {
                Label warning = new Label("Please enter both the currency code and name.");
                grid.add(warning, 1, 3);
            }
            else {
                CurrencyMap currencymap = admin.getACurrency(currencyField.getText().toUpperCase().trim());
                if (currencymap == null) {
                    admin.addACurrency(nameField.getText().trim(), currencyField.getText().trim().toUpperCase());
                    Label name = new Label("Newly Added Currency: " +
                            admin.getACurrency(currencyField.getText().trim().toUpperCase()).getCurrencySymbol());
                    grid.add(name, 1, 3);
                    CurrencyMap.saveExchangeRates(filePath, currencyList);
                }
                else{
                    Label outcome = new Label("This currency already exists in our database.");
                    grid.add(outcome, 1, 3);
                }
            }

        });

        addCurrencyStage.close();

        Scene scene = new Scene(grid, 600, 700);
        addCurrencyStage.setScene(scene);
        addCurrencyStage.show();
    }

    // Admin: Update Exchange Rates
    private void updateExchangeRates(Admin admin) {

        Stage updateStage = new Stage();
        updateStage.setTitle("Update Exchange Rates");

        // Create a grid pane to organize labels and text fields
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        // Logic for updating exchange rates and maintaining history
        Label currencyFrom = new Label("Enter Currency <ISO 4217> Code (From): ");
        TextField currencyFromField = new TextField();
        grid.add(currencyFrom, 0, 0);
        grid.add(currencyFromField, 1, 0);
        Label currencyTo = new Label("Enter Target Currency <ISO 4217> Code (To): ");
        TextField currencyToField = new TextField();
        grid.add(currencyTo, 0, 1);
        grid.add(currencyToField, 1, 1);
        Label exchangeRate = new Label("Enter Updated Exchange Rate: ");
        TextField exchangeRateField = new TextField();
        grid.add(exchangeRate, 0, 2);
        grid.add(exchangeRateField, 1, 2);

        Button submitButton = new Button("Update");
        grid.add(submitButton, 1, 3);

        submitButton.setOnAction(e -> {
            // Get user input from the text fields
            String fromCurrency = currencyFromField.getText().trim();
            String toCurrency = currencyToField.getText().trim();
            String rateText = exchangeRateField.getText().trim();

            // Check if the fields are empty
            if (fromCurrency.isEmpty() || toCurrency.isEmpty() || rateText.isEmpty()) {
                Label warning = new Label("Please enter both the currency codes and the exchange rate.");
                grid.add(warning, 1, 4);
                return;  // Exit the event handler if the fields are empty
            }

            // Try parsing the exchange rate, handling the exception
            try {
                double rate = Double.parseDouble(rateText);

                // Update the exchange rate using the admin object
                admin.updateExchangeRate(fromCurrency.toUpperCase(), toCurrency.toUpperCase(), rate);

                // Display the updated rate
                Label name = new Label("The exchange rate between " + fromCurrency.toUpperCase() +
                        " and " + toCurrency.toUpperCase() + " is now " +
                        admin.getExchangeRate(fromCurrency.toUpperCase(), toCurrency.toUpperCase()));
                grid.add(name, 1, 4);
                CurrencyMap.saveExchangeRates(filePath, currencyList);
            } catch (NumberFormatException ex) {
                Label warning = new Label("Invalid exchange rate. Please enter a valid number.");
                grid.add(warning, 1, 4);
            }
        });


        //close updateStage window
        updateStage.close();

        Scene scene = new Scene(grid, 600, 600);
        updateStage.setScene(scene);
        updateStage.show();
    }

    // Admin: View Exchange History and Statistics
    private void viewExchangeHistory(Admin user) {
        Stage convertStage = new Stage();
        convertStage.setTitle("Exchange History");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(20);
        grid.setHgap(10);

        Label currencyCode = new Label("Write Currency <ISO 4217> Code (From): ");
        TextField currencyField = new TextField();
        grid.add(currencyCode, 0, 0);
        grid.add(currencyField, 1, 0);

        Label currencyTo = new Label("Enter Currency <ISO 4217> Code (To): ");
        TextField currencyToField = new TextField();
        grid.add(currencyTo, 0, 1);
        grid.add(currencyToField, 1, 1);

        Label startDateLabel = new Label("Enter Start Date: ");
        TextField startDateField = new TextField();
        grid.add(startDateLabel, 0, 2);
        grid.add(startDateField, 1, 2);

        Label endDateLabel = new Label("Enter End Date: ");
        TextField endDateField = new TextField();
        grid.add(endDateLabel, 0, 3);
        grid.add(endDateField, 1, 3);

        Button submitButton = new Button("Submit");
        grid.add(submitButton, 1, 4);

        submitButton.setOnAction(e -> {
            String FromCurrency = currencyField.getText().trim().toUpperCase();
            String ToCurrency = currencyToField.getText().trim().toUpperCase();
            String startDate = startDateField.getText().trim();
            String endDate = endDateField.getText().trim();

            // Clear only previous result entries starting from row index 5 onward
            grid.getChildren().removeIf(node -> GridPane.getRowIndex(node) >= 5);

            if (FromCurrency.isEmpty() || ToCurrency.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                Label warning = new Label("Please enter both the currency codes and start and end date.");
                grid.add(warning, 0, 5, 2, 1);  // Span two columns to fit neatly
            } else if (user.getACurrency(FromCurrency) == null || user.getACurrency(ToCurrency) == null) {
                Label warning = new Label("The currency doesn't exist in our data.");
                grid.add(warning, 0, 5, 2, 1);
            } else {
                try {
                    // Fetching rate data using the findRateHistory method from the Admin object
                    List<Double> out = user.findRateHistory(startDate, endDate, FromCurrency, ToCurrency);

                    int resultRowStart = 6; // Starting row index for displaying results

                    // Adding result labels, ensuring they don't overlap
                    grid.add(new Text("Exchange History"), 0, resultRowStart++);
                    grid.add(new Label("Minimum: " + out.get(0)), 0, resultRowStart++);
                    grid.add(new Label("Maximum: " + out.get(1)), 0, resultRowStart++);
                    grid.add(new Label("Average: " + out.get(2)), 0, resultRowStart++);
                    grid.add(new Label("Median: " + out.get(3)), 0, resultRowStart++);
                    grid.add(new Label("Standard Deviation: " + out.get(4)), 0, resultRowStart++);

                    // Concatenating the remaining rates into a single label
                    StringBuilder ratesString = new StringBuilder("Rates: ");
                    for (int i = 5; i < out.size(); i++) {
                        ratesString.append(out.get(i)).append(", ");
                    }
                    if (ratesString.length() > 7) {
                        ratesString.setLength(ratesString.length() - 2);
                    }

                    grid.add(new Label(ratesString.toString()), 0, resultRowStart++);

                } catch (NumberFormatException ex) {
                    Label warning = new Label("Invalid exchange rate. Please enter a valid number.");
                    grid.add(warning, 0, 5, 2, 1);
                }
            }
        });

        Scene scene = new Scene(grid, 600, 700);
        convertStage.setScene(scene);
        convertStage.show();
    }
}

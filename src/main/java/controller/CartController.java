package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import service.ItemService;
import service.LocalizationService;
import service.CartService;

import java.util.Locale;
import java.util.Map;

public class CartController {
    @FXML
    public TextField amountOfAllField;
    @FXML
    public TextField piecesField;
    @FXML
    public TextField priceField;
    @FXML
    public Button arButton;
    @FXML
    public Button scanButton;
    @FXML
    public Label itemNumLabel;
    @FXML
    public Button addItemButton;
    @FXML
    public Label result;
    @FXML
    public Label titleLabel;

    int amount;
    int newAmount;
    double totalPrice;
    int totalItems;
    int cartId;

    private Locale currentLocale =  new Locale("en", "US");
    private Map<String, String> localizedStrings;

    public void initialize(){
        setLanguage(currentLocale);
        titleLabel.setText(localizedStrings.getOrDefault("title", "Shopping Cart Calculator"));
        amountOfAllField.setPromptText(localizedStrings.getOrDefault("amount", "Amount of different products:" ));
        piecesField.setPromptText(localizedStrings.getOrDefault("pieces", "How many pieces:" ));
        priceField.setPromptText(localizedStrings.getOrDefault("cost", "Price per piece:" ));

        amount = 0;
        totalPrice = 0;
        amountOfAllField.clear();
        piecesField.clear();
        priceField.clear();
        piecesField.setDisable(true);
        priceField.setDisable(true);
        itemNumLabel.setVisible(false);
        addItemButton.setDisable(true);
    }

    private void setLanguage(Locale locale) {
        currentLocale = locale;
        //result.setText(""); // Clear previous result

        // Load localized strings
        localizedStrings = LocalizationService.getLocalizedStrings(locale);

        // Update all UI text
        titleLabel.setText(localizedStrings.getOrDefault("title", "Shopping Cart Calculator"));
        amountOfAllField.setPromptText(localizedStrings.getOrDefault("amount", "Amount of different products:" ));
        piecesField.setPromptText(localizedStrings.getOrDefault("pieces", "How many pieces:" ));
        priceField.setPromptText(localizedStrings.getOrDefault("cost", "Total price:" ));
        addItemButton.setText(localizedStrings.getOrDefault("add_button", "Add item:" ));
        scanButton.setText(localizedStrings.getOrDefault("scan_button", "Start Scanning:" ));
        itemNumLabel.setVisible(false);
        result.setText(localizedStrings.getOrDefault("results_def", "Results are shown here"));


    }

    public void onClickChangetoEn() {
        setLanguage(new Locale("en", "US"));
    }

    public void onClickChangetoFi() {
        setLanguage(new Locale("fi", "FI"));
    }
    public void onClickChangetoSv() {
        setLanguage(new Locale("sv", "SE"));
    }

    public void onClickChangetoJa() {
        setLanguage(new Locale("ja", "JP"));
    }

    public void onClickChangetoAr() {
        setLanguage(new Locale("ar", "IQ"));

    }

    public void onClickStartScan() {
        amount =  Integer.parseInt(amountOfAllField.getText());
        newAmount = amount-2;
        result.setText((localizedStrings.getOrDefault("addItems", "Add items to see the total price!")));
        itemNumLabel.setVisible(true);
        addItemButton.setDisable(false);
        piecesField.setDisable(false);
        priceField.setDisable(false);
        priceField.clear();
        piecesField.clear();
        itemNumLabel.setText(1 + localizedStrings.getOrDefault("itemNum", ". item:"));
        totalPrice = 0;
        cartId = CartService.saveRecord(0,0, currentLocale.getLanguage());

    }
    int itemnumber = 0;
    public void onClickAddItem() {

        double itemPrice = 0;
        int quantity = 0;
        try {
            double price = Double.parseDouble(priceField.getText());
            quantity = Integer.parseInt(piecesField.getText());
            itemPrice = calcPrice(price, quantity);
            if (itemPrice == -1.0) {
                throw new NumberFormatException();
            }
            calcTotalPrice(itemPrice);
            result.setText(totalPrice + localizedStrings.getOrDefault("sign", "$"));
            // for setting the number of item we are on at the moment
            if (newAmount >= 0) {
                itemNumLabel.setText(amount - newAmount + localizedStrings.getOrDefault("itemNum", ". item:"));
                newAmount -= 1;
            } else {
                addItemButton.setDisable(true);
            }
            totalItems += quantity;
            itemnumber += 1;
            ItemService.saveItem(cartId, itemnumber, itemPrice, quantity, totalPrice);
        } catch (NumberFormatException e) {
            result.setText(localizedStrings.getOrDefault("error_invalid_input", "Please enter valid numbers"));
        }


    }

    // Test this one:
    public Double calcPrice(double  price, int quantity) {
        if (price < 0 || quantity < 0) {
            return -1.0;
        }
        return price * quantity;
    }
    // and test this:
    public void calcTotalPrice(double addedPrice) {
        totalPrice += addedPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }


    public void onClickSaveToDB() {
        CartService.updateRecord(totalItems, totalPrice, cartId);
    }
}

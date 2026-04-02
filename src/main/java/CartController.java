import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import service.LocalizationService;

import java.util.Locale;
import java.util.Map;

public class CartController {

    public Button enButton;
    public Button fiButton;
    public Button svButton;
    public Button jaButton;
    public TextField amountOfAllField;
    public TextField piecesField;
    public TextField priceField;
    public Button arButton;
    public Button scanButton;
    public Label itemNumLabel;
    public Button addItemButton;
    public Label result;
    public Label titleLabel;

    int amount;
    int newAmount;
    double totalPrice;
    int totalItems;
    int cart_id;

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

        // Update time display with new locale
        //displayLocalTime(locale);

        // Apply text direction based on language

    }

    public void onClickChangetoEn(ActionEvent actionEvent) {
        setLanguage(new Locale("en", "US"));
    }

    public void onClickChangetoFi(ActionEvent actionEvent) {
        setLanguage(new Locale("fi", "FI"));
    }
    public void onClickChangetoSv(ActionEvent actionEvent) {
        setLanguage(new Locale("sv", "SE"));
    }

    public void onClickChangetoJa(ActionEvent actionEvent) {
        setLanguage(new Locale("ja", "JP"));
    }

    public void onClickChangetoAr(ActionEvent actionEvent) {
        setLanguage(new Locale("ar", "IQ"));

    }

    public void onClickStartScan(ActionEvent actionEvent) {
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
        cart_id = CartService.saveRecord(0,0, currentLocale.getLanguage());

    }
    int itemnumber = 0;
    public void onClickAddItem(ActionEvent actionEvent) {

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
            ItemService.saveItem(cart_id, itemnumber, itemPrice, quantity, totalPrice);
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


    public void onClickSaveToDB(ActionEvent actionEvent) {
        CartService.updateRecord(totalItems, totalPrice, cart_id);
    }
}

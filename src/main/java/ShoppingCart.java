import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ShoppingCart {
    public  static void main(String[] args) {
        /*String lang = "fa";
        String country = "AF";
        Locale locale = new Locale(lang, country);
        ResourceBundle rb = ResourceBundle.getBundle("MessagesBundle", locale);
        String wish = rb.getString("wish");
        System.out.println( wish);
   */

        Scanner input = new Scanner(System.in);
        System.out.println("Select language: ");
        System.out.println("1. English");
        System.out.println("2. Finnish");
        System.out.println("3. Japanese");
        System.out.println("4. Swedish");
        String choise = input.nextLine();
        Locale locale;

        String language = "";
        String country = "";
        switch (choise) {
            case "1":
                language = "en";
                country = "UK";
                break;
            case "2":
                language = "fi";
                country = "FI";
                break;
            case "3":
                language = "jp";
                country = "JA";
                break;
            case "4":
                language = "sv";
                country = "SE";
                break;

        }
        locale = new Locale(language, country);
        ResourceBundle rb = ResourceBundle.getBundle("MessagesBundle", locale);

        String wish =  rb.getString("wish");
        String prompt1 = rb.getString("prompt1");
        String prompt2 = rb.getString("prompt2");
        String prompt3 = rb.getString("prompt3");
        String total =  rb.getString("total");

        System.out.println(wish);
        Scanner scanner =  new Scanner(System.in);
        System.out.println(prompt1);
        double totalPrice = 0;
        double amount =  scanner.nextDouble();
        for (int i = 1; i <= amount; i++) {
            System.out.println(prompt2);
            double amountOfThis =  scanner.nextDouble();
            System.out.println(prompt3);
            double priceOfOne =   scanner.nextDouble();
            totalPrice += calcTotalPrice(priceOfOne, amountOfThis);
            System.out.println(total + totalPrice);
        }


    }

    public static double calcTotalPrice(double price, double amount) {
        return price * amount;
    }


}

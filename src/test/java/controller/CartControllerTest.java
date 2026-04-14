package controller;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class CartControllerTest {

    private CartController cartController;

    @BeforeAll
    static void startJavaFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException e) {
            // JavaFX already started
            latch.countDown();
        }

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }
    @BeforeEach
    void setUp() throws InterruptedException {
        cartController = new CartController();

        runOnFxThread(() -> {
            cartController.amountOfAllField = new TextField();
            cartController.piecesField = new TextField();
            cartController.priceField = new TextField();
            cartController.arButton = new Button();
            cartController.scanButton = new Button();
            cartController.itemNumLabel = new Label();
            cartController.addItemButton = new Button();
            cartController.result = new Label();
            cartController.titleLabel = new Label();
        });
    }

    @Test
    void initialize_shouldSetDefaultEnglishTextsAndState() throws InterruptedException {
        runOnFxThread(() -> cartController.initialize());

        runOnFxThread(() -> {
            assertEquals("Shopping Cart Calculator", cartController.titleLabel.getText());
            assertEquals("Amount of different products:", cartController.amountOfAllField.getPromptText());
            assertEquals("How many pieces:", cartController.piecesField.getPromptText());
            assertEquals("Price per piece:", cartController.priceField.getPromptText());

            assertTrue(cartController.piecesField.isDisabled());
            assertTrue(cartController.priceField.isDisabled());
            assertTrue(cartController.addItemButton.isDisabled());
            assertFalse(cartController.itemNumLabel.isVisible());

            assertEquals("", cartController.amountOfAllField.getText());
            assertEquals("", cartController.piecesField.getText());
            assertEquals("", cartController.priceField.getText());
        });
    }

    @Test
    void onClickChangetoFi_shouldUpdateTexts() throws InterruptedException {
        runOnFxThread(() -> {
            cartController.initialize();
            cartController.onClickChangetoFi();
        });

        runOnFxThread(() -> {
            assertNotNull(cartController.titleLabel.getText());
            assertNotNull(cartController.amountOfAllField.getPromptText());
            assertNotNull(cartController.result.getText());

            assertFalse(cartController.itemNumLabel.isVisible());
        });
    }

    @Test
    void onClickChangetoEn_shouldUpdateTexts() throws InterruptedException {
        runOnFxThread(() -> {
            cartController.initialize();
            cartController.onClickChangetoEn();
        });

        runOnFxThread(() -> {
            assertEquals("Shopping Cart Calculator", cartController.titleLabel.getText());
            assertEquals("Amount of different products:", cartController.amountOfAllField.getPromptText());
            assertEquals("How many pieces:", cartController.piecesField.getPromptText());
            assertEquals("Add item:", cartController.addItemButton.getText());
            assertEquals("Start Scanning:", cartController.scanButton.getText());
            assertEquals("Results are shown here", cartController.result.getText());
            assertFalse(cartController.itemNumLabel.isVisible());
        });
    }

    private void runOnFxThread(Runnable action) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                action.run();
            } finally {
                latch.countDown();
            }
        });
        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    void calcPrice_shouldReturnCorrectPrice_whenInputsAreValid() {
        Double result = cartController.calcPrice(2.5, 4);

        assertEquals(10.0, result);
    }

    @Test
    void calcPrice_shouldReturnZero_whenPriceIsZero() {
        Double result = cartController.calcPrice(0, 5);

        assertEquals(0.0, result);
    }

    @Test
    void calcPrice_shouldReturnZero_whenQuantityIsZero() {
        Double result = cartController.calcPrice(10.0, 0);

        assertEquals(0.0, result);
    }

    @Test
    void calcPrice_shouldReturnMinusOne_whenPriceIsNegative() {
        Double result = cartController.calcPrice(-2.0, 3);

        assertEquals(-1.0, result);
    }

    @Test
    void calcPrice_shouldReturnMinusOne_whenQuantityIsNegative() {
        Double result = cartController.calcPrice(2.0, -3);

        assertEquals(-1.0, result);
    }

    @Test
    void calcTotalPrice_shouldAddOnePriceCorrectly() {
        cartController.calcTotalPrice(12.5);

        assertEquals(12.5, cartController.getTotalPrice());
    }

    @Test
    void calcTotalPrice_shouldAddMultiplePricesCorrectly() {
        cartController.calcTotalPrice(10.0);
        cartController.calcTotalPrice(5.5);
        cartController.calcTotalPrice(4.5);

        assertEquals(20.0, cartController.getTotalPrice());
    }

    @Test
    void calcTotalPrice_shouldKeepZero_whenAddingZero() {
        cartController.calcTotalPrice(0.0);

        assertEquals(0.0, cartController.getTotalPrice());
    }
    @Test
    void calcPrice_returnsCorrectValue() {
        CartController controller = new CartController();
        assertEquals(10.0, controller.calcPrice(2.0, 5));
    }

    @Test
    void calcPrice_returnsMinusOne_whenPriceIsNegative() {
        CartController controller = new CartController();
        assertEquals(-1.0, controller.calcPrice(-2.0, 5));
    }

    @Test
    void calcPrice_returnsMinusOne_whenQuantityIsNegative() {
        CartController controller = new CartController();
        assertEquals(-1.0, controller.calcPrice(2.0, -5));
    }
    @Test
    void calcTotalPrice_addsToTotal() {
        CartController controller = new CartController();
        controller.calcTotalPrice(10.0);
        assertEquals(10.0, controller.getTotalPrice());
    }

    @Test
    void calcTotalPrice_addsMultipleValues() {
        CartController controller = new CartController();
        controller.calcTotalPrice(10.0);
        controller.calcTotalPrice(5.0);
        assertEquals(15.0, controller.getTotalPrice());
    }
}
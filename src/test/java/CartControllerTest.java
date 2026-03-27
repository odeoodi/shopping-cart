import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartControllerTest {

    CartController cartController = new CartController();

    @Test
    void testCalcPrice_normal() {
        Double result = cartController.calcPrice(10.0, 3);
        assertEquals(30.0, result);
    }

    @Test
    void testCalcPrice_zero() {
        Double result = cartController.calcPrice(0.0, 5);
        assertEquals(0.0, result);
    }

    @Test
    void testCalcPrice_negativePrice() {
        Double result = cartController.calcPrice(-5.0, 3);
        assertEquals(-1.0, result);
    }

    @Test
    void testCalcPrice_negativeQuantity() {
        Double result = cartController.calcPrice(5.0, -3);
        assertEquals(-1.0, result);
    }

    @Test
    void testCalcTotalPrice_singleAddition() {
        cartController.calcTotalPrice(10.0);
        assertEquals(10.0, cartController.getTotalPrice());
    }

    @Test
    void testCalcTotalPrice_multipleAdditions() {
        cartController.calcTotalPrice(10.0);
        cartController.calcTotalPrice(5.0);
        cartController.calcTotalPrice(5.0);
        assertEquals(20.0, cartController.getTotalPrice());
    }
}
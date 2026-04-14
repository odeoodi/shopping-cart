package service;

import connection.DatabaseConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ItemServiceTest {

    @BeforeEach
    void setUp() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            try (PreparedStatement deleteItems =
                         conn.prepareStatement("DELETE FROM cart_items")) {
                deleteItems.executeUpdate();
            }

            try (PreparedStatement deleteCarts =
                         conn.prepareStatement("DELETE FROM cart_records")) {
                deleteCarts.executeUpdate();
            }

            try (PreparedStatement resetCartItems =
                         conn.prepareStatement("ALTER TABLE cart_items AUTO_INCREMENT = 1")) {
                resetCartItems.executeUpdate();
            } catch (SQLException ignored) {
                // Some databases may not support this
            }

            try (PreparedStatement resetCartRecords =
                         conn.prepareStatement("ALTER TABLE cart_records AUTO_INCREMENT = 1")) {
                resetCartRecords.executeUpdate();
            } catch (SQLException ignored) {
                // Some databases may not support this
            }
        }
    }

    @Test
    void saveItem_shouldInsertItemIntoDatabase() throws SQLException {
        int cartId = CartService.saveRecord(0, 0.0, "en");
        assertTrue(cartId > 0);

        ItemService.saveItem(cartId, 1, 12.50, 3, 37.50);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT cart_record_id, item_number, price, quantity, subtotal " +
                             "FROM cart_items WHERE cart_record_id = ?")) {

            stmt.setInt(1, cartId);

            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(cartId, rs.getInt("cart_record_id"));
                assertEquals(1, rs.getInt("item_number"));
                assertEquals(12.50, rs.getDouble("price"), 0.001);
                assertEquals(3, rs.getInt("quantity"));
                assertEquals(37.50, rs.getDouble("subtotal"), 0.001);
            }
        }
    }

    @Test
    void saveItem_shouldInsertMultipleItemsForSameCart() throws SQLException {
        int cartId = CartService.saveRecord(0, 0.0, "en");
        assertTrue(cartId > 0);

        ItemService.saveItem(cartId, 1, 10.00, 2, 20.00);
        ItemService.saveItem(cartId, 2, 5.50, 4, 42.00);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM cart_items WHERE cart_record_id = ?")) {

            stmt.setInt(1, cartId);

            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(2, rs.getInt(1));
            }
        }
    }

    @Test
    void saveItem_shouldNotThrowWhenValuesAreZero() {
        int cartId = CartService.saveRecord(0, 0.0, "en");
        assertTrue(cartId > 0);

        assertDoesNotThrow(() ->
                ItemService.saveItem(cartId, 0, 0.0, 0, 0.0)
        );
    }
}

package service;

import connection.DatabaseConnection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class CartServiceTest {

    @BeforeEach
    void setUp() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            try (PreparedStatement deleteItems =
                         conn.prepareStatement("DELETE FROM items")) {
                deleteItems.executeUpdate();
            } catch (SQLException ignored) {
                // Ignore if items table does not exist or is not needed
            }

            try (PreparedStatement deleteCarts =
                         conn.prepareStatement("DELETE FROM cart_records")) {
                deleteCarts.executeUpdate();
            }

            try (PreparedStatement resetAutoIncrement =
                         conn.prepareStatement("ALTER TABLE cart_records AUTO_INCREMENT = 1")) {
                resetAutoIncrement.executeUpdate();
            } catch (SQLException ignored) {
                // Some databases may not support this
            }
        }
    }

    @Test
    void saveRecord_shouldReturnGeneratedId_whenInsertSucceeds() throws SQLException {
        int id = CartService.saveRecord(3, 19.99, "en");

        assertTrue(id > 0);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT total_items, total_cost, language FROM cart_records WHERE id = ?")) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(3, rs.getInt("total_items"));
                assertEquals(19.99, rs.getDouble("total_cost"), 0.001);
                assertEquals("en", rs.getString("language"));
            }
        }
    }

    @Test
    void updateRecord_shouldUpdateExistingCartRecord() throws SQLException {
        int id = CartService.saveRecord(1, 5.00, "fi");
        assertTrue(id > 0);

        CartService.updateRecord(7, 42.50, id);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT total_items, total_cost FROM cart_records WHERE id = ?")) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(7.0, rs.getDouble("total_items"), 0.001);
                assertEquals(42.50, rs.getDouble("total_cost"), 0.001);
            }
        }
    }


    @Test
    void getLatestCartId_shouldReturnLatestInsertedId() {
        int firstId = CartService.saveRecord(1, 10.0, "en");
        int secondId = CartService.saveRecord(2, 20.0, "fi");

        assertTrue(firstId > 0);
        assertTrue(secondId > firstId);

        int latestId = CartService.getLatestCartId();

        assertEquals(secondId, latestId);
    }

    @Test
    void getLatestCartId_shouldReturnZero_whenNothingHasBeenInsertedYet() {
        int latestId = CartService.getLatestCartId();

        assertEquals(0, latestId);
    }

    @Test
    void updateRecord_shouldNotThrow_whenCartIdDoesNotExist() {
        assertDoesNotThrow(() -> CartService.updateRecord(5, 99.99, 99999));
    }
}
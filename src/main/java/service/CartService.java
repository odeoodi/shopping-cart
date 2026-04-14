package service;

import java.sql.Statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import connection.DatabaseConnection;
import exception.DatabaseException;

public class CartService {
    private CartService() {

    }
    private static String sql =
            "INSERT INTO cart_records(total_items, total_cost, language, created_at) VALUES (?, ?, ?, ?)";

    public static int saveRecord(
            int total_items,
            double total_cost,
            String language
    ) {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, total_items);
            stmt.setDouble(2, total_cost);
            stmt.setString(3, language);
            stmt.setObject(4, LocalDateTime.now());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;


    }
    private static String UPDATE_SQL =
            "UPDATE cart_records SET total_items = ?, total_cost = ? WHERE id = ?";

    public static void updateRecord(
            double total_items,
            double total_cost,
            int cart_id
    ) {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

            stmt.setDouble(1, total_items);
            stmt.setDouble(2, total_cost);
            stmt.setInt(3, cart_id);


            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    public static int getLatestCartId() {
        int cartId = 0;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT id FROM cart_records ORDER BY id DESC LIMIT 1"
             )) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cartId = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to create cart", e);
        }

        return cartId;
    }
}

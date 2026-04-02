import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ItemService {

    private static final String INSERT_SQL =
            "INSERT INTO cart_items (cart_record_id, item_number, price, quantity, subtotal) VALUES (?, ?, ?, ?, ?)";

    public static void saveItem(
            int cart_record_id,
            int item_number,
            double price,
            int quantity,
            double subtotal
    ) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {

            stmt.setDouble(1, cart_record_id);
            stmt.setDouble(2, item_number);
            stmt.setDouble(3, price);
            stmt.setInt(4, quantity);
            stmt.setDouble(5, subtotal);


            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

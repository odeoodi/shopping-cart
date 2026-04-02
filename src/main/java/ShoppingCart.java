import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ShoppingCart extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Ode Ojala: Shopping cart calculator");
        stage.setScene(scene);
        stage.show();
    }


}
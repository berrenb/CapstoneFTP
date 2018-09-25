
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        AnchorPane mainPane = FXMLLoader.load(Main.class.getResource("ftpGUI.fxml"));
       primaryStage.setScene(new Scene(mainPane));
       primaryStage.setTitle("FTP");
        primaryStage.getIcons().add(new Image("logo.png"));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

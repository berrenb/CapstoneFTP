
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Loads the .fxml file from Screen Builder
        AnchorPane mainPane = FXMLLoader.load(Main.class.getResource("ftpGUI.fxml"));
       //Sets primaryStage to a new Scene called mainPane
        primaryStage.setScene(new Scene(mainPane));
        //Sets the title of the window
       primaryStage.setTitle("FTP");
       //Sets the icon of the window
        primaryStage.getIcons().add(new Image("logo.png"));
        //Shows the window
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

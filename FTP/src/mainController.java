/*
Main Controller Class
This class will be used to create the
various actions needed to perform a successful
File transfer to my website
Created by Bryan Berrent
 */

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;



public class mainController {

    @FXML
    private AnchorPane mainStage;

    @FXML
    private TextField hostID;

    @FXML
    private TextField userID;

    @FXML
    private PasswordField passID;

    @FXML
    private TextField portID;

    @FXML
    private Button goBtn;

    @FXML
    private ListView<String> myFolder;

    @FXML
    private ListView<?> myServerFolder;

    @FXML
    private ProgressBar status;


    /*
        The method, getContents, takes no arguments and doesn't return anything.
        The purpose of this method is to get the text contents from the TextFields
        when the Go button is pressed
     */

    public void getContents() {
        //Gets the contents in hostId TextField
        hostID.getText();
        //Gets the contents in userID TextField
        userID.getText();
        //Gets the contents in passID TextField
        passID.getText();
        //Gets the contents in portID TextField
        portID.getText();
    }

    /*
        The method, openFolder, takes no arguments and doesn't return anything.
        The purpose of this method is to open a folder, and lists its content
        into the ListView, myFolder.
     */
    public void openFolder() {
        //Creates a DirectoryChooser
        final DirectoryChooser dir = new DirectoryChooser();

        //Sets the stage to the file browser
        Stage stage = (Stage) mainStage.getScene().getWindow();


        File file = dir.showDialog(stage);

        //Creates an ObservableList which will store the files in the directory
        ObservableList<String> fileList = FXCollections.observableArrayList();

        //If there is content, display them into myFolder
        if (file != null) {
            //Adds the files to an array
            String[] files = file.list();

            //For each element of the array, files, add them to the fileList
            for (String string : files) {
                fileList.add(string);
            }
            //Displays the items
            myFolder.setItems(fileList);
        }
    }

    /*
        The method, aboutProgram, does not take any arguments and does not return anything.
        The purpose of this method is to explain what the program is about using the Alert
        library into a new dialog.
     */
    public void aboutProgram() {

        //Creates an Information Alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        //Sets the title of the Alert dialog
        alert.setTitle("About this program");
        //Sets the alert header to null
        alert.setHeaderText(null);
        //The contents of the alert
        alert.setContentText("This will perform a successful File transfer to my website." +
                "\n" + "To do this, first select you local folder under file." + "\n" +
                "Next, enter the host, username, and password to connect to the website directory. The port is defaulted to Port 22."
                + "\n" + "Finally, select your file and drag it into the website directory");
        //The alert will be displayed until the OK button is clicked
        alert.showAndWait();
    }

    /*
        The method, closeProgram, does not take any arguments and does not return anything.
        The purpose of this method is to close the program.
     */
    public void closeProgram() {
        //Closes the window
        Platform.exit();
        //Finishes the program
        System.exit(0);
    }

    /*
        The method, openClient, takes no arguments and does not return anything.
        The purpose of this method is to open the file directory of the website client.
     */
    public void openClient() {


    }
}




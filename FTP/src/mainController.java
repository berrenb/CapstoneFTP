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

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.IOException;



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
    private ListView<String> myServerFolder;

    @FXML
    private ProgressBar status;

    //Variables needed for connection to the server
    private FTPClient ftp =null;
    private String host = null;
    private String user = null;
    private int port = 0;
    private String pass = null;
    private int reply = 0;

    /*
        The method, clientConnect, takes no arguments and doesn't return anything.
        The purpose of this method is to establish a connection to the client.
        The method will get the text contents from the TextFields when the Go button is pressed.
        Once the content is obtained, the method will attempt to connect to the host.
     */

    public void clientConnect() throws IOException {
        //Gets the contents in hostId TextField
        host = hostID.getText();
        //Gets the contents in userID TextField
        user = userID.getText();
        //Gets the contents in passID TextField
        pass = passID.getText();
        //Gets the contents in portID TextField
        port = Integer.parseInt(portID.getText());


    try {
        //Creates an FTP Client
        ftp = new FTPClient();

        //Connects to the host
        ftp.connect(host);

        //Obtains the reply code
        reply = ftp.getReplyCode();

        ftp.enterLocalPassiveMode();

        ftp.login(user, pass);

        System.out.println(ftp.getStatus());
        //If the reply code is negative value, the connection failed
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            //Creates an Information Alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            //Sets the title of the Alert dialog
            alert.setTitle("ERROR");
            //Sets the alert header to null
            alert.setHeaderText(null);
            //The contents of the alert
            alert.setContentText("There was an issue connecting. Please try again");
            //The alert will be displayed until the OK button is clicked
            alert.showAndWait();
        }


        //If the connection is successful, login
        else {
            //Creates an Information Alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            //Sets the title of the Alert dialog
            alert.setTitle("SUCCESS");
            //Sets the alert header to null
            alert.setHeaderText(null);
            //The contents of the alert
            alert.setContentText("You're connected to: " + host);
            //The alert will be displayed until the OK button is clicked
            alert.showAndWait();
        }
        if (ftp.login(user, pass) == false){
            ftp.disconnect();
            //Creates an Information Alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            //Sets the title of the Alert dialog
            alert.setTitle("ERROR");
            //Sets the alert header to null
            alert.setHeaderText(null);
            //The contents of the alert
            alert.setContentText("The username or password was incorrect");
            //The alert will be displayed until the OK button is clicked
            alert.showAndWait();

            userID.setText("");
            passID.setText("");

        }
        else{
            //Creates an Information Alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            //Sets the title of the Alert dialog
            alert.setTitle("SUCCESS");
            //Sets the alert header to null
            alert.setHeaderText(null);
            //The contents of the alert
            alert.setContentText("The username and password was correct!");
            //The alert will be displayed until the OK button is clicked
            alert.showAndWait();

        }



    }
    catch(Exception e) {
        ftp.disconnect();
        //Creates an Information Alert
        Alert alert = new Alert(Alert.AlertType.WARNING);
        //Sets the title of the Alert dialog
        alert.setTitle("ERROR");
        //Sets the alert header to null
        alert.setHeaderText(null);
        //The contents of the alert
        alert.setContentText("I'm sorry. There was an unknown error");
        //The alert will be displayed until the OK button is clicked
        alert.showAndWait();
    }



        }

    /*
        The method, openFolder, takes no arguments and doesn't return anything.
        The purpose of this method is to open a folder, and lists its content
        into the ListView, myFolder.
     */
    public void openFolder(){
        //Creates a DirectoryChooser
        final DirectoryChooser dir = new DirectoryChooser();

        //Sets the stage to the file browser
        Stage stage = (Stage) mainStage.getScene().getWindow();


        File file = dir.showDialog(stage);

        //Creates an ObservableList which will store the files in the directory
        ObservableList<String> fileList = FXCollections.observableArrayList();


        status.setProgress(0);



        //If there is content, display them into myFolder
        if (file != null) {
            //Adds the files to an array
            String[] files = file.list();

            //For each element of the array, files, add them to the fileList
            for (String string : files) {
                status.setProgress(files.length);
                fileList.add(string);
            }

            //Displays the items
            myFolder.setItems(fileList);

            //Creates an Information Alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            //Sets the title of the Alert dialog
            alert.setTitle("COMPLETE");
            //Sets the alert header to null
            alert.setHeaderText(null);
            //The contents of the alert
            alert.setContentText("Directory: " + file);
            //The alert will be displayed until the OK button is clicked
            alert.showAndWait();

            status.setProgress(0);
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


}




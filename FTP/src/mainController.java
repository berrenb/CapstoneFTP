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

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPClient;

import java.util.Scanner;
import java.io.*;


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

    @FXML
    private Button localRefresh;

    @FXML
    private Button serverRefresh;

    //Variables needed for FTP purposes
    private FTPClient ftp =null;
    private String host = null;
    private String user = null;
    private int port = 0;
    private String pass = null;
    private int reply = 0;
    private String localDir = null;
    private String remoteDir = null;
    private ObservableList<String> localList = null;
    private ObservableList<String> remoteList = null;
    private String[] localNames = null;
    private String[] serverNames = null;
    private File file = null;

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
        ftp.connect(host, port);

        System.out.println(ftp.getReplyString());
        //login into server
        ftp.login(user, pass);


        //Creates an ObservableList which holds folders/files in the directory
        remoteList = FXCollections.observableArrayList();

        serverNames = ftp.listNames();
        remoteDir = ftp.printWorkingDirectory();
        //myServerFolder.setItems(fileList);
        for(String name : serverNames){
            remoteList.add(name);
        }
        //Displays the items
        myServerFolder.setItems(remoteList);
        //Obtains the reply code
        reply = ftp.getReplyCode();

        ftp.enterLocalPassiveMode();



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

            hostID.setText("");
            userID.setText("");
            passID.setText("");
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

            hostID.setText("");
            userID.setText("");
            passID.setText("");

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
        localList = FXCollections.observableArrayList();


        localDir = file.toString();
        //If there is content, display them into myFolder
        if (file != null) {
            //Adds the files to an array
            localNames = file.list();

            //For each element of the array, files, add them to the fileList
            for (String string : localNames) {
                localList.add(string);
            }

            //Displays the items
            myFolder.setItems(localList);

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
        alert.setContentText("This program will perform a successful File Transfer" +
                "\n" + "To do this, first select your local folder under file." + "\n" +
                "Next, enter the host, username, and password to connect to the website directory. The port is defaulted to Port 21."
                + "\n" + "Finally, select the file you wish to upload or download and click the corresponding button.");
        //The alert will be displayed until the OK button is clicked
        alert.showAndWait();
    }

    /*
        The method, uploadFile, does not take any arguments and does not return anything. The purpose of this
        method is to upload a file from the local directory to the remote directory.
     */
    public void uploadFile(){

        try {

            //Connect to the server
            ftp.connect(host, port);


            //Login with the username and password
            ftp.login(user, pass);

            //This line is used so you are able to transfer files
            ftp.enterLocalPassiveMode();

            //Sets file type of files transferred
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            //Stores the file chosen in localFile
            ObservableList<String> upFile;
            upFile = myFolder.getSelectionModel().getSelectedItems();

            for(String x:upFile) {

                File localFile = new File(localDir + "/" + x);
                status.setProgress(0.25F);
                //Stores the new name of the file to be uploaded to the server
                String remoteFile = (remoteDir + "/" + x);
                status.setProgress(0.50F);
                InputStream inputStream = new FileInputStream(localFile);
                status.setProgress(0.75F);
                boolean done = ftp.storeFile(remoteFile, inputStream);
                inputStream.close();

                if (done) {
                    status.setProgress(1F);
                    //Creates an Confirmation Alert
                    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    //Sets the title of the Alert dialog
                    confirmAlert.setTitle("SUCCESS");
                    //Sets the alert header to null
                    confirmAlert.setHeaderText(null);
                    //The contents of the alert
                    confirmAlert.setContentText("The file " + x + " has successfully uploaded");
                    //The alert will be displayed until the OK button is clicked
                    confirmAlert.showAndWait();
                    status.setProgress(0);
                }
            }

        } catch (IOException ex) {
            //Creates an Confirmation Alert
            Alert ioAlert = new Alert(Alert.AlertType.ERROR);
            //Sets the title of the Alert dialog
            ioAlert.setTitle("ERROR");
            //Sets the alert header to null
            ioAlert.setHeaderText(null);
            //The contents of the alert
            ioAlert.setContentText(ex.getMessage());
            //The alert will be displayed until the OK button is clicked
            ioAlert.showAndWait();

            ex.printStackTrace();
        } finally {
            try {
                if (ftp.isConnected()) {
                    ftp.logout();
                    ftp.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    public void downloadFile(){
        try {
            ftp.connect(host, port);
            ftp.login(user, pass);
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            ObservableList<String> downFile;
            downFile = myServerFolder.getSelectionModel().getSelectedItems();

            for(String x:downFile) {
                String remoteFile = (remoteDir + "/" + x);


                File downloadFile = new File(localDir + "/" + x);

                localList.add(x);

                OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile));

                boolean success = ftp.retrieveFile(remoteFile, outputStream1);
                outputStream1.close();

                if (success) {
                    //Creates an Information Alert
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    //Sets the title of the Alert dialog
                    alert.setTitle("SUCCESS");
                    //Sets the alert header to null
                    alert.setHeaderText(null);
                    //The contents of the alert
                    alert.setContentText("The file " + downloadFile + " has successfully dowloaded");
                    //The alert will be displayed until the OK button is clicked
                    alert.showAndWait();

                }
            }
        }
        catch (IOException ex) {
            //Creates an Error Alert
            Alert ioAlert = new Alert(Alert.AlertType.ERROR);
            //Sets the title of the Alert dialog
            ioAlert.setTitle("ERROR");
            //Sets the alert header to null
            ioAlert.setHeaderText(null);
            //The contents of the alert
            ioAlert.setContentText(ex.getMessage());
            //The alert will be displayed until the OK button is clicked
            ioAlert.showAndWait();

            ex.printStackTrace();
        } finally {
            try {
                if (ftp.isConnected()) {
                    ftp.logout();
                    ftp.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void localDirectoryRefresh(){
        try{
            myFolder.getItems().clear();
                //For each element of the array, files, add them to the fileList
                for (String string : localNames) {
                    localList.add(string);
                }

                //Displays the items
                myFolder.setItems(localList);


        }
        catch(Exception e){
            System.out.println("There was an error");
        }
    }

    public void serverDirectoryRefresh() {
        try {
            //Creates an FTP Client
            ftp = new FTPClient();

            //Connects to the host
            ftp.connect(host, port);

            System.out.println(ftp.getReplyString());
            //login into server
            ftp.login(user, pass);


            //Creates an ObservableList which holds folders/files in the directory
            remoteList = FXCollections.observableArrayList();

            serverNames = ftp.listNames();
            remoteDir = ftp.printWorkingDirectory();
            //myServerFolder.setItems(fileList);
            for (String name : serverNames) {
                remoteList.add(name);
            }
            //Displays the items
            myServerFolder.setItems(remoteList);
        }
        catch(Exception e){
            System.out.println("There was an error");
        }
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




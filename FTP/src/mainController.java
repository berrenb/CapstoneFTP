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
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPClient;

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

        host = hostID.getText();

        user = userID.getText();

        pass = passID.getText();

        port = Integer.parseInt(portID.getText());

        try {

            ftp = new FTPClient();

            ftp.connect(host, port);

            ftp.login(user, pass);

            remoteList = FXCollections.observableArrayList();

            serverNames = ftp.listNames();

            remoteDir = ftp.printWorkingDirectory();

            for(String name : serverNames){
                remoteList.add(name);
            }

            myServerFolder.setItems(remoteList);

            reply = ftp.getReplyCode();

            ftp.enterLocalPassiveMode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();

                Alert alert = new Alert(Alert.AlertType.WARNING);

                alert.setTitle("ERROR");

                alert.setHeaderText(null);

                alert.setContentText("There was an issue connecting. Please try again");

                alert.showAndWait();

                hostID.setText("");
                userID.setText("");
                passID.setText("");
            }

            else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                alert.setTitle("SUCCESS");

                alert.setHeaderText(null);

                alert.setContentText("You're connected to: " + host);

                alert.showAndWait();
            }

            if (ftp.login(user, pass) == false){
                ftp.disconnect();

                Alert alert = new Alert(Alert.AlertType.WARNING);

                alert.setTitle("ERROR");

                alert.setHeaderText(null);

                alert.setContentText("The username or password was incorrect");

                alert.showAndWait();

                userID.setText("");
                passID.setText("");
            }

            else{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                alert.setTitle("SUCCESS");

                alert.setHeaderText(null);

                alert.setContentText("The username and password was correct!");

                alert.showAndWait();

                hostID.setText("");
                userID.setText("");
                passID.setText("");
            }
        }
        catch(Exception e) {
            ftp.disconnect();

            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setTitle("ERROR");

            alert.setHeaderText(null);

            alert.setContentText("I'm sorry. There was an unknown error");

            alert.showAndWait();
        }
    }

    /*
        The method, openFolder, takes no arguments and doesn't return anything.
        The purpose of this method is to open a folder, and lists its content
        into the ListView, myFolder.
     */
    public void openFolder(){

        final DirectoryChooser dir = new DirectoryChooser();


        Stage stage = (Stage) mainStage.getScene().getWindow();

        File file = dir.showDialog(stage);

        localList = FXCollections.observableArrayList();

        localDir = file.toString();

        if (file != null) {

            localNames = file.list();

            for (String string : localNames) {
                localList.add(string);
            }

            myFolder.setItems(localList);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("COMPLETE");

            alert.setHeaderText(null);

            alert.setContentText("Directory: " + file);

            alert.showAndWait();

        }
    }

    /*
        The method, aboutProgram, does not take any arguments and does not return anything.
        The purpose of this method is to explain what the program is about using the Alert
        library into a new dialog.
     */
    public void aboutProgram() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("About this program");

        alert.setHeaderText(null);

        alert.setContentText("This program will perform a successful File Transfer" +
                "\n" + "To do this, first select your local folder under file." + "\n" +
                "Next, enter the host, username, and password to connect to the website directory. The port is defaulted to Port 21."
                + "\n" + "Finally, select the file you wish to upload or download and click the corresponding button.");

        alert.showAndWait();
    }

    /*
        The method, uploadFile, does not take any arguments and does not return anything. The purpose of this
        method is to upload a file from the local directory to the remote directory.
     */
    public void uploadFile(){

        try {

            ftp.connect(host, port);

            ftp.login(user, pass);

            ftp.enterLocalPassiveMode();

            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            ObservableList<String> upFile;
            upFile = myFolder.getSelectionModel().getSelectedItems();

            for(String x:upFile) {

                File localFile = new File(localDir + "/" + x);
                status.setProgress(0.25F);

                String remoteFile = (remoteDir + "/" + x);
                status.setProgress(0.50F);

                InputStream inputStream = new FileInputStream(localFile);
                status.setProgress(0.75F);

                boolean done = ftp.storeFile(remoteFile, inputStream);
                inputStream.close();

                if (done) {
                    status.setProgress(1F);

                    remoteList.add(x);

                    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);

                    confirmAlert.setTitle("SUCCESS");

                    confirmAlert.setHeaderText(null);

                    confirmAlert.setContentText("The file " + x + " has successfully uploaded");

                    confirmAlert.showAndWait();

                    status.setProgress(0F);
                }
            }

        } catch (IOException ex) {
            Alert ioAlert = new Alert(Alert.AlertType.ERROR);

            ioAlert.setTitle("ERROR");

            ioAlert.setHeaderText(null);

            ioAlert.setContentText(ex.getMessage());

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

/*
    The method, downloadFile, does not take any arguments and does not return anything. The purpose of this
    method is to download a file from the remote directory to the local directory.
 */
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
                status.setProgress(0.25F);

                File downloadFile = new File(localDir + "/" + x);
                status.setProgress(0.50F);

                localList.add(x);

                OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile));
                status.setProgress(0.75F);

                boolean success = ftp.retrieveFile(remoteFile, outputStream1);
                outputStream1.close();

                if (success) {
                    status.setProgress(1F);

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                    alert.setTitle("SUCCESS");

                    alert.setHeaderText(null);

                    alert.setContentText("The file " + x + " has successfully dowloaded");

                    alert.showAndWait();

                    status.setProgress(0F);
                }
            }
        }
        catch (IOException ex) {

            Alert ioAlert = new Alert(Alert.AlertType.ERROR);

            ioAlert.setTitle("ERROR");

            ioAlert.setHeaderText(null);

            ioAlert.setContentText(ex.getMessage());

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
    /*
        The method, localDirectoryRefresh, does not take any arguments and does not return anything. The purpose of this
        method is to refresh the list of files on the local directory.
     */
    public void localDirectoryRefresh(){
        try{
            myFolder.getItems().clear();
            File folder = new File(localDir);

                for (File fileEntry: folder.listFiles()) {
                    localList.add(fileEntry.getName());
                }


                myFolder.setItems(localList);

    }
        catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("ERROR");

            alert.setHeaderText(null);

            alert.setContentText(ex.getMessage());

            alert.showAndWait();

            ex.printStackTrace();
        }
    }
    /*
        The method, serverDirectoryRefresh, does not take any arguments and does not return anything. The purpose of this
        method is to refresh the list of files on the server directory.
     */
    public void serverDirectoryRefresh() {
        try {

            ftp = new FTPClient();

            ftp.connect(host, port);

            System.out.println(ftp.getReplyString());

            ftp.login(user, pass);

            remoteList = FXCollections.observableArrayList();

            serverNames = ftp.listNames();
            remoteDir = ftp.printWorkingDirectory();

            for (String name : serverNames) {
                remoteList.add(name);
            }

            myServerFolder.setItems(remoteList);
        }
        catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("ERROR");

            alert.setHeaderText(null);

            alert.setContentText(ex.getMessage());

            alert.showAndWait();

            ex.printStackTrace();
        }
    }


    /*
        The method, closeProgram, does not take any arguments and does not return anything.
        The purpose of this method is to close the program.
     */
    public void closeProgram() {
        Platform.exit();

        System.exit(0);
    }


}




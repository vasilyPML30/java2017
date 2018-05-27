package net.netau.vasyoid;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Graphical application that allows to walk through directories on a remote FTP server.
 */
public class FtpView extends Application {

    @SuppressWarnings("FieldCanBeLocal")
    private final static int MIN_WINDOW_WIDTH = 300;
    @SuppressWarnings("FieldCanBeLocal")
    private final static int MIN_WINDOW_HEIGHT = 400;

    private static FtpClient client;
    private static File currentDirectory = new File("/");
    private static ListView<String> folderContents = new ListView<>();
    private static TextField currentPath = new TextField();


    /**
     * Parse command line arguments and launch the application.
     * @param args command line arguments. args[0] -- remote address, args[1] -- remote port.
     */
    public static void main(@NotNull String[] args) {
        if (args.length != 2) {
            System.out.println("Invalid arguments:\nFtpView <remote address> <remote port>");
            System.exit(0);
        }
        try {
            client = new FtpClient(InetAddress.getByName(args[0]), Integer.parseInt(args[1]));
        } catch (FtpException e) {
            System.out.println("Could not establish connection: " + e.getCause().getMessage());
            System.exit(0);
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + args[0]);
            System.exit(0);
        }
        Application.launch(args);
    }

    private static void alert(@NotNull Alert.AlertType type,
                              @NotNull String title,
                              @NotNull String description) {
        Alert alertDialog = new Alert(type);
        alertDialog.setHeaderText(title);
        alertDialog.setContentText(description);
        alertDialog.showAndWait();
    }

    private void rebuildListView(@NotNull String newDirectory) {
        currentDirectory = Paths.get(currentDirectory + "/" + newDirectory)
                .normalize().toFile();
        try {
            ObservableList<String> items = FXCollections.observableArrayList(
                    client.list(currentDirectory.toString())
                            .stream()
                            .map(MyFile::toString)
                            .collect(Collectors.toList()));
            folderContents.setItems(items);
            if (!currentDirectory.getPath().equals("/")) {
                folderContents.getItems().add(0, "../");
            }
            currentPath.setText(currentDirectory.toString());
        } catch (FtpException e) {
            alert(Alert.AlertType.ERROR, "Could not list files.", e.getCause().getMessage());
        }
    }

    private static void getFile(@NotNull String file) {
        try {
            File result = client.get(currentDirectory + "/" + file);
            if (result == null) {
                alert(Alert.AlertType.ERROR, "File is empty.", "");
            } else {
                alert(Alert.AlertType.INFORMATION, "File saved.",
                        "file name: " + result.getName());
            }
        } catch (FtpException e) {
            alert(Alert.AlertType.ERROR, "Could not save file.", e.getCause().getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * Initialise the interface.
     * @param primaryStage main scene.
     */
    @Override
    public void start(@NotNull Stage primaryStage) {
        folderContents.setMaxHeight(Double.MAX_VALUE);
        folderContents.setOnMouseClicked(click -> {
            if (click.getClickCount() >= 2) {
                String item = folderContents.getSelectionModel().getSelectedItem();
                if (item.endsWith("/")) {
                    rebuildListView(item);
                } else {
                    getFile(item);
                }
            }
        });
        folderContents.setOnKeyPressed(keyEvent -> {
            String item = folderContents.getSelectionModel().getSelectedItem();
            if (keyEvent.getCode().equals(KeyCode.ENTER) &&
                    item != null) {
                if (item.endsWith("/")) {
                    rebuildListView(item);
                } else {
                    getFile(item);
                }
            } else if (keyEvent.getCode().equals(KeyCode.BACK_SPACE)) {
                rebuildListView("..");
            }
        });
        currentPath.setEditable(false);
        currentPath.setFocusTraversable(false);
        VBox layout = new VBox();
        VBox.setVgrow(folderContents, Priority.ALWAYS);
        layout.getChildren().addAll(currentPath, folderContents);
        Scene scene = new Scene(layout, MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                primaryStage.close();
            }
        });
        primaryStage.setMinWidth(MIN_WINDOW_WIDTH);
        primaryStage.setMinHeight(MIN_WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
        rebuildListView("");
    }

    /**
     * {@inheritDoc}
     * Closes the connection with the server and stops the application.
     */
    @Override
    public void stop() throws Exception {
        try {
            client.close();
            super.stop();
        } catch (FtpException e) {
            System.out.println("Could not close connection properly: " +
                    e.getCause().getMessage());
        }
    }

}

package com.example.userlogin;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class HelloApplication extends Application {
    private File selectedFile;
    private File selectedDir;
    private TextField keyField = new TextField();

    @Override
    public void start(Stage stage) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(30));
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setAlignment(Pos.CENTER);


        Label heading = new Label("Welcome to the SARIAN");
        heading.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #0d47a1;");

        TextField userName = new TextField();
        PasswordField passwordField = new PasswordField();
        TextField ipField = new TextField();
        ipField.setPromptText("Enter receiver IP address");

        TextField portField = new TextField("5000");

        ToggleGroup roleGroup = new ToggleGroup();
        RadioButton receiverBtn = new RadioButton("Receiver");
        RadioButton senderBtn = new RadioButton("Sender");
        receiverBtn.setToggleGroup(roleGroup);
        senderBtn.setToggleGroup(roleGroup);

        Button browseBtn = new Button("Browse");
        Button submitBtn = new Button("Submit");
        Button registerBtn = new Button("Register");

        Label keyLabel = new Label("Encryption/Decryption Key:");
        keyField.setPromptText("16-character key");

        // Button Styling
        String btnStyle = "-fx-background-color: #1976d2; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;";
        String btnHover = "-fx-background-color: #1565c0;";

        browseBtn.setStyle(btnStyle);
        submitBtn.setStyle(btnStyle);
        registerBtn.setStyle(btnStyle);

        browseBtn.setOnMouseEntered(e -> browseBtn.setStyle(btnHover));
        browseBtn.setOnMouseExited(e -> browseBtn.setStyle(btnStyle));
        submitBtn.setOnMouseEntered(e -> submitBtn.setStyle(btnHover));
        submitBtn.setOnMouseExited(e -> submitBtn.setStyle(btnStyle));
        registerBtn.setOnMouseEntered(e -> registerBtn.setStyle(btnHover));
        registerBtn.setOnMouseExited(e -> registerBtn.setStyle(btnStyle));


        gridPane.addRow(0, new Label("Username:"), userName);
        gridPane.addRow(1, new Label("Password:"), passwordField);
        gridPane.addRow(2, new Label("Role:"), new HBox(20, receiverBtn, senderBtn));
        gridPane.addRow(3, new Label("File/Directory:"), browseBtn);
        gridPane.addRow(4, new Label("Port:"), portField);
        gridPane.addRow(5, keyLabel, keyField);
        gridPane.addRow(6, new Label("IP Address:"), ipField);
        gridPane.add(registerBtn, 1, 7);
        gridPane.add(submitBtn, 1, 8);

        keyLabel.setVisible(true);
        keyField.setVisible(true);
        ipField.setVisible(false);

        VBox content = new VBox(30, heading, gridPane);
        content.setAlignment(Pos.CENTER);


        BorderPane root = new BorderPane();
        root.setCenter(content);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #e3f2fd, #bbdefb);");


        roleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            boolean isReceiver = receiverBtn.isSelected();
            keyLabel.setVisible(true);
            keyField.setVisible(true);
            ipField.setVisible(!isReceiver);
            selectedFile = null;
            selectedDir = null;
        });

        browseBtn.setOnAction(e -> {
            if (senderBtn.isSelected()) {
                FileChooser fc = new FileChooser();
                fc.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("All Files", "*.*"),
                        new FileChooser.ExtensionFilter("JPEG Files", "*.jpg", "*.jpeg"),
                        new FileChooser.ExtensionFilter("PNG Files", "*.png")
                );
                selectedFile = fc.showOpenDialog(stage);
                if (selectedFile != null) {
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                }
            } else if (receiverBtn.isSelected()) {
                DirectoryChooser dc = new DirectoryChooser();
                selectedDir = dc.showDialog(stage);
                if (selectedDir != null) {
                    System.out.println("Selected directory: " + selectedDir.getAbsolutePath());
                }
            } else {
                showAlert("Role not selected", "Please select Sender or Receiver role first.", Alert.AlertType.WARNING);
            }
        });

        submitBtn.setOnAction(e -> new Thread(() -> {
            try {
                String key = keyField.getText();
                if (key == null || key.length() != 16) {
                    throw new IllegalArgumentException("Key must be exactly 16 characters");
                }

                int port;
                try {
                    port = Integer.parseInt(portField.getText());
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Invalid port number");
                }

                if (senderBtn.isSelected()) {
                    if (selectedFile == null) {
                        throw new IllegalArgumentException("Please select a file to send");
                    }
                    if (!selectedFile.exists() || !selectedFile.isFile() || !selectedFile.canRead()) {
                        throw new IllegalArgumentException("Selected file is invalid or unreadable");
                    }

                    String ip = ipField.getText();
                    if (ip == null || ip.trim().isEmpty()) {
                        throw new IllegalArgumentException("IP address is required for sender");
                    }

                    new Client().sendFile(selectedFile.getAbsolutePath(), ip, port, key);
                    Platform.runLater(() -> showAlert("Success", "File sent successfully.", Alert.AlertType.INFORMATION));

                } else if (receiverBtn.isSelected()) {
                    if (selectedDir == null) {
                        throw new IllegalArgumentException("Please select a directory to save the received file");
                    }
                    if (!selectedDir.exists() || !selectedDir.isDirectory() || !selectedDir.canWrite()) {
                        throw new IllegalArgumentException("Selected directory is invalid or not writable");
                    }

                    new Server().receiveFile(port, selectedDir.getAbsolutePath(), key);
                    Platform.runLater(() -> showAlert("Success", "File received successfully.", Alert.AlertType.INFORMATION));
                } else {
                    throw new IllegalArgumentException("Please select a role: Sender or Receiver");
                }

            } catch (Exception ex) {
                Platform.runLater(() -> showAlert("Error", ex.getMessage(), Alert.AlertType.ERROR));
            }
        }).start());

        Scene scene = new Scene(root, 700, 600);
        stage.setTitle("Secure File Transfer");
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

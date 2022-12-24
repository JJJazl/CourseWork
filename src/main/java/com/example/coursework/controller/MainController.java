package com.example.coursework.controller;

import com.example.coursework.ActionManager;
import com.example.coursework.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    private final ActionManager actionManager;

    private Stage stage;
    @FXML
    private TextArea textArea;
    @FXML
    private RadioButton radioButtonForEncrypt;
    @FXML
    private RadioButton radioButtonForDecrypt;
    @FXML
    private RadioButton radioButtonForClearAllText;
    @FXML
    private RadioButton radioButtonForClearNewText;
    private String textFromFile;

    public MainController() {
        this.actionManager = ActionManager.getInstance();
    }
    public void go() {
        if (radioButtonForDecrypt.isSelected()) {
            this.textFromFile = actionManager.decrypt(
                    actionManager.readFromFile(),
                    "secretKey");
            textArea.setText(this.textFromFile);
        } else if (radioButtonForEncrypt.isSelected()) {
            String encryptedText = actionManager.encrypt(textArea.getText(), "secretKey");
            if (encryptedText != null) {
                actionManager.writeToFile(encryptedText);
                textArea.setText("Text successfully encrypted!");
            }
        }
    }
    public void clear() {
        if (radioButtonForClearNewText.isSelected()) {
            textArea.setText(this.textFromFile);
        } else if (radioButtonForClearAllText.isSelected()) {
            actionManager.writeToFile("");
            textArea.setText("");
        }
    }
    public void logout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout!");
        alert.setContentText("Do you want to logout? ");

        if (alert.showAndWait().get() == ButtonType.OK) {
            switchToLoginPage(event);
        }
    }
    private void switchToLoginPage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void switchToRegisterPage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("register-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void about(ActionEvent event) throws IOException {
        Label secondLabel = new Label("Made by a student Renat Safarov group KІUKI-20-10");

        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(secondLabel);

        Scene secondScene = new Scene(secondaryLayout, 330, 100);

        Stage newWindow = new Stage();
        newWindow.setTitle("About");
        newWindow.setScene(secondScene);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        newWindow.setX(stage.getX() + 200);
        newWindow.setY(stage.getY() + 100);

        newWindow.show();
    }
    public void deleteAccount(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete account");
        alert.setHeaderText("You're about to delete account!");
        alert.setContentText("Do you want to delete account? ");
        if (alert.showAndWait().get() == ButtonType.OK) {
            if (actionManager.deleteAccount()) {
                switchToRegisterPage(event);
            }
        }
    }
}

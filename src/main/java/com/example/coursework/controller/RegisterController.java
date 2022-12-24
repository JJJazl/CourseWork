package com.example.coursework.controller;

import com.example.coursework.ActionManager;
import com.example.coursework.Main;
import com.example.coursework.exception.ConfirmPasswordException;
import com.example.coursework.exception.UserWithSameUsernameAlreadyExist;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {
    private final ActionManager actionManager;
    private Stage stage;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField confirmPassword;
    @FXML
    private Label usernameError;
    @FXML
    private Label passwordError;
    @FXML
    private Label confirmPasswordError;

    public RegisterController() {
        actionManager = ActionManager.getInstance();
    }

    public void signUp(ActionEvent event) throws IOException {
        if (username.getText().equals("")
                || password.getText().equals("")
                || confirmPassword.getText().equals("")) {
            setErrorLabels("All fields must be filled");
        } else {
            setErrorLabels("");
            try {
                actionManager.createUserAndFile(username.getText(), password.getText(), confirmPassword.getText());
                switchToMainPage(event);
            } catch (ConfirmPasswordException e) {
                confirmPasswordError.setText(e.getMessage());
            } catch (UserWithSameUsernameAlreadyExist e) {
                usernameError.setText(e.getMessage());
            }
        }
    }
    public void switchToLoginPage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMainPage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    private void setErrorLabels(String text) {
        usernameError.setText(text);
        passwordError.setText(text);
        confirmPasswordError.setText(text);
    }
}

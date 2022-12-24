package com.example.coursework.controller;

import com.example.coursework.ActionManager;
import com.example.coursework.Main;
import com.example.coursework.exception.IncorrectPasswordException;
import com.example.coursework.exception.UserNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    private final ActionManager actionManager;
    private Stage stage;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Label inputError;

    public LoginController() {
        actionManager = ActionManager.getInstance();
    }
    public void signIn(ActionEvent event) throws IOException {
        try {
            actionManager.login(username.getText(), password.getText());
            switchToMainPage(event);
        } catch (UserNotFoundException | IncorrectPasswordException e) {
            inputError.setText(e.getMessage());
        }
    }
    public void switchToRegisterPage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("register-page.fxml"));
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
}

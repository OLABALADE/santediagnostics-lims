package com.santediagnostics.lims.controller.auth;

import com.santediagnostics.lims.service.AuthService;
import com.santediagnostics.lims.util.SceneManager;
import com.santediagnostics.lims.util.ValidationUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

  @FXML
  private TextField nameField;
  @FXML
  private TextField emailField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private Label errorLabel;

  private final AuthService authService = new AuthService();

  @FXML
  private void handleRegister(ActionEvent event) {
    errorLabel.setText("");
    String name = nameField.getText().trim();
    String email = emailField.getText().trim();
    String pass = passwordField.getText();

    if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
      errorLabel.setText("All fields are required.");
      return;
    }
    if (!ValidationUtil.isValidEmail(email)) {
      errorLabel.setText("Invalid email address.");
      return;
    }
    if (!ValidationUtil.isValidPassword(pass)) {
      errorLabel.setText("Password must be at least 8 characters.");
      return;
    }
    try {
      authService.registerCustomer(name, email, pass);
      errorLabel.setStyle("-fx-text-fill: green;");
      errorLabel.setText("Registration successful! Check your email to verify your account.");
    } catch (Exception e) {
      errorLabel.setText(e.getMessage());
    }
  }

  @FXML
  private void goToLogin(ActionEvent event) {
    try {
      SceneManager.navigateTo(event, "/fxml/auth/Login.fxml");
    } catch (Exception e) {
      errorLabel.setText(e.getMessage());
    }
  }
}

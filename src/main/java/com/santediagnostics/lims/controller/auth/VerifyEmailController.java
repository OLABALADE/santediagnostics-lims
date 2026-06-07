package com.santediagnostics.lims.controller.auth;

import com.santediagnostics.lims.service.AuthService;
import com.santediagnostics.lims.util.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class VerifyEmailController {

  @FXML
  private TextField tokenField;
  @FXML
  private Label errorLabel;

  private final AuthService authService = new AuthService();

  @FXML
  private void handleVerify(ActionEvent event) {
    errorLabel.setText("");
    String token = tokenField.getText().trim();
    if (token.isEmpty()) {
      errorLabel.setText("Please enter your verification token.");
      return;
    }
    try {
      authService.verifyEmail(token);
      errorLabel.setStyle("-fx-text-fill: green;");
      errorLabel.setText("Email verified! You can now log in.");
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
